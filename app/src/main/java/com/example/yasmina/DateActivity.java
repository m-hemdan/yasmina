package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Console;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


import io.paperdb.Paper;

import static android.widget.LinearLayout.*;

public class DateActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    String ccurrentDate = null, currentTime = null;
    public int y = 0;
    Button btPrev, btNext;
    List<SelectDate> mBooking;
    List<String> dayDateList;
    List<Integer> intList;
    List<TimeClass> tTimeList;
    public List<Event> arrayEvent;
    CompactCalendarView compactCalendarView;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    TimeInDayadapter AdapterTime;
    ListView bookingsListView;
    FirebaseUser firebaseUser;
    Calendar calendarView;
    TextView currentdateView, nameItem, subItem, durationItem, priceItem;
    androidx.appcompat.widget.Toolbar toolbarCalendar;
    Controller controller;
    AdapterCheckOut adapterCheckOut;
    Button btnOpen, btnClose, btnPush, btnShow;
    private boolean shouldShow = false;
    LinearLayout linearLayoutVertical;
    RelativeLayout itemlayout;
    LinearLayoutManager linearLayoutManager;
    ModelProduct modelProduct;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    List<String> HorizontalMultibleBook;
    String horizontalCurrentDate;
    TelephonyManager manager ;
    int orientation;
    int i = 1;

    List<String> mutableBookings = new ArrayList<>();
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        InternetConnection internetConnection = new InternetConnection();
        if (!internetConnection.isConnected(this)) {
            OfflineNetwork offlineNetwork = new OfflineNetwork();
            offlineNetwork.alertShowFunction(this);
        }


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordianteLayout);
        itemlayout = (RelativeLayout) findViewById(R.id.ItemLayout);
        manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        orientation = getResources().getConfiguration().orientation;
        recyclerView = (RecyclerView) findViewById(R.id.avaliable_time_recycle_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
       /* if (orientation == Configuration.ORIENTATION_LANDSCAPE || manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {

            if (horizontalCurrentDate !=null && HorizontalMultibleBook !=null) {
                AdapterTime = new TimeInDayadapter(tTimeList, DateActivity.this, HorizontalMultibleBook, horizontalCurrentDate.substring(0, 10));
                recyclerView.setAdapter(AdapterTime);
                AdapterTime.notifyDataSetChanged();
            }
            Toast.makeText(this, "landscape", Toast.LENGTH_LONG).show();
         /*   itemlayout.setVisibility(GONE);
            controller = (Controller) getApplication();
            recyclerView = (RecyclerView) findViewById(R.id.itemInCheckOutRecycleView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            Toast.makeText(this,controller.AllProduct().toString() ,Toast.LENGTH_SHORT).show();
            List<ModelProduct>list= Paper.book().read("modelproductList");
             adapterCheckOut =new  AdapterCheckOut(list,this);
             recyclerView.setAdapter(adapterCheckOut);
             SwapToDeleteItem swapToDeleteItem=new SwapToDeleteItem(this,adapterCheckOut,recyclerView,coordinatorLayout);
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swapToDeleteItem);
            itemTouchhelper.attachToRecyclerView(recyclerView);*/

        toolbarCalendar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarCalendar);
        setSupportActionBar(toolbarCalendar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("selectData");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("selectData");
        mDatabase3 = FirebaseDatabase.getInstance().getReference("selectData");
        Gson gson = new Gson();
        modelProduct = gson.fromJson(getIntent().getStringExtra("myjson"), ModelProduct.class);
        nameItem = (TextView) findViewById(R.id.nameItem);
        subItem = (TextView) findViewById(R.id.subItem);
        durationItem = (TextView) findViewById(R.id.durationItem);
        priceItem = (TextView) findViewById(R.id.priceItem);

        nameItem.setText(modelProduct.getTextProduct());
        subItem.setText(modelProduct.getSubTextProduct());
        durationItem.setText(String.valueOf(modelProduct.getDurationProduct()));
        priceItem.setText(modelProduct.getPriceProduct());

        dayDateList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        arrayEvent = new ArrayList<>();
        mBooking = new ArrayList<>();
        tTimeList = new ArrayList<>();
        tTimeList.add(new TimeClass("10 am", R.drawable.clock_ten));
        tTimeList.add(new TimeClass("11 am", R.drawable.clock_eleven));
        tTimeList.add(new TimeClass("12 pm", R.drawable.clock_twelve));
        tTimeList.add(new TimeClass("1 pm", R.drawable.clock_one));
        tTimeList.add(new TimeClass("2 pm", R.drawable.clock_two));
        tTimeList.add(new TimeClass("3 pm", R.drawable.clock_three));
        tTimeList.add(new TimeClass("4 pm", R.drawable.clock_four));
        mutableBookings = new ArrayList<>();
        mutableBookings.add("");
        //  AdapterTime = new TimeInDayadapter(tTimeList, this, mutableBookings,ccurrentDate);
        //  recyclerView.setAdapter(AdapterTime);
        currentdateView = (TextView) findViewById(R.id.currentdate);
        btNext = (Button) findViewById(R.id.button_next);
        btPrev = (Button) findViewById(R.id.button_previous);

        btnShow = (Button) findViewById(R.id.showList);
        if (savedInstanceState == null) {
            btnShow.setVisibility(GONE);
        }

        intList = new ArrayList<>();

        calendarView = Calendar.getInstance(Locale.getDefault());
        // dataView = (TextView) findViewById(R.id.date_view);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        compactCalendarView.setIsRtl(true);
        loadEvents();
        loadEventsForYear(2017);
        logEventsByMonth(compactCalendarView);
        compactCalendarView.invalidate();
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                btnShow.setVisibility(VISIBLE);
                //  dataView.setText(dateFormatForMonth.format(dateClicked));
                getSupportActionBar().setTitle(dateFormatForMonth.format(dateClicked));
                ccurrentDate = dateFormatForDisplaying.format(dateClicked);
                SimpleDateFormat dayintheweek = new SimpleDateFormat("EEEE", Locale.CANADA);
                String dayOfTheWeek = dayintheweek.format(dateClicked);
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                if (bookingsFromMap != null) {
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    AdapterTime = new TimeInDayadapter(tTimeList, DateActivity.this, mutableBookings, ccurrentDate.substring(0, 10),modelProduct);
                    recyclerView.setAdapter(AdapterTime);
                    AdapterTime.notifyDataSetChanged();
                }
                currentdateView.setText(" المواعيد المتاحه " + ccurrentDate.substring(0, 10) + " " + dayOfTheWeek + " ");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //      dataView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                Date dd = new Date();

                getSupportActionBar().setTitle(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });


        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();


            }
        });
        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });
        final View.OnClickListener exposeCalendarListener = getCalendarExposeLis();
        btnShow.setOnClickListener(exposeCalendarListener);

        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });
    }

    @NonNull
    private View.OnClickListener getCalendarExposeLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendarWithAnimation();
                        btnShow.setText("مشاهدة جميع المواعيد ");
                    } else {
                        compactCalendarView.hideCalendarWithAnimation();
                        btnShow.setText("مشاهدة التقويم");
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    private void loadEvents() {
        addEvents(-1, -1);
    }

    private void loadEventsForYear(int year) {

    }

    private void logEventsByMonth(CompactCalendarView compactCalendarView) {
        calendarView.setTime(new Date());
        calendarView.set(Calendar.DAY_OF_MONTH, 1);
        calendarView.set(Calendar.MONTH, Calendar.AUGUST);
        List<String> dates = new ArrayList<>();
        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
        }
    }

    private void addEvents(final int month, final int year) {
        calendarView.setTime(new Date());
        calendarView.set(Calendar.DAY_OF_MONTH, 1);
        final Date firstDayOfMonth = calendarView.getTime();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectDate selectDate = dataSnapshot.getValue(SelectDate.class);

                calendarView.setTime(firstDayOfMonth);
                if (month > -1) {
                    calendarView.set(Calendar.MONTH, month);
                }
                if (year > -1) {
                    calendarView.set(Calendar.ERA, GregorianCalendar.AD);
                    calendarView.set(Calendar.YEAR, year);
                }
                setToMidnight(calendarView);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
                Date date;
                long mill = 0;
                try {
                    date = simpleDateFormat.parse(selectDate.getDay());
                    mill = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendarView.add(Calendar.DATE, i);
                if (!dayDateList.contains(selectDate.getDay())) {
                    dayDateList.add(selectDate.getDay());
                    getEvents(mill, i, selectDate.getDay());

                    i++;
                }
                y++;


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase3.addChildEventListener(childEventListener);

    }

    private List<Event> getEvents(final long timeInMillis, final int day, final String dateDay) {


        Query query = FirebaseDatabase.getInstance().getReference("selectData").orderByChild("day")
                .equalTo(dateDay);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayEvent.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    SelectDate selectDate = d.getValue(SelectDate.class);
                    arrayEvent.add(new Event(Color.argb(255, 169, 68, 65), timeInMillis, selectDate.getTimeInDay()));
                }

                compactCalendarView.addEvents(arrayEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return arrayEvent;
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("currentDate", ccurrentDate);
        Gson gson = new Gson();
        String myJson = gson.toJson(mutableBookings);
        savedInstanceState.putString("listMultibleBook",myJson);
        super.onSaveInstanceState(savedInstanceState);
    }

//onRestoreInstanceState

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        horizontalCurrentDate = savedInstanceState.getString("currentDate");
        String myString2 = savedInstanceState.getString("listMultibleBook");
        Gson converter = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
         HorizontalMultibleBook =  converter.fromJson(myString2, type );
        if (orientation == Configuration.ORIENTATION_LANDSCAPE || manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {

            if (horizontalCurrentDate != null && HorizontalMultibleBook != null) {
                AdapterTime = new TimeInDayadapter(tTimeList, DateActivity.this, HorizontalMultibleBook, horizontalCurrentDate.substring(0, 10),modelProduct);
                recyclerView.setAdapter(AdapterTime);
                AdapterTime.notifyDataSetChanged();
            }
        }

    }


}







