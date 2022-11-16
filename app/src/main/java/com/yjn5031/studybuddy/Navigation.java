package com.yjn5031.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenuButton:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
       LoginedUser.Logout();
       startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //this line hide action bar
        getSupportActionBar().setTitle("To Do");

        String TAG = "TODOFragment";


        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.bodyContainer, new TodoFragment(), TAG).commit();
        navigationView.setSelectedItemId(R.id.nav_todo);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String TAG = "TODOFragment";
                switch (item.getItemId()){
                    case R.id.nav_todo:
                        fragment = new TodoFragment();
                        getSupportActionBar().setTitle("To Do");
                        TAG = "TODOFragment";
                        break;

                    case R.id.nav_coffee:
                        fragment = new CoffeeFragment();
                        getSupportActionBar().setTitle("Coffee Finder");
                        TAG = "COFFEEFINDERFragment";
                        break;

                    case R.id.nav_upcoming_events:
                        fragment = new EventFragment();
                        getSupportActionBar().setTitle("Event Tracker");
                        TAG = "EVENTTRACKERFragment";
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.bodyContainer, fragment, TAG).commit();

                return true;
            }
        });
    }
}