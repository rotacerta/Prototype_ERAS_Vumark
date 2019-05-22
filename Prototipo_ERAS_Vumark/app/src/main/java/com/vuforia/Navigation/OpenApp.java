package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.sql.Time;
import java.util.ArrayList;

import com.vuforia.Models.List;
import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.Services.PathFinderService;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;
import com.vuforia.Util.Tuple;
import com.vuforia.VuMark.VuMark;

public class OpenApp extends Navigate {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.open_view);

        PathFinderService pfs = new PathFinderService(new Tuple<Integer, Integer>(0, 0), new ArrayList<Tuple<Integer, Integer>>());
        List l = new List(0, "MockList", new Time(0,0,0), new ArrayList<Product>());
        Data.Init(pfs, l, new ArrayList<Location>());
        startApp();
    }

    private void startApp() {
        Button btn_open = findViewById(R.id.btn_open);
        setOnClickInButton(btn_open, VuMark.class);
    }
}
