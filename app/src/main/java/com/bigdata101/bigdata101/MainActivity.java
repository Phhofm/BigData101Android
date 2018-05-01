package com.bigdata101.bigdata101;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bigdata101.bigdata101.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragmentInteraction {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private View popupView;

    SharedPreferences sharedPreferences;


    private ArrayAdapter stringAdaptor;
    private PopupWindow popupWindow;


    private String techArticlesEndpoint = Constants.TECH_ARTICLES_ENDPOINT;
    private String lawArticlesEndpoint = Constants.LAW_ARTICLES_ENDPOINT;
    private String newsFragmentTag = Constants.NEWS_FRAGMENT_TAG;

    private boolean accepted_legal_disclaimer;

    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        sharedPreferences = getPreferences(MODE_PRIVATE);
        Log.d("legal", Boolean.toString(accepted_legal_disclaimer));
        accepted_legal_disclaimer = sharedPreferences.getBoolean("legal_accepted", false);
        drawerLayout = findViewById(R.id.drawer_layout);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });




        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, WelcomeFragment.newInstance(null, null))
                .commit();










    }

    @Override
    protected void onResume() {
        super.onResume();
        accepted_legal_disclaimer = sharedPreferences.getBoolean("legal_accepted", false);
        makePopup();



    }

    private void makePopup() {
        if(!accepted_legal_disclaimer) {

            findViewById(R.id.content_frame)
            .post(new Runnable() {
                @Override
                public void run() {

                    Log.d("legalrunnable", Boolean.toString(accepted_legal_disclaimer));



                    popupView = getLayoutInflater().inflate(R.layout.popup_view, null);


                    popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.d("dissmissed", "die");
                        }
                    });
                    popupView.findViewById(R.id.accept_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            Log.d("popup", "accpeted");
                            sharedPreferences.edit().putBoolean("legal_accepted", true).commit();
                        }
                    });

                    popupView.findViewById(R.id.decline_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            sharedPreferences.edit().putBoolean("legal_accepted", false).commit();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);

                        }
                    });

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


                }

            });

        }
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
    public void onBackPressed() {
        if(popupWindow != null && popupWindow.isShowing()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    @Override
    public void fragmentInteraction() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void recyclerCallback() {
        Log.d("callbakc", "hi");


        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.NEWS_FRAGMENT_TAG);
        if (fragment != null && fragment.isVisible()) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
                    if(swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ErrorFragment.newInstance(null, null))
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}

