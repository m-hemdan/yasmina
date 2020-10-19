package com.example.yasmina;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.paperdb.Paper;

public class AdapterProtine extends RecyclerView.Adapter<AdapterProtine.ViewHolder>  {
    private List<Content> mList;
    boolean showLayout=false;
    Context mContext;
    OnItemClickListener mlistener;
    LinearLayout linearLayoutFront;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    RecyclerView rr;
    private static final String PREFS_TAG = "SharedPrefs";
    private static final String PRODUCT_TAG = "MyProduct";
    int dPrice = 0;
    int lPrice = 0;
    final String[] lengthHair = new String[1];
    final String[] densityHair = new String[1];
    int totalPrice = 0;
       public AdapterProtine(List<Content> list, Context context, OnItemClickListener listener,RecyclerView r1) {
        mList = list;
        mContext = context;
        mlistener = listener;
        rr=r1;
    }
    public interface OnItemClickListener {
        void onItemClick(Content item);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Content content = mList.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(mList.get(position).getImg()).into(holder.photoimageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.nameTextView.setText(content.getText());
        holder.subTextView.setText(content.getSubtext());
        holder.priceTextView.setText(content.getPrice() + "pound");
        holder.durationTextView.setText(String.valueOf(mList.get(position).getDuration()) + " hr");
        holder.bind(mList.get(position), mlistener);
        holder.horRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.horRecycleView.setLayoutManager(linearLayoutManager);
        AdapterHorBack adapterHorBack = new AdapterHorBack(mList.get(position).getArrImage());
        //tall//
        final String[] hairArr = {"", "short", "medium", "long"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, hairArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerHair.setAdapter(adapter);
        holder.spinnerHair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lengthHair[0] = hairArr[position];
                Toast.makeText(mContext, content.getPrice(), Toast.LENGTH_SHORT).show();
                callPrice(holder.warningTextView, holder.PriceLayout, content.getPrice(), holder.priceAfterCal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //density//
        final String[] densityHairArr = {"", "light", "medium", "heavy"};
        ArrayAdapter<String> adapterDensity = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, densityHairArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerDensity.setAdapter(adapterDensity);
        holder.spinnerDensity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                densityHair[0] = densityHairArr[position];
                Toast.makeText(mContext, content.getPrice(), Toast.LENGTH_SHORT).show();
                callPrice(holder.warningTextView, holder.PriceLayout, content.getPrice(), holder.priceAfterCal);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.AddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (densityHair[0] != "" && lengthHair[0] != "") {
                    String text = mList.get(position).getText();
                    String subText = mList.get(position).getSubtext();

                    int duration = mList.get(position).getDuration();
                    int timeWithOwner =mList.get(position).getTimeInHr();
                    String imgHair=mList.get(position).getImg();
                    final ModelProduct modelProduct = new ModelProduct(text, subText, String.valueOf(totalPrice), duration,timeWithOwner,imgHair);
                           InternetConnection internetConnection=new InternetConnection();
                            if (internetConnection.isConnected(mContext)) {
                               Intent intent = new Intent(mContext, DateActivity.class);
                                Gson gson = new Gson();
                                String myJson = gson.toJson(modelProduct);
                                intent.putExtra("myjson", myJson);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              mContext.getApplicationContext().startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(mContext,"Sorry i can't register ",Toast.LENGTH_LONG).show();
                            }
                        }
                else {
                    holder.warningTextView.setVisibility(View.VISIBLE);
                }

                }
            });



        holder.horRecycleView.setAdapter(adapterHorBack);
    }

