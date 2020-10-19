package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

import static com.example.yasmina.R.id.navigationView;
import static com.example.yasmina.R.id.start;
import static java.security.AccessController.getContext;

public class book extends AppCompatActivity  {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    androidx.appcompat.widget.Toolbar topAppBar;
    RelativeLayout container;
    FragmentManager mFragmentManager;
    HairColorFragment hairColorFragment;
    MakeUpFragment makeUpFragment;
    HairFragment HairFragment;
    CheckoutFragment checkoutFragment;
    BottomNavigationView bottomNavigationView ;
    DrawerLayout drawerLayout;
    ProgressBar progressBar;
    boolean show=false;
    ArrayAdapter<String>adapter;
    ArrayList<String> list;
    TextView textCartItemCount;
    int mCartItemCount;
    Controller controller;
    NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";
    final static String TAG_3 = "FRAGMENT_3";
    final static String KEY_MSG_1 = "FRAGMENT1_MSG";
    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    final static String KEY_MSG_3 = "FRAGMENT3_MSG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Paper.init(this);
        List<ModelProduct> mlist = Paper.book().read("modelproductList");
        Controller mcontroller = (Controller) getApplication();
        if (mlist != null) {
            for (ModelProduct m : mlist) {
                mcontroller.setProducts(m);
            }
        }

        controller=(Controller)getApplicationContext();
        topAppBar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.shopping_card_id){
                  //  Toast.makeText(getApplication(),"asasa",Toast.LENGTH_LONG).show();
                }
                return false;

            }
        });

         mCartItemCount=controller.AllProduct().size();
        setupBadge();

        InternetConnection internetConnection=new InternetConnection();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

           /* String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);*/
        }

        list=new ArrayList<>();
        adapter=new ArrayAdapter(this,R.layout.list_item1,list);
          progressBar=(ProgressBar)findViewById(R.id.prograss_bar);

           ActionBar actionbar = getSupportActionBar();
           actionbar.setDisplayHomeAsUpEnabled(true);
           configureNavigationDrawer();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("others");
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Content content=dataSnapshot.getValue(Content.class);
                list.add(content.getText());
                adapter.notifyDataSetChanged();
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
        databaseReference.addChildEventListener(childEventListener);
        if (firebaseUser !=null)
        {
            topAppBar.setTitle("Hi"+firebaseUser.getDisplayName());
        }

        bottomNavigationView=(BottomNavigationView)findViewById(navigationView);
        container=(RelativeLayout) findViewById(R.id.mainContainer);
         mFragmentManager = getSupportFragmentManager();
        hairColorFragment = new HairColorFragment();
        makeUpFragment = new MakeUpFragment();
        checkoutFragment=new CheckoutFragment();
        HairFragment = new HairFragment();
        DisconnectedFragment disconnectedFragment=new DisconnectedFragment();
        if( ! internetConnection.isConnected(this))
        {
            OfflineNetwork offlineNetwork=new OfflineNetwork();
            offlineNetwork.alertShowFunction(this);
            progressBar.setVisibility(View.INVISIBLE);
        }
         if (savedInstanceState == null) {
                //if's the first time created

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainContainer, makeUpFragment);
                fragmentTransaction.commit();
            }
            //  topAppBar.inflateMenu(R.menu.main_menu);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    progressBar.setVisibility(View.VISIBLE);
                    switch (item.getItemId()) {
                        case R.id.make_up:
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            fragmentTransaction.replace(R.id.mainContainer, makeUpFragment);
                            fragmentTransaction.commit();
                            progressBar.setVisibility(View.INVISIBLE);
                            return true;
                        case R.id.hair:
                            fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            fragmentTransaction.replace(R.id.mainContainer, HairFragment);
                            fragmentTransaction.commit();
                            progressBar.setVisibility(View.INVISIBLE);
                            return true;
                        case R.id.other:
                            fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            fragmentTransaction.replace(R.id.mainContainer, HairFragment);
                            fragmentTransaction.commit();
                            progressBar.setVisibility(View.INVISIBLE);
                            return true;
                    }
                    return false;
                }
            });
    }
   @SuppressLint("ResourceAsColor")
        private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                progressBar.setVisibility(View.VISIBLE);
                 int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_my_rewards) {
                    show=!show;
                    Intent intent=new Intent(book.this,LoginActivity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);

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
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(
            new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                ArrayList<Content> filteredData = new ArrayList<Content>();
                Log.d("myApp", query);
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                if (adapter !=null) {
                    Log.d("myApp", newText + list.toString());
                }
                else
                    Log.d("myApp", "null");
                return false;
            }
        });
        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.shopping_card_id);
        View actionView = item.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
                Intent intent1=new Intent(getApplication(),checkOut.class);
                startActivity(intent1);
            }
        });
         return true;
    }

    public void setupBadge() {
        if (textCartItemCount != null) {
            if (controller.AllProduct().size() == 0) {
                    textCartItemCount.setVisibility(View.GONE);
            } else {
                textCartItemCount.setText(String.valueOf(controller.AllProduct().size()));
                    textCartItemCount.setVisibility(View.VISIBLE);
            }
        }
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
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fragmentTransaction.replace(R.id.mainContainer, hairColorFragment);
                fragmentTransaction.commit();
                progressBar.setVisibility(View.INVISIBLE);
                return  true;
            case R.id.shopping_card_id:
                Toast.makeText(this,"asasa",Toast.LENGTH_LONG).show();
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
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    @Override
    public void onBackPressed() {
        setupBadge();
        super.onBackPressed();
    }

}


