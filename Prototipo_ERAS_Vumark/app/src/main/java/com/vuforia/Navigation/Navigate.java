package com.vuforia.Navigation;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.vuforia.UI.R;

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

    public void setOnClickInButton(Button button, final Class classSelected) {
        this.setOnClickInButton(button, classSelected, true);
    }

    public void setOnClickInButton(Button button, final Class classSelected, final boolean shouldClose) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToActivity(v, classSelected);
                if(shouldClose)
                    finish();
            }
        });
    }

    public void setOnClickInImageView(ImageView button, final Class classSelected) {
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

    public void changeMenu(String page) {
        resetMenu();
        ImageView camera = findViewById(R.id.image_camera);
        switch (page) {
            case "MAP":
                ImageView map = findViewById(R.id.image_map);
                map.setImageResource(R.drawable.ic_map_black_dp);

                camera.setImageResource(R.drawable.ic_camera_alt_white);
                break;
            case "LIST":
                ImageView list = findViewById(R.id.image_list);
                list.setImageResource(R.drawable.ic_list_black_24dp);

                camera.setImageResource(R.drawable.ic_camera_alt_white);
                break;
        }
    }

    public void resetMenu() {
        ImageView map = findViewById(R.id.image_map);
        map.setBackgroundColor(getResources().getColor(R.color.orange));
        map.setImageResource(R.drawable.ic_map_white_dp);

        ImageView camera = findViewById(R.id.image_camera);
        camera.setBackgroundColor(getResources().getColor(R.color.orange));
        camera.setImageResource(R.drawable.ic_camera_alt_white);

        ImageView list = findViewById(R.id.image_list);
        list.setBackgroundColor(getResources().getColor(R.color.orange));
        list.setImageResource(R.drawable.ic_list_white_24dp);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        String currentClass = this.getClass().getName();
        if(!VuMark.class.getName().equals(currentClass) &&
                !NavigationSummary.class.getName().equals(currentClass))
        {
            if(keyCode == KeyEvent.KEYCODE_BACK)
            {
                if(!OpenApp.class.getName().equals(currentClass))
                {
                    startActivity(new Intent(this, VuMark.class));
                    finish();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
