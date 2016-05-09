package com.neucrack.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.neucrack.communication.ToServer;
import com.neucrack.entity.PreferenceData;
import com.neucrack.entity.User;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView mNavigationView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id._home_fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageView userHeadImage = (ImageView) findViewById(R.id._userImage);
        Button room1 = (Button)findViewById(R.id._room1);
        Button room2 = (Button)findViewById(R.id._room2);
        Button room3 = (Button)findViewById(R.id._room3);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mNavigationView.setNavigationItemSelectedListener(this);


        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("deviceName","1:2:3:4:5:6");
                Intent intent = new Intent(Home.this, subDevices.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("deviceName","1:2:3:4:5:7");
                Intent intent = new Intent(Home.this, subDevices.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("deviceName","1:2:3:4:5:8");
                Intent intent = new Intent(Home.this, subDevices.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        Init();


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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id._nav_user) {
            // Handle the camera action
            Intent intent = new Intent(Home.this,signIn.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void Init(){
        ToServer toServer = new ToServer();
        User user = PreferenceData.GetUserInfo();
        if(user!=null) {
            if (!toServer.SignIn(user)) {
                Toast.makeText(getApplicationContext(), "无法登录，请检查网络", Toast.LENGTH_SHORT).show();
                PreferenceData.mIsSignedIn = false;
            }
            else
                PreferenceData.mIsSignedIn = true;
        }
    }
}
