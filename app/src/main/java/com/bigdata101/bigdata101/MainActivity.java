package com.bigdata101.bigdata101;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bigdata101.bigdata101.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragmentInteraction {

    private DrawerLayout drawerLayout;
    private ListView drawerList;



    private ArrayAdapter stringAdaptor;
    private PopupWindow popupWindow;


    private String techArticlesEndpoint = Constants.TECH_ARTICLES_ENDPOINT;
    private String lawArticlesEndpoint = Constants.LAW_ARTICLES_ENDPOINT;
    private String newsFragmentTag = Constants.NEWS_FRAGMENT_TAG;

    private boolean accepted_legal_disclaimer = false;

    private
    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        Log.d("legal", Boolean.toString(accepted_legal_disclaimer));
        accepted_legal_disclaimer = sharedPref.getBoolean("legal_accepted", false);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);

        final List<String> drawerMenuesList = new ArrayList<String>();
        drawerMenuesList.add("Home");
        drawerMenuesList.add("Introduction");
        drawerMenuesList.add("Technology news");
        drawerMenuesList.add("Law news");
        drawerMenuesList.add("Chapters");



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, WelcomeFragment.newInstance(null, null))
                .commit();

        stringAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerMenuesList);
        drawerList.setAdapter(stringAdaptor);



        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = adapterView.getItemAtPosition(i).toString();

                if (selectedFromList.equals("Chapters") && !clicked){
                    Log.d("chapterclick", "subchapter");
                    drawerMenuesList.add("Subchapter1");
                    drawerMenuesList.add("Subchapter2");
                    drawerMenuesList.add("Subchapter3");
                    clicked = true;
                    stringAdaptor.notifyDataSetChanged();
                    return;
                }
                if (selectedFromList.equals("Chapters") && clicked){
                    drawerMenuesList.remove("Subchapter1");
                    drawerMenuesList.remove("Subchapter2");
                    drawerMenuesList.remove("Subchapter3");
                    clicked = false;
                    stringAdaptor.notifyDataSetChanged();
                    return;
                }
                if (selectedFromList.equals("Home")) {
                    Log.d("clicked", "home");
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, WelcomeFragment.newInstance(null, null))
                            .commit();


                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (selectedFromList.equals("Introduction")){
                    Log.d("clicked", "introduction");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, BigData101Fragment.newInstance(null,null))
                            .addToBackStack(null)
                            .commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (selectedFromList.equals("Technology news")){
                    Log.d("clicked", "tech news");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecyclerViewFragment.newInstance(techArticlesEndpoint,null), newsFragmentTag)
                            .addToBackStack("techFragment").
                            commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (selectedFromList.equals("Law news")){
                    Log.d("clicked", "tech news");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecyclerViewFragment.newInstance(lawArticlesEndpoint,null), newsFragmentTag)
                            .addToBackStack("lawFragment")
                            .commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (selectedFromList.equals("Subchapter1")){
                    Log.d("clicked", "subchapter1");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, Subchapter1Fragment.newInstance(null,null))
                            .addToBackStack(null)
                            .commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!accepted_legal_disclaimer) {

            findViewById(R.id.content_frame).post(new Runnable() {
                @Override
                public void run() {

                    Log.d("legalrunnable", Boolean.toString(accepted_legal_disclaimer));


                    final View popupView = getLayoutInflater().inflate(R.layout.popup_view, null);

                    popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


                    popupView.findViewById(R.id.accept_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            Log.d("popup", "accpeted");
                            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("legal_accepted", true);
                            editor.apply();
                        }
                    });

                    popupView.findViewById(R.id.decline_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

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
            drawerLayout.closeDrawer(R.id.drawer_layout);
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
    public void recyclerCallback() {

    }
}
