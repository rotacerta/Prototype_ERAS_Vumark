package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Window;
import android.view.WindowManager;

import com.levitnudi.legacytableview.LegacyTableView;
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
        createTable();
    }

    public void createTable() {
        //set table title labels
        LegacyTableView.insertLegacyTitle("Id", "Name", "Age", "Email");
        //set table contents as string arrays
        LegacyTableView.insertLegacyContent("2999010", "John Deer", "50", "john@example.com",
                "332312", "Kennedy F", "33", "ken@example.com"
                ,"42343243", "Java Lover", "28", "Jlover@example.com"
                ,"4288383", "Mike Tee", "22", "miket@example.com");

        LegacyTableView legacyTableView = (LegacyTableView)findViewById(R.id.legacy_table_view);
        legacyTableView.setTitle(LegacyTableView.readLegacyTitle());
        legacyTableView.setContent(LegacyTableView.readLegacyContent());

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        legacyTableView.setTablePadding(7);

        //to enable users to zoom in and out:
        legacyTableView.setZoomEnabled(true);
        legacyTableView.setShowZoomControls(true);

        //remember to build your table as the last step
        legacyTableView.build();
    }

    public void setOnClick() {
        FloatingActionButton btn_map = findViewById(R.id.FbtnBottomMap);
        setOnClickInFloatingButton(btn_map, Map.class);

        FloatingActionButton btn_camera = findViewById(R.id.FbtnBottomCam);
        setOnClickInFloatingButton(btn_camera, ActivitySplashScreen.class);
    }

}
