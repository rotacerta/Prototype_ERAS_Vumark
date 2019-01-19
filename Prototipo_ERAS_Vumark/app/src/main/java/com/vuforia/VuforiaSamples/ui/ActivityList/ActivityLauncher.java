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
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.LineMoveIndicator;
import com.vuforia.Navigation.Adapters.PagerAdapter;
import com.vuforia.Navigation.MenuNavigationActvity;
import com.vuforia.VuforiaSamples.R;


/**
 * This class will display the Vuforia features list and start the selected Vuforia activity
  */

public class ActivityLauncher extends ListActivity
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

        MenuNavigationActvity menuNavigationActvity = new MenuNavigationActvity();

        hideVumarkApp();
    }

    public void hideVumarkApp() {
        Intent intent = new Intent(this, AboutScreen.class);
        intent.putExtra("ACTIVITY_TO_LAUNCH",
                "app.VuMark.VuMark");
        intent.putExtra("ABOUT_TEXT", "VuMark/VM_about.html");
        startActivity(intent);
    }

}
