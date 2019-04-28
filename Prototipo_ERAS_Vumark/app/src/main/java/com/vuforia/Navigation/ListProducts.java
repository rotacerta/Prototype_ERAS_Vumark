package com.vuforia.Navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.Models.ProductItem;
import com.vuforia.UI.R;
import com.vuforia.UI.ActivitySplashScreen;
import com.vuforia.VuMark.VuMark;

public class ListProducts extends Navigate
{
    private ProductItem listProducts[];

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.list_products);
        changeMenu("LIST");

        SetOnClick();
        initializeProductsList();
        CardView cards[] = createList();
        setCardsInLayout(cards);
    }

    private void initializeProductsList(ProductItem[]... products) {
        // listProducts = products;
        listProducts = new ProductItem[3];
        listProducts[0] = new ProductItem("Fardamento",5, "121212", null);
        listProducts[1] = new ProductItem("Computadores",25, "123123", null);
        listProducts[2] = new ProductItem("Ferramentas",5, "444444", null);
    }

    public CardView[] createList() {
        CardView cards[] = new CardView[listProducts.length];
        for (int index = 0; index < cards.length; index++) {
            cards[index] = new CardView(this);
            cards[index].addView( createLinearLayout(index) );
            LinearLayout.LayoutParams cardViewParams
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardViewParams.setMargins(50, 20, 50, 20);
            cards[index].setLayoutParams(cardViewParams);
            cards[index].requestLayout();
            cards[index].setMinimumHeight(150);
            cards[index].setCardBackgroundColor(Color.parseColor("#F0E1BF"));
        }
        return cards;
    }

    public LinearLayout createLinearLayout(int index) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(createTextView("Produto: " + listProducts[index].getTitle()));
        linearLayout.addView(createTextView("Quantidade: " + Integer.toString(listProducts[index].getAmount())));
        linearLayout.addView(createTextView("Local: " + listProducts[index].getLocate()));
        if(listProducts[index].getDescription() != null) {
            linearLayout.addView(createTextView("Descrição: " + listProducts[index].getDescription()));
        }
        return linearLayout;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(30,5,30,5);
        return textView;
    }

    public void setCardsInLayout(CardView[] cards) {
        LinearLayout layout = findViewById(R.id.listProducts);
        for (CardView card: cards) {
            layout.addView(card);
        }
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
