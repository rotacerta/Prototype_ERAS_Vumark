package com.vuforia.Navigation;

import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vuforia.VuforiaSamples.R;
import com.vuforia.VuforiaSamples.ui.ActivityList.ActivitySplashScreen;

import androidx.cardview.widget.CardView;


public class ListProducts extends Navigate
{
    ProductItem listProducts[];

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.list_products);

        setOnClick();
        initializeProductsList();
        createList();
    }

    private void initializeProductsList(ProductItem[]... products) {
        // listProducts = products;
        listProducts = new ProductItem[3];
        listProducts[0] = new ProductItem("Fardamento",5, "121212", null);
        listProducts[1] = new ProductItem("Computadores",25, "123123", null);
        listProducts[2] = new ProductItem("Ferramentas",5, "444444", null);
    }

    public void createList() {
        MaterialCardView cards[] = new MaterialCardView[listProducts.length];
        for (int index = 0; index < cards.length; index++) {
            cards[index].addView(createTextView(listProducts[index].title));
            cards[index].addView(createTextView(Integer.toString(listProducts[index].amount)));
            cards[index].addView(createTextView(listProducts[index].locate));
            if(listProducts[index].description != null) {
                cards[index].addView(createTextView(listProducts[index].description));
            }
            cards[index].setPadding(15,15,15,15);
            cards[index].setMinimumHeight(150);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(15,15,15,15);
        return textView;
    }

    public void setOnClick() {
        FloatingActionButton btn_map = findViewById(R.id.FbtnBottomMap);
        setOnClickInFloatingButton(btn_map, Map.class);

        FloatingActionButton btn_camera = findViewById(R.id.FbtnBottomCam);
        setOnClickInFloatingButton(btn_camera, ActivitySplashScreen.class);
    }

}
