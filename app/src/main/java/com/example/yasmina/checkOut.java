package com.example.yasmina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.paperdb.Paper;

public class checkOut extends AppCompatActivity {
    Controller controller;
    RecyclerView recyclerView;
    AdapterCheckOut adapterCheckOut;
    CoordinatorLayout coordinatorLayout;
    FirebaseUser firebaseUser;
    Button bt;
    Controller globalControl;
    String Uid = "";
    TextView totalPriceTextView,numOfHrTextView,numOfItemTextView;
    int sumTotalPrice=0;
    int sumNumOFHr=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        bt = (Button) findViewById(R.id.pushDatabase);
        globalControl = (Controller) getApplication();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordianteLayout);
        controller = (Controller) getApplication();
        recyclerView = (RecyclerView) findViewById(R.id.itemInCheckOutRecycleView);


        totalPriceTextView=(TextView)findViewById(R.id.mTotalPrice);
        numOfHrTextView=(TextView)findViewById(R.id.MnumOfHours);
        numOfItemTextView=(TextView)findViewById(R.id.MnumItemCheckOut);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toast.makeText(this, controller.AllProduct().toString(), Toast.LENGTH_SHORT).show();
        adapterCheckOut = new AdapterCheckOut(controller.AllProduct(), this);
        recyclerView.setAdapter(adapterCheckOut);

        SwapToDeleteItem swapToDeleteItem = new SwapToDeleteItem(this, adapterCheckOut, recyclerView, coordinatorLayout);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swapToDeleteItem);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        calculatePriceCheckOut();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnection internetConnection=new InternetConnection();
                if (! internetConnection.isConnected(getApplication()))
                {
                   OfflineNetwork offlineNetwork=new OfflineNetwork();
                   offlineNetwork.alertShowFunction(getApplication());
                   return;
                }

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Uid = firebaseUser.getUid();
                    globalControl = (Controller) getApplication();
                    for (ModelProduct mModelProduct : globalControl.AllProduct()) {
                        SelectDate selectDate = new SelectDate(Uid, mModelProduct.getSelectDate(), mModelProduct.getTime());
                        FirebaseDatabase.getInstance().getReference("selectData").push().setValue(selectDate);
                        ModelProduct modelProduct = new ModelProduct(mModelProduct.getTextProduct(), mModelProduct.getSubTextProduct(),
                                mModelProduct.getPriceProduct(), mModelProduct.getDurationProduct(), mModelProduct.getTimeWithOwner());
                        FirebaseDatabase.getInstance().getReference("cart").child(Uid).push().setValue(modelProduct);

                    }

                    globalControl.AllProduct().clear();
                    Paper.book().delete("modelproductList");
                    adapterCheckOut = new AdapterCheckOut(controller.AllProduct(), getApplication());
                    recyclerView.setAdapter(adapterCheckOut);
                } else {
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    intent.putExtra("checkOut","checkOut");
                    startActivity(intent);
                    return;
                }
            }
        });

    }

   public void calculatePriceCheckOut() {
    // LastPrice lastPrice=adapterCheckOut.calculatePrice();
       Controller controller=(Controller)this.getApplicationContext();
      totalPriceTextView.setText(String.valueOf(controller.getTotalSum().getSumTotalPrice()));
   //   numOfHrTextView.setText(String.valueOf(lastPrice.getSumNumOFHr()));
   //   numOfItemTextView.setText(String.valueOf(lastPrice.getNumOfItem()));
    }

    public void upCheck(View view) {

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