    public void callPrice(TextView warnTextView, RelativeLayout mPriceRelative, String price, TextView priceTextView) {
         totalPrice = 0;
        String[] priceArr = price.split("-");
        int constP = Integer.parseInt(priceArr[0]) / 5;
        if (densityHair[0] == "" || lengthHair[0] == "") {
            mPriceRelative.setVisibility(View.GONE);
             warnTextView.setVisibility(View.VISIBLE);
            return;
        } else if (densityHair[0] != "" && lengthHair[0] != "") {
            warnTextView.setVisibility(View.GONE);
            switch (densityHair[0]) {
                case "light":
                    dPrice = 1;
                    break;
                case "medium":
                    dPrice = 2;
                    break;
                case "heavy":
                    dPrice = 3;
                    break;

            }
            switch (lengthHair[0]) {
                case "short":
                    lPrice = 1;
                    break;
                case "medium":
                    lPrice = 2;
                    break;
                case "long":
                    lPrice = 3;
                    break;
            }
            if ((dPrice + lPrice) == 2) {
                totalPrice = Integer.parseInt(priceArr[0]);
            } else if ((dPrice + lPrice) == 3) {
                totalPrice = Integer.parseInt(priceArr[0]) + constP;
            } else if ((dPrice + lPrice) == 4) {
                totalPrice = (Integer.parseInt(priceArr[1]) + Integer.parseInt(priceArr[0])) / 2;

            } else if (dPrice + lPrice == 5) {
                totalPrice = (Integer.parseInt(priceArr[1]) + Integer.parseInt(priceArr[0])) / 2 + constP;
            } else if ((dPrice + lPrice) == 6) {
                totalPrice = Integer.parseInt(priceArr[1]);
            }
            mPriceRelative.setVisibility(View.VISIBLE);
            priceTextView.setVisibility(View.VISIBLE);
            priceTextView.setText(String.valueOf(totalPrice));

        }


    }

    public void notifyData(List<Content> myList) {
        Log.d("notifyData ", myList.size() + "");
        mList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
           ProgressBar progressBar;
        ImageView photoimageView, selectDenLenImg;
        Button detailsBtn;
        TextView nameTextView, subTextView, durationTextView, priceTextView, warningTextView, priceAfterCal;
        RecyclerView horRecycleView;
        Button AddToCard;
        Spinner spinnerHair;
        Spinner spinnerDensity;
        RelativeLayout PriceLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressbar_image);
            warningTextView = (TextView) itemView.findViewById(R.id.warning_one);
            photoimageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text);
            subTextView = (TextView) itemView.findViewById(R.id.sub_name_text);
            durationTextView = (TextView) itemView.findViewById(R.id.duration_text);
            priceTextView = (TextView) itemView.findViewById(R.id.price_text);
            horRecycleView = (RecyclerView) itemView.findViewById(R.id.hor_recycleView);
            AddToCard = (Button) itemView.findViewById(R.id.addToCard);
            priceAfterCal = (TextView) itemView.findViewById(R.id.priceAfterCal);
            PriceLayout = (RelativeLayout) itemView.findViewById(R.id.priceCalLayout);
            /*horRecycleView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
            horRecycleView.setLayoutManager(linearLayoutManager);*/
            String[] tallHairArr = {"short", "medium", "long", "very long"};
            spinnerHair = (Spinner) itemView.findViewById(R.id.tall_hair_spinner);
            spinnerDensity = (Spinner) itemView.findViewById(R.id.density_spinner);
        }
        public void bind(final Content content, final OnItemClickListener mlistener) {
            detailsBtn = (Button) itemView.findViewById(R.id.details_show);
            linearLayoutFront = (LinearLayout) itemView.findViewById(R.id.linear_Front);
            selectDenLenImg = (ImageView) itemView.findViewById(R.id.select_Length_density_img);
            final LinearLayout linearLayoutBack = (LinearLayout) itemView.findViewById(R.id.linearLauoutBack);
            final ValueAnimator mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
            mFlipAnimator.addUpdateListener(new FlipListener(linearLayoutFront, linearLayoutBack));

            selectDenLenImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder denLenAlertDialog = new AlertDialog.Builder(mContext);
                    denLenAlertDialog.setTitle("Select length and density");
                    LayoutInflater factory = LayoutInflater.from(mContext);
                    final View view = factory.inflate(R.layout.select_len_den_layout, null);
                    denLenAlertDialog.setView(view);
                    denLenAlertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    denLenAlertDialog.show();
                }
            });
            detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLayout=true;
                    mFlipAnimator.start();
                }
            });
            Button button = (Button) itemView.findViewById(R.id.back);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlipAnimator.reverse();
                    showLayout=false;
                }
            });

           }

    }
}