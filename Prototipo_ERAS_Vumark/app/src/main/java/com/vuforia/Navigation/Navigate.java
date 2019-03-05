package com.vuforia.Navigation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.vuforia.VuMark.VuMark;

public abstract class Navigate extends AppCompatActivity {

    protected abstract void SetOnClick();

    public void setOnClickInFloatingButton(FloatingActionButton button, final Class classSelected) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToActivity(v, classSelected);
                finish();
            }
        });
    }

    public void goToActivity(View view, Class classSelected) {
        Intent intent = new Intent( view.getContext(), classSelected );
        startActivityForResult( intent, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!VuMark.class.getName().equals(event.getClass().getName())){
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                startActivity(new Intent(this, VuMark.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
