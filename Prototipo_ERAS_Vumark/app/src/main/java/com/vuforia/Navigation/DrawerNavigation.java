package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.vuforia.UI.R;
import com.vuforia.VuMark.VuMark;

public class DrawerNavigation extends Navigate {
    protected DrawerLayout mDrawer;
    protected NavigationView navDrawer;
    protected View currentView;

    protected void initializeDrawer() {
        currentView = getWindow().getDecorView().getRootView();
        setupDrawerContent(navDrawer);
    }

    protected void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
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
