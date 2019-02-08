package com.vuforia.Navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vuforia.VuforiaSamples.ui.ActivityList.ActivityLauncher;

public class Navigate extends AppCompatActivity {

    public void setActivityMap(View view) {
        Intent intent = new Intent(this, Map.class);
        startActivityForResult(intent, 0);
    }

    public void setActivityList(View view) {
        Intent intent = new Intent(this, ListProducts.class);
        startActivityForResult(intent, 0);
    }

    public void setActivityCamera(View view) {
        Intent intent = new Intent(this, ActivityLauncher.class);
        startActivityForResult(intent, 0);
    }
}
