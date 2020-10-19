package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_LONG;

public class checkOut extends AppCompatActivity {
    Controller controller;
    RecyclerView recyclerView;
    AdapterCheckOut adapterCheckOut;
    CoordinatorLayout coordinatorLayout;
    FirebaseUser firebaseUser;
    Button bt;
    Controller globalControl;
    String Uid = "";
    DrawerLayout drawerLayout;
    ProgressBar progressBar;
    boolean show=false;
    TextView totalPriceTextView,numOfHrTextView,numOfItemTextView,warningMsg;
    int sumTotalPrice=0;
    int sumNumOFHr=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
       // bt = (Button) findViewById(R.id.pushDatabase);
        globalControl = (Controller) getApplication();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordianteLayout);
        controller = (Controller) getApplication();
        recyclerView = (RecyclerView) findViewById(R.id.itemInCheckOutRecycleView);
        totalPriceTextView=(TextView)findViewById(R.id.total_price);
        numOfHrTextView=(TextView)findViewById(R.id.Total_Hours_Id);
        numOfItemTextView=(TextView)findViewById(R.id.total_number_of_item);
        warningMsg=(TextView)findViewById(R.id.warning_msg);
        warningMsg.setVisibility(View.INVISIBLE);
        bt=(Button)findViewById(R.id.register_id) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toast.makeText(this, controller.AllProduct().toString(), Toast.LENGTH_SHORT).show();
        adapterCheckOut = new AdapterCheckOut(controller.AllProduct(), this);
        recyclerView.setAdapter(adapterCheckOut);

        SwapToDeleteItem swapToDeleteItem = new SwapToDeleteItem(this, adapterCheckOut, recyclerView, coordinatorLayout);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swapToDeleteItem);
        itemTouchhelper.attachToRecyclerView(recyclerView);

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
                        FirebaseDatabase.getInstance().getReference("cart").child(Uid).push().setValue(modelProduct)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Succsess register",Toast.LENGTH_SHORT).show();
                                        recyclerView.setVisibility(View.INVISIBLE);
                                        warningMsg.setVisibility(View.VISIBLE);
                                    }
                                });
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
        calculatePriceCheckOut();

    }
    @SuppressLint("ResourceAsColor")
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_my_rewards) {
                    show=!show;
                    Intent intent=new Intent(checkOut.this,LoginActivity.class);
                    startActivity(intent);


                } else if (itemId == R.id.nav_my_cart) {
                    Controller controller=(Controller)getApplication();
                    controller.AllProduct().clear();
                    Paper.book().delete("modelproductList");
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    //  System.exit(0);
                    startActivity(getIntent());
                    progressBar.setVisibility(View.INVISIBLE);
                }
                if ( show) {
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                progressBar.setVisibility(View.VISIBLE);
                return  true;
            case R.id.shopping_card_id:
                Intent intent1=new Intent(getApplication(),checkOut.class);
                startActivity(intent1);
                return true;
            case R.id.more:
                Intent intent11=new Intent(this,nnn.class);
                startActivity(intent11);
 /*            FirebaseAuth.getInstance().signOut();
                finish();
                System.exit(0);
*/
           return true;
            default:
                return true;
        }
    }
   public void calculatePriceCheckOut() {
       Controller controller=(Controller)this.getApplicationContext();
       int numH=controller.getTotalSum().getNumOfItem();
       numOfHrTextView.setText(String.valueOf(controller.getTotalSum().getSumNumOFHr())+" Hr ");
       numOfItemTextView.setText(String.valueOf(controller.getTotalSum().getNumOfItem())+ (numH>1?" items ":" item "));
       totalPriceTextView.setText(String.valueOf(controller.getTotalSum().getSumTotalPrice()) + "$");
    }
    public void upCheck(View view) {
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
