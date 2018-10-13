package com.concessionform.rajmehta.concessionform;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener /*implements NavigationView.OnNavigationItemSelectedListener*/{

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DisplayMetrics metrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);

        nvDrawer = findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        //setupDrawerContent(nvDrawer);

        expListView = findViewById(R.id.lvExp);

        View hView = nvDrawer.getHeaderView(0);
        final TextView emaildisp = hView.findViewById(R.id.emaildisp);
        final TextView sapdisp = hView.findViewById(R.id.sapdisp);

        /*final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    String email = firebaseUser.getEmail();
                    String sapid = firebaseUser.getUid();
                    emaildisp.setText(email);
                    sapdisp.setText(sapid);
                }
            }
        };*/

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

        prepareListData();
        listAdapter = new com.concessionform.rajmehta.concessionform.ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int cp = (int) listAdapter.getGroupId(groupPosition);
                if(cp==1){
                    loadFragment(new ThirdFragment());
                }
                if(cp==2){
                    loadFragment(new FourthFragment());
                }
                if(cp==3){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginPage.class));
                }
                return false;
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),"Collapsed", Toast.LENGTH_LONG).show();
            }
        });



        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int cp = (int) listAdapter.getChildId(groupPosition, childPosition);
                /*Fragment fragment = null;
                Class fragmentClass;

                switch (cp){
                    case 0:
                        fragmentClass = FirstFragment.class;
                        break;
                    case 1:
                        fragmentClass = SecondFragment.class;
                        break;
                    default:
                        fragmentClass = FirstFragment.class;
                }
                try{
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e){
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
                mDrawer.closeDrawers();*/

                if(cp==0){
                    loadFragment(new FirstFragment());
                    //Toast.makeText(getApplicationContext(),"Pahila fragment daba",Toast.LENGTH_LONG).show();
                }
                if(cp==1){
                    loadFragment(new SecondFragment());
                    //Toast.makeText(getApplicationContext(), "Dui fragment daba", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        //Fragment fragment = null;
        /*FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flContent, fragment).commit();*/

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
        DrawerLayout mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Railway Concession");
        listDataHeader.add("Contact Us");
        listDataHeader.add("About");
        listDataHeader.add("Logout");

        List<String> concession = new ArrayList<String>();
        concession.add("Apply for new form");
        concession.add("Check Form Status");
        listDataChild.put(listDataHeader.get(0),concession);
    }

    private ActionBarDrawerToggle setupDrawerToggle(){
        return new ActionBarDrawerToggle(this,mDrawer,R.string.drawer_open,R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /*private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
