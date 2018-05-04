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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragmentInteraction {


    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private View popupView;
    private PopupWindow popupWindow;

    private String techArticlesEndpoint = Constants.TECH_ARTICLES_ENDPOINT;
    private String lawArticlesEndpoint = Constants.LAW_ARTICLES_ENDPOINT;
    private String newsFragmentTag = Constants.NEWS_FRAGMENT_TAG;

    private boolean accepted_legal_disclaimer;


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
        accepted_legal_disclaimer = sharedPreferences.getBoolean("legal_accepted", false);
        drawerLayout = findViewById(R.id.drawer_layout);

        // get the listview
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        // preparing list data
        prepareListData();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String selectedFromList =listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);


                if (selectedFromList.equals("Home")) {
                    Log.d("clicked", "home");

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, WelcomeFragment.newInstance(null, null))
                            .addToBackStack(null)
                            .commit();


                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;



                }

                if (selectedFromList.equals("Introduction")){
                    Log.d("clicked", "introduction");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, BigData101Fragment.newInstance(null,null))
                            .addToBackStack(null)
                            .commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                if (selectedFromList.equals("Technology news")){
                    Log.d("clicked", "tech news");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecyclerViewFragment.newInstance(techArticlesEndpoint,null), newsFragmentTag)
                            .addToBackStack("techFragment").
                            commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                if (selectedFromList.equals("Law news")){
                    Log.d("clicked", "tech news");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecyclerViewFragment.newInstance(lawArticlesEndpoint,null), newsFragmentTag)
                            .addToBackStack("lawFragment")
                            .commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, GenericTextFragment.newInstance(selectedFromList,null))
                            .addToBackStack(null)
                            .commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;



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
                            //popupWindow.dismiss();
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
        listDataHeader.add("Home");
        listDataHeader.add("Big data");
        listDataHeader.add("Terms and definitions");
        listDataHeader.add("Scope");
        listDataHeader.add("Key points");
        listDataHeader.add("Consequences and sanctions");
        listDataHeader.add("News");

        List<String> homeSubchapters = Arrays.asList("Home","Introduction");

        // Adding child data$
        List<String> bigdataSubchapters = Arrays.asList("What is big data","What is big data used for",
                "How is big data obtained","How and where is data stored","How is data organized",
                "How is data processed","Problems with the GDPR");


        List<String> termsandefSubchapters = Arrays.asList("Terms and definitions");


        List<String> scopeSubchapters = Arrays.asList("Theory","Examples");


        List<String> keypointsSubchapters = Arrays.asList("GDPR principles","Main principles",
                "Right to erasure","Right to data portability","Privacy by design and default",
                "Processor and controller","Accountability","Security",
                "Data protection impact assessment","Data protection officer");


        List<String> consequencesSubchapters = Arrays.asList("Fines","Reprimands");

        List<String> newsSubchapters = Arrays.asList("Technology news","Law news");


        listDataChild.put(listDataHeader.get(0), homeSubchapters);
        listDataChild.put(listDataHeader.get(1), bigdataSubchapters);
        listDataChild.put(listDataHeader.get(2), termsandefSubchapters);
        listDataChild.put(listDataHeader.get(3), scopeSubchapters);
        listDataChild.put(listDataHeader.get(4), keypointsSubchapters);
        listDataChild.put(listDataHeader.get(5), consequencesSubchapters);
        listDataChild.put(listDataHeader.get(6), newsSubchapters);

    }
}

