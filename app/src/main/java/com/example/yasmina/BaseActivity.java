package com.example.yasmina;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import io.paperdb.Paper;

public class BaseActivity extends AppCompatActivity {
    boolean show=false;
    DrawerLayout drawerLayout;
    int drawerLayoutID;
    int navigationViewID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void initToolbar(int toolbarId,int mdrawerLayout,int mNavigationView) {

        Toolbar myToolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(myToolbar);
        drawerLayoutID=mdrawerLayout;
        navigationViewID=mNavigationView;
        configureNavigationDrawer();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home :
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                return  true;
            case R.id.shopping_card_id:
                Intent intent1=new Intent(this,checkOut.class);
                startActivity(intent1);
                return true;
            case R.id.more:
                Intent intent11=new Intent(this,checkOut.class);
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

                        ArrayList<Content> filteredData = new ArrayList<Content>();
                        Log.d("myApp", query);

                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        return true;

                    }
                });

        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("ResourceAsColor")
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(drawerLayoutID);
        NavigationView navView = (NavigationView) findViewById(navigationViewID);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.shopping_card_id) {
                    show=!show;
                    Intent intent=new Intent(BaseActivity.this,LoginActivity.class);
                    startActivity(intent);

                } else if (itemId == R.id.nav_my_rewards) {
                    Controller controller=(Controller)getApplication();
                    controller.AllProduct().clear();
                    Paper.book().delete("modelproductList");
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    //  System.exit(0);
                    startActivity(getIntent());

                }
                if ( show) {
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

}
