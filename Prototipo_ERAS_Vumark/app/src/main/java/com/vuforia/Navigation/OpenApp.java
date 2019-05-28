package com.vuforia.Navigation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import com.vuforia.Models.List;
import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.Services.APIConnection;
import com.vuforia.Services.PathFinderService;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;
import com.vuforia.Util.Tuple;
import com.vuforia.VuMark.VuMark;

public class OpenApp extends Navigate
{
    String url, requestBody;

    @Override
    protected void SetOnClick() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.open_view);

        ArrayList<Tuple<Integer, Integer>> destinations = new ArrayList<>();
        destinations.add(new Tuple<>(5, 13));
        destinations.add(new Tuple<>(0, 11));
        PathFinderService pfs = new PathFinderService(new Tuple<>(27, 8), destinations);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, "Camisa CAI TEC INDUSTRIAL", 4, 25, 0));
        products.add(new Product(2, "Detergente Neutro", 3, 5, 0));
        List list = new List(0, "MockList", new Time(0,0,0), products);
        Data.Init(pfs, list, getLocations());
        startApp();

        RequestList();
    }

    private void RequestList()
    {
        url = Data.getAPIUrl();
        requestBody = "[]";
        new RequestData().execute(url);
    }

    private ArrayList<Location> getLocations()
    {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(1, 3,1,1,1));
        locations.add(new Location(2, 3,1,2,1));
        locations.add(new Location(3, 7,5,9,3));
        locations.add(new Location(4, 9,3,4,3));
        return locations;
    }

    private void startApp() {
        Button btn_open = findViewById(R.id.btn_open);
        setOnClickInButton(btn_open, VuMark.class);
    }

    private class RequestData extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return APIConnection.Request(urls[0], requestBody,0);
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
            /* TODO: Realizar tratamento dos dados recebidos
            try
            {
                JSONObject jsonObject = new JSONObject(result);
            }
            catch (JSONException e)
            {
            }*/
        }
    }
}
