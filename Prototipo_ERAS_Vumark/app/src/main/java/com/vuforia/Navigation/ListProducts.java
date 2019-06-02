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
import android.widget.ScrollView;
import android.widget.TextView;

import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;
import com.vuforia.VuMark.VuMark;

import java.util.ArrayList;
import java.util.Locale;

public class ListProducts extends Navigate
{
    LinearLayout cardsLinerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.list_products);
        cardsLinerLayout = findViewById(R.id.listProducts);
        changeMenu("LIST");

        SetOnClick();
        AddProductsInView(FilterProducts(Data.getProductList().getProducts()));
    }

    /**
     * Method to filter an ArrayList of products and return all of them that were not visited
     * @param products products to filter
     * @return products filtered
     */
    private ArrayList<Product> FilterProducts(ArrayList<Product> products)
    {
        ArrayList<Product> _products = new ArrayList<>();
        if(products != null && products.size() > 0)
        {
            for(Product product: products)
            {
                if(!product.WasVisited()) _products.add(product);
            }
        }
        return _products;
    }

    private void AddProductsInView(ArrayList<Product> products)
    {
        if(products != null && products.size() > 0)
        {
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
                        productLocation.setText(l.ToString());
                    else
                        productLocation.setText("Indefinido");
                    LinearLayout ll = newCard.findViewById(R.id.catchedLayout);
                    if(ll != null)
                        ll.setVisibility(View.GONE);
                }
            }
        }
        else
        {
            ShowExceptionMessage();
        }
    }

    private void ShowExceptionMessage()
    {
        ScrollView scrollView = findViewById(R.id.scrollListProducts);
        scrollView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.tv_no_products);
        textView.setVisibility(View.VISIBLE);
    }

    public void SetOnClick() {
        ImageView btn_list = findViewById(R.id.image_map);
        setOnClickInImageView(btn_list, Map.class);

        ImageView btn_camera = findViewById(R.id.image_camera);
        setOnClickInImageView(btn_camera, VuMark.class);

        Button btn_end = findViewById(R.id.btn_end);
        setOnClickInButton(btn_end, NavigationSummary.class);
    }

}
