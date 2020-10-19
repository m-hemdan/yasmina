package com.example.yasmina;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class TimeInDayadapter extends RecyclerView.Adapter<TimeInDayadapter.ViewHolder> {
    int counter=0;
    List<TimeClass> mTimeList;
    Context mContext;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    List<String> mCurrentDate;
    String mSelectDate;
    String [] dateArr;
    Controller globalControl;
    ModelProduct mModelProduct;


    public TimeInDayadapter(List<TimeClass> timeList, Context context) {
        mTimeList = timeList;
        mContext = context;


    }

    public TimeInDayadapter(List<TimeClass> timeList, Context context, List<String> currentDate,String selectedDate,ModelProduct modelProduct) {
        mTimeList = timeList;
        mContext = context;
        mCurrentDate=currentDate;
       mSelectDate=selectedDate;
       mModelProduct=modelProduct;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_time, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        Button z=holder.addTimeText;
        final TimeClass timeClass = mTimeList.get(position);
        holder.textTime.setText(timeClass.getmTime() );
        holder.clockImg.setImageResource(timeClass.getmImgSrc());
            if (mCurrentDate.contains(timeClass.getmTime())) {
                    int i= countOfTime( mCurrentDate,timeClass.getmTime());
                  if(i>1) {
                      holder.textTime.setPaintFlags(holder.textTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                      // And to undo it: paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                      holder.addTimeText.setText(" محجوز ");
                      holder.addTimeText.setBackgroundColor(Color.GRAY);
                      holder.addTimeText.setEnabled(false);
                      Toast.makeText(mContext, mCurrentDate.toString(), Toast.LENGTH_SHORT).show();
                  }
                  else {
                      changeButon(z,timeClass.getmTime());
                  }
            }
             else {
                changeButon(z,timeClass.getmTime());
             }
    }
    private int countOfTime(List<String> mCurrentDate, String getmTime) {
        counter=0;
        for (String item :mCurrentDate)
        {
            Log.v("itemgetmitem",item+"---" + getmTime);
            if (item.equals(getmTime))
            {
                counter++;
            Log.v("CounterCounter",counter+"");
            }
            else
            Log.v("falseCounter",counter+"");
        }
      return counter;
    }

    public void changeButon(Button x, final String time)
    {
       x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog(time);

            }
        });
    }
    public void alertDialog(final String time)
    {
        FirebaseUser firebaseUser = null;
        String Uid="";

        InternetConnection internetConnection=new InternetConnection();
        if (! internetConnection.isConnected(mContext))
        {
            Toast.makeText(mContext,"sorry i can't",Toast.LENGTH_LONG).show();
            return;
        }
   /*     if(FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            Uid=firebaseUser.getUid();
        }
        else
        {
            Intent intent=new Intent(mContext,LoginActivity.class);
            intent.putExtra("Login","Please login in the first");
            mContext.startActivity(intent);
            return;
        }*/

        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
        boolean checkStr=time.contains("am");
        if (checkStr) {
            dialog.setMessage( "you  choose "+  time + " صباحا");
        }
        else
        {
            dialog.setMessage( "you  choose "+time + " مساء");
        }

        dialog.setTitle("Keep shoping");

        final String finalUid = Uid;
        dialog.setPositiveButton("Check Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                     ModelProduct  ModelProductWithTime=new ModelProduct(mModelProduct.getTextProduct(),mModelProduct.getSubTextProduct(),
                         mModelProduct.getPriceProduct(),mModelProduct.getDurationProduct(),mModelProduct.getDurationProduct(),time,mSelectDate,mModelProduct.getImageHair());
                        globalControl=(Controller)mContext.getApplicationContext();
                        globalControl.setProducts(ModelProductWithTime);
                        Paper.book().write("modelproductList",globalControl.AllProduct());

                        Intent intent =new Intent(mContext,checkOut.class);
                        mContext.startActivity(intent);



                    }
                });
        dialog.setNegativeButton("Keep shopping",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(mContext,book.class);
                mContext.startActivity(intent);

            }
        });
        ((book)mContext).setupBadge();
        BadgeClass badgeClass=new BadgeClass();
        badgeClass.setbadgeConnect(mContext);
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
    @Override

    public int getItemCount() {
        return mTimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTime;
       // Button addTimeText;
        ImageView clockImg;
        AppCompatButton addTimeText;
        RelativeLayout relativeLayoutRegister;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = (TextView) itemView.findViewById(R.id.time_register);
          //  addTimeText = (Button) itemView.findViewById(R.id.btn_Time);
            clockImg=(ImageView)itemView.findViewById(R.id.clock_img);
            addTimeText=(AppCompatButton)itemView.findViewById(R.id.btn_Time);
            relativeLayoutRegister=(RelativeLayout)itemView.findViewById(R.id.relative_layout_register);


        }
    }
}
