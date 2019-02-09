/*===============================================================================
Copyright (c) 2016-2018 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.vuforia.VuforiaSamples.ui.ActivityList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.vuforia.Navigation.ListProducts;
import com.vuforia.Navigation.Map;
import com.vuforia.Navigation.Navigate;
import com.vuforia.VuforiaSamples.R;
import com.vuforia.VuforiaSamples.app.VuMark.VuMark;


/**
 * This class will display the Vuforia features list and start the selected Vuforia activity
  */

public class ActivityLauncher extends AppCompatActivity
{

    private String DEBUG_TAG = "TESTE";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.camera_overlay_reticle);

        hideVumarkApp();
        setOnClick();
    }

    public void hideVumarkApp( ) {
        Log.d(DEBUG_TAG, "hideVumarkApp");
        Intent intent = new Intent(this, VuMark.class);
        startActivityForResult(intent, 0);
    }

    public void setOnClick() {
        RelativeLayout btn_map = (RelativeLayout) findViewById(R.id.map_camera);
        RelativeLayout btn_list = (RelativeLayout) findViewById(R.id.list_camera);


        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Onclick");
                Intent intent = new Intent( v.getContext(), Map.class );
                startActivityForResult( intent, 0);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Onclick");
                Intent intent = new Intent( v.getContext(), ListProducts.class );
                startActivityForResult( intent, 0);
            }
        });
    }

}
