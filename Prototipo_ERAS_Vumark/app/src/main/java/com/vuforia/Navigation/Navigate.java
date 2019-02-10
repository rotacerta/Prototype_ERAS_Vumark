package com.vuforia.Navigation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Navigate extends AppCompatActivity {

    public void setOnClickInFloatingButton(FloatingActionButton button, final Class classSelected) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToActivity(v, classSelected);
            }
        });
    }

    public void goToActivity(View view, Class classSelected) {
        Intent intent = new Intent( view.getContext(), classSelected );
        startActivityForResult( intent, 0);
    }
}
