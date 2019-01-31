/*===============================================================================
Copyright (c) 2016-2018 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.vuforia.VuforiaSamples.ui.ActivityList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.LineMoveIndicator;
import com.vuforia.Navigation.Adapters.PagerAdapter;
import com.vuforia.VuforiaSamples.R;
import com.vuforia.VuforiaSamples.app.VuMark.VuMark;


/**
 * This class will display the Vuforia features list and start the selected Vuforia activity
  */

public class ActivityLauncher extends AppCompatActivity
{

    private static final String TABS_TITLE[] = {"Câmera", "Lista de produtos"};
    private DachshundTabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        navigationMenu();
        // hideVumarkApp();
    }

    public void navigationMenu() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), TABS_TITLE));

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setAnimatedIndicator(new LineMoveIndicator(tabLayout));
        Log.d(null, "navigationMenu");
    }

    public void hideVumarkApp(View view) {

        Log.d(null, "hideVumarkApp");
        Intent intent = new Intent(this, VuMark.class);
        startActivity(intent);
    }

    public void finishActivity() {
        Log.d(null, "finishActivity");
        finishActivity(0);
    }



}
