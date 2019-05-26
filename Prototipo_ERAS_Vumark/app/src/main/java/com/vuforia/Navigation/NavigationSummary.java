package com.vuforia.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;

import java.util.Locale;

public class NavigationSummary extends Navigate
{
    LinearLayout cardsLinerLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.navigation_summary);
        cardsLinerLayout = findViewById(R.id.LinearLayoutList);
        SetOnClick();
        AddProductsInView();
    }

    private void AddProductsInView()
    {
        // TODO: adicionar layout de QuantityCatched no layout do card (na tela de lista basta dar um hide no layout)
        Product[] products = Data.getProductList().getMockProducts();
        for (Product product : products)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            if(inflater != null)
            {
                @SuppressLint("InflateParams") View newCard = inflater.inflate(R.layout.card_list, null);
                cardsLinerLayout.addView(newCard);
                TextView productName = newCard.findViewById(R.id.card_product_name);
                productName.setText(product.getName());
                TextView productQuantity = newCard.findViewById(R.id.card_product_quantity);
                productQuantity.setText(String.format(Locale.getDefault(), "%d", product.getRequiredQuantity()));
                TextView productLocation = newCard.findViewById(R.id.card_product_location);
                Location l = Data.getLocationById(product.getLocationId());
                if(l != null)
                {
                    productLocation.setText(l.ToString());
                }
                else
                {
                    productLocation.setText("Indefinido");
                }
            }
        }
    }

    @Override
    protected void SetOnClick()
    {
        Button btn_finish = findViewById(R.id.btn_finish);
        setOnClickInButton(btn_finish, OpenApp.class);
    }
}
