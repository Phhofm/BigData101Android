package com.bigdata101.bigdata101;

import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BigData101Fragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayAdapter stringAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.abs_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);

        List<String> drawerMenuesList = new ArrayList<String>();
        drawerMenuesList.add("Home");
        drawerMenuesList.add("Introduction");
        drawerMenuesList.add("Technology news");
        drawerMenuesList.add("Law news");

        stringAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerMenuesList);
        drawerList.setAdapter(stringAdaptor);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = adapterView.getItemAtPosition(i).toString();
                if (selectedFromList.equals("Introduction")){
                    Log.d("clicked", "introduction");
                    getSupportFragmentManager()
                            .beginTransaction().add(R.id.fragment_container, new BigData101Fragment()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(R.layout.drawer_layout)) {
            drawerLayout.closeDrawer(R.layout.drawer_layout);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
