package com.example.richard.bookrian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

//The navigation menu is retrieved and modified from http://robinsonprogramming.com/tuts/download.php?page=downloads_android_studio_$_NavigationDrawer.zip

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String m_sEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Refer to drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //create navigation view fir side menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //go to home page on initiated
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home_layout));
        navigationView.setNavigationItemSelectedListener(this);

        //SharePreference
        SharedPreferences pref = getSharedPreferences("userName", MODE_PRIVATE);
        String restoredText = pref.getString("text", "No input");
        if (restoredText != null) {
            m_sEmail = pref.getString("email", "No name defined");//"No name defined" is the default value.
        }

        //Update textView
        View v = navigationView.getHeaderView(0);
        TextView l_user = (TextView ) v.findViewById(R.id.l_userNav);
        l_user.setText(m_sEmail);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainActivity()).commit();
        }

        else if (id == R.id.nav_book_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new BookScrollActivity()).commit();
        }
        else if (id == R.id.nav_account_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AccountActivity()).commit();
        }
        else if (id == R.id.nav_contact_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ContactsActivity()).commit();
        }
        else if (id == R.id.nav_lib_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new LibRecyclerActivity()).commit();
        }
        else if (id == R.id.nav_reference) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ReferenceActivity()).commit();

        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();

            Intent newIntent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
            startActivity(newIntent);


        }
        //Refer to drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Set it to closed on start.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

