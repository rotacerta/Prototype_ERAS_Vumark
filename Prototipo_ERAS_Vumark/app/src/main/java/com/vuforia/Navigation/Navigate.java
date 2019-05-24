package com.vuforia.Navigation;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vuforia.UI.R;

import com.vuforia.VuMark.VuMark;

public abstract class Navigate extends AppCompatActivity {

    protected View currentView;
    protected BottomNavigationView bottomNavigation;

    public void setOnClickInButton(Button button, final Class classSelected) {
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

    protected void initializeMenu() {
        currentView = getWindow().getDecorView().getRootView();
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    selectDrawerItem(item);
                    return true;
                }
            }
        );
    }

    protected void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_camera:
                fragmentClass = VuMark.class;
                break;
            case R.id.nav_list:
                fragmentClass = ListProducts.class;
                break;
            case R.id.nav_map:
                fragmentClass = Map.class;
                break;
            default:
                fragmentClass = VuMark.class;
        }

        goToActivity(currentView, fragmentClass);
        finish();
    }

}
