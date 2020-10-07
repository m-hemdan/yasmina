package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    private FrameLayout frameLayout;
    public static boolean setSignUpFragment = false;
    private int currentFragment = -1;
    private NavigationView navigationView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        List<ModelProduct> list = Paper.book().read("modelproductList");
          androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        frameLayout = findViewById(R.id.main_framelayout);
        if (showCart) {
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MakeUpFragment(), -2);
        } else {

            setFragment(new HairColorFragment(), HOME_FRAGMENT);
        }
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }
    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            //todo:search
            return true;
        } else if (id == R.id.more) {
            //todo:notifications
            return true;
        } else if (id == R.id.shopping_card_id) {
            final Dialog signInDialog = new Dialog(MainActivity.this);
            signInDialog.setContentView(R.layout.sign_in_dialog);
            signInDialog.setCancelable(true);
            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button dialogSignInBtn = signInDialog.findViewById(R.id.dialog_sign_in_btn);
            Button dialogSignUpBtn = signInDialog.findViewById(R.id.dialog_sign_up_btn);
            final Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
            dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInDialog.dismiss();
                    setSignUpFragment =false;
                    startActivity(registerIntent);
                }
            });
            dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInDialog.dismiss();
                    setSignUpFragment =true;
                    startActivity(registerIntent);
                }
            });
            signInDialog.show();
            //gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                showCart = false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my_Home) {

            invalidateOptionsMenu();
            setFragment(new HairColorFragment(), HOME_FRAGMENT);
        } else if (id == R.id.nav_my_orders) {
            gotoFragment("My Orders", new MakeUpFragment(), ORDERS_FRAGMENT);
        } else if (id == R.id.nav_my_rewards) {
            gotoFragment("My Rewards", new MakeUpFragment(), REWARDS_FRAGMENT);
        } else if (id == R.id.nav_my_cart) {
            gotoFragment("My Cart", new MakeUpFragment(), CART_FRAGMENT);

        } else if (id == R.id.nav_my_wishlist) {
            gotoFragment("My Wishlist", new MakeUpFragment(), WISHLIST_FRAGMENT);
        } else if (id == R.id.nav_my_account) {
            gotoFragment("My Account", new MakeUpFragment(), ACCOUNT_FRAGMENT);
        } else if (id == R.id.nav_sign_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {

                    invalidateOptionsMenu();
                    setFragment(new HairColorFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }
}

/* Paper.init(this);
         List<ModelProduct> list = Paper.book().read("modelproductList");
        InternetConnection internetConnection = new InternetConnection();
        if (!internetConnection.isConnected(this)) {
            OfflineNetwork offlineNetwork=new OfflineNetwork();
            offlineNetwork.alertShowFunction(this);
           Intent intent=new Intent(this,SorryNetwork.class);
           startActivity(intent);
        }


        Controller controller = (Controller) getApplication();
        if (list != null) {
            for (ModelProduct m : list) {
                controller.setProducts(m);
            }
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, book.class);
                startActivity(intent);

            }
        });
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, search_test.class);
                startActivity(intent);
            }
        });
    }*/
