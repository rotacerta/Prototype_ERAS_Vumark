package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;
import com.vuforia.UI.R;
import com.vuforia.VuMark.VuMark;

public class OpenApp extends Navigate {
    @Override
    protected void SetOnClick() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.open_view);

        startApp();
    }

    private void startApp() {
        Button btn_open = findViewById(R.id.btn_open);
        setOnClickInButton(btn_open, VuMark.class);
    }
}
