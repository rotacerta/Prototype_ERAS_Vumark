package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;
import com.vuforia.UI.R;

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

        btn_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToActivity(v, ListProducts.class);
                finish();
            }
        });
    }
}
