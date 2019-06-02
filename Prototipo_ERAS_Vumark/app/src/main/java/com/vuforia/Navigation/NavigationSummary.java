package com.vuforia.Navigation;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.Enums.HttpConnectionMethodEnum;
import com.vuforia.Models.List;
import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.Services.APIConnection;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;
import com.google.gson.Gson;
import com.vuforia.VuMark.VuMark;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;

import okhttp3.Response;

public class NavigationSummary extends Navigate
{
    private LinearLayout cardsLinerLayout, mainLayout;
    private String requestBodyJson;
    private Button btn_finish;
    private int attempts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.navigation_summary);
        attempts = 0;
        btn_finish = findViewById(R.id.btn_finish);
        cardsLinerLayout = findViewById(R.id.LinearLayoutList);
        mainLayout = findViewById(R.id.mainNavigationSummLayout);
        AddProductsInView();
        btn_finish.setOnClickListener(sendJSON);
    }

    @Override
    protected void SetOnClick() { }

    /**
     * Method to create, fill and insert cards in view
     */
    private void AddProductsInView()
    {
        // TODO: vincular com todos os produtos
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
                    productLocation.setText(l.ToString());
                else
                    productLocation.setText("Indefinido");
                TextView productQuantitys = newCard.findViewById(R.id.card_product_quantityC);
                productQuantitys.setText(String.format(Locale.getDefault(), "%d", product.getQuantityCatched()));
            }
        }
    }

    /**
     * Method to create a JSON string to send by POST
     * @return a JSON string
     */
    private String CreateJson()
    {
        List list = Data.getProductList();
        if(list != null)
        {
            JSONObject listJSON = new JSONObject();
            try {
                listJSON.put("ListId", list.getListId());
                listJSON.put("Name", list.getName());
                listJSON.put("Requester", list.getRequester());
                listJSON.put("Time", list.getRunningTime());
                Gson gson = new Gson();
                String jsonProducts = gson.toJson(list.getProducts());
                listJSON.put("ProductsList", jsonProducts);
                return String.format("\"%s\"", listJSON.toString()
                        .replace("\\\"", "\"")
                        .replace("\"", "'")
                        .replace("'ProductsList':'[{", "'ProductsList':[{")
                        .replace("}]'}", "}]}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    View.OnClickListener sendJSON = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            RequestList();
            goToActivity(v, OpenApp.class);
        }
    };

    /**
     * Method that starts a request to API
     */
    private void RequestList()
    {
        String url = Data.getAPIUrl();
        requestBodyJson = CreateJson();
        new RequestData().execute(url);
    }

    private void ShowSnackbar(String message)
    {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Class to execute a request to API
     */
    private class RequestData extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                Response response = APIConnection.Request(urls[0], requestBodyJson, HttpConnectionMethodEnum.POST);
                if (response != null && response.body() != null)
                {
                    if(response.code() == HttpURLConnection.HTTP_OK)
                    {
                        return response.body().string();
                    }
                    else
                    {
                        ShowSnackbar(response.body().string().replace("\\\"", ""));
                        return null;
                    }
                }
                return null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result == null)
            {
                attempts++;
                if(attempts != 3)
                    RequestList();
                return;
            }
            ShowSnackbar(result.replace("\"", ""));
        }
    }
}
