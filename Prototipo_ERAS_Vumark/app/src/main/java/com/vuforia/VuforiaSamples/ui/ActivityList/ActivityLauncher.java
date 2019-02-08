/*===============================================================================
Copyright (c) 2016-2018 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.vuforia.VuforiaSamples.ui.ActivityList;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vuforia.Navigation.Map;
import com.vuforia.Navigation.Navigate;
import com.vuforia.VuforiaSamples.R;
import com.vuforia.VuforiaSamples.app.VuMark.VuMark;


/**
 * This class will display the Vuforia features list and start the selected Vuforia activity
  */

public class ActivityLauncher extends AppCompatActivity
{
    Navigate navigate;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        navigate = new Navigate();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideVumarkApp();
        setOnClick();
    }

    public void hideVumarkApp( ) {
        Log.d(null, "hideVumarkApp");
        Intent intent = new Intent(this, VuMark.class);
        startActivityForResult(intent, 0);
    }

    public void setOnClick() {
        FloatingActionButton btn_map = (FloatingActionButton) findViewById(R.id.map_camera);
        FloatingActionButton btn_list = (FloatingActionButton) findViewById(R.id.list_camera);

        btn_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigate.setActivityMap(v);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigate.setActivityList(v);
            }
        });
    }

}
