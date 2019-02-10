package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Window;
import android.view.WindowManager;

import com.vuforia.VuforiaSamples.R;
import com.vuforia.VuforiaSamples.ui.ActivityList.ActivitySplashScreen;

public class ListProducts extends Navigate
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.list_products);

        setOnClick();
    }

    public void setOnClick() {
        FloatingActionButton btn_map = findViewById(R.id.FbtnBottomMap);
        setOnClickInFloatingButton(btn_map, Map.class);

        FloatingActionButton btn_camera = findViewById(R.id.FbtnBottomCam);
        setOnClickInFloatingButton(btn_camera, ActivitySplashScreen.class);
    }

}
