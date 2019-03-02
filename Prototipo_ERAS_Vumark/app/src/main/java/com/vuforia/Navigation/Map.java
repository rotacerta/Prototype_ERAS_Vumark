package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Window;
import android.view.WindowManager;

import com.vuforia.UI.R;
import com.vuforia.UI.ActivitySplashScreen;

public class Map  extends Navigate
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.map);
        setOnClick();
    }

    public void setOnClick() {
        FloatingActionButton btn_list = findViewById(R.id.FbtnBottomList);
        setOnClickInFloatingButton(btn_list, ListProducts.class);

        FloatingActionButton btn_camera = findViewById(R.id.FbtnBottomCam);
        setOnClickInFloatingButton(btn_camera, ActivitySplashScreen.class);
    }

}

