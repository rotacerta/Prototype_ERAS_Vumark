package com.vuforia.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.Models.ProductItem;
import com.vuforia.UI.R;

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
        ProductItem[] products = GetProducts();
        for (ProductItem product : products)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            if(inflater != null)
            {
                @SuppressLint("InflateParams") View newCard = inflater.inflate(R.layout.card_list, null);
                cardsLinerLayout.addView(newCard);
                TextView productName = newCard.findViewById(R.id.card_product_name);
                productName.setText(product.getTitle());
                TextView productQuantity = newCard.findViewById(R.id.card_product_quantity);
                productQuantity.setText(String.format(Locale.getDefault(), "%d", product.getAmount()));
                TextView productLocation = newCard.findViewById(R.id.card_product_location);
                productLocation.setText(product.getLocate());
            }
        }
    }

    private ProductItem[] GetProducts()
    {
        ProductItem[] products = new ProductItem[3];
        products[0] = new ProductItem("Fardamento",55, "1.1.1.1", null);
        products[1] = new ProductItem("Computadores",25, "2.2.2.2", null);
        products[2] = new ProductItem("Ferramentas",15, "3.3.3.3", null);
        products[2] = new ProductItem("Produtos de Limpeza",5, "4.4.4.4", null);
        products[2] = new ProductItem("Papél Higiênico",35, "5.5.5.5", null);
        products[2] = new ProductItem("Teste com um produto com nome muito grande para verificar o comportamento do card",0, "6.6.6.6", null);
        return  products;
    }

    @Override
    protected void SetOnClick()
    {
        Button btn_finish = findViewById(R.id.btn_finish);
        setOnClickInButton(btn_finish, OpenApp.class);
    }
}
