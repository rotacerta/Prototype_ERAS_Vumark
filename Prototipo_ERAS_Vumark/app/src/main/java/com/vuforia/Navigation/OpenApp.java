package com.vuforia.Navigation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.sql.Time;
import java.util.ArrayList;

import com.vuforia.Enums.HttpConnectionMethodEnum;
import com.vuforia.Models.Cell;
import com.vuforia.Models.List;
import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.Services.APIConnection;
import com.vuforia.Services.PathFinderService;
import com.vuforia.UI.R;
import com.vuforia.Util.Data;
import com.vuforia.Util.Tuple;
import com.vuforia.VuMark.VuMark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http2.Http2;
import okhttp3.internal.http2.Http2Connection;

public class OpenApp extends Navigate
{
    private ArrayList<Location> locations;
    private ConstraintLayout mainLayout;
    private ArrayList<Product> products;
    private boolean canStartNavigation;
    private String requestBody;
    private Button btnOpen;
    private int attempts;
    private List list;

    @Override
    protected void SetOnClick() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.open_view);

        attempts = 1;
        canStartNavigation = false;
        mainLayout = findViewById(R.id.mainOpenAppLayout);
        btnOpen = findViewById(R.id.btn_open);

        ArrayList<Tuple<Integer, Integer>> destinations = new ArrayList<>();
        destinations.add(new Tuple<>(5, 13));
        destinations.add(new Tuple<>(0, 11));
        PathFinderService pfs = new PathFinderService(new Tuple<>(27, 8), destinations);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, 4, "Camisa CAI TEC INDUSTRIAL", 25, 0,0));
        products.add(new Product(2, 3, "Detergente Neutro", 5, 0,0));
        List list = new List(0, "MockList", "", new Time(0,0,0), products);
        Data.Init(pfs, list, getLocations());
        startApp();
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


    /**
     * Method that starts the objects in the static data class
     */
    private void InitData()
    {
        ArrayList<Integer> locationsId = new ArrayList<>();
        for(Product p: products)
        {
            int lid = p.getLocationId();
            boolean exists = false;
            for(int id: locationsId)
            {
                exists = (id == lid);
                if(exists) break;
            }
            if(!exists) locationsId.add(lid);
        }
        ArrayList<Tuple<Integer, Integer>> destinations = Data.getCellsByLocations(locationsId);
        if(destinations != null && destinations.size() > 0)
        {
            PathFinderService pfs = new PathFinderService(new Tuple<>(27, 8), destinations);
            Data.Init(pfs, list, locations);
        }
        else
        {
            btnOpen.setEnabled(false);
            ShowSnackbar("Não foi possível definir as localizações dos produtos.");
        }
    }

    /**
     * Method that starts a request to API
     */
    private void RequestList()
    {
        String url = Data.getAPIUrl();
        requestBody = "";
        new RequestData().execute(url);
    }

    private void startApp()
    {
        setOnClickInButton(btnOpen, VuMark.class);
    }

    private void ShowSnackbar(String message)
    {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Method to start to extract the objects that the API returned
     * @param jsonObject JSON Object returned by API
     * @throws JSONException if something goes wrong
     */
    private void TreatData(JSONObject jsonObject) throws JSONException
    {
        products = ExtractProducts(jsonObject);
        if(products != null && products.size() > 0)
        {
            list = ExtractList(jsonObject, products);
            if(list.getListId() != 0 && !list.getName().isEmpty())
            {
                locations = ExtractLocations(jsonObject);
                if(locations != null && locations.size() > 0)
                    canStartNavigation = true;
            }
        }
    }

    /**
     * Method to extract a list of locations from a JSON Object returned by API
     * @param jsonObject JSON Object returned by API
     * @return a list of locations
     * @throws JSONException if something goes wrong
     */
    private ArrayList<Location> ExtractLocations(JSONObject jsonObject) throws JSONException
    {
        JSONArray _locations = jsonObject.getJSONArray("Locations");
        ArrayList<Location> locations = new ArrayList<>();
        for(int i = 0, tam = _locations.length(); i < tam; i++)
        {
            int locationId = Integer.parseInt(_locations.getJSONObject(i).getString("LocationId"));
            int structure = Integer.parseInt(_locations.getJSONObject(i).getString("Structure"));
            int street = Integer.parseInt(_locations.getJSONObject(i).getString("Street"));
            int building = Integer.parseInt(_locations.getJSONObject(i).getString("Building"));
            int flat = Integer.parseInt(_locations.getJSONObject(i).getString("Flat"));
            Location location = new Location(locationId, structure, street, building, flat);
            locations.add(location);
        }
        return locations;
    }

    /**
     * Method to extract a list of products from a JSON Object returned by API
     * @param jsonObject JSON Object returned by API
     * @return a list of products
     * @throws JSONException if something goes wrong
     */
    private ArrayList<Product> ExtractProducts(JSONObject jsonObject) throws JSONException
    {
        JSONArray _products = jsonObject.getJSONArray("ProductsList");
        ArrayList<Product> products = new ArrayList<>();
        for(int i = 0, tam = _products.length(); i < tam; i++)
        {
            int productListId = Integer.parseInt(_products.getJSONObject(i).getString("ProductListId"));
            int productId = Integer.parseInt(_products.getJSONObject(i).getString("ProductId"));
            String name = _products.getJSONObject(i).getString("Name");
            int locationId = Integer.parseInt(_products.getJSONObject(i).getString("LocationId"));
            int requiredQuantity = Integer.parseInt(_products.getJSONObject(i).getString("RequiredQuantity"));
            Product product = new Product(productListId, productId, name, locationId, requiredQuantity, 0);
            products.add(product);
        }
        return products;
    }

    /**
     * Method to extract a list from a JSON Object returned by API
     * @param jsonObject JSON Object returned by API
     * @param products a list of products of the list
     * @return a list
     * @throws JSONException if something goes wrong
     */
    private List ExtractList(JSONObject jsonObject, ArrayList<Product> products) throws JSONException
    {
        int listId = Integer.parseInt(jsonObject.getString("ListId"));
        String listName = jsonObject.getString("Name");
        String listRequester = jsonObject.getString("Requester");
        Time listTime = new Time(0, 0 , 0);
        return new List(listId, listName, listRequester, listTime, products);
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
                Response response = APIConnection.Request(urls[0], requestBody, HttpConnectionMethodEnum.GET);
                if (response != null && response.body() != null)
                {
                    if(response.code() == HttpURLConnection.HTTP_OK)
                    {
                        return response.body().string();
                    }
                    else
                    {
                        ShowSnackbar(response.body().string());
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
            try
            {
                TreatData(new JSONObject(result));
                if(canStartNavigation)
                {
                    btnOpen.setText("Iniciar navegação");
                    btnOpen.setEnabled(true);
                    InitData();
                }
            }
            catch (Exception e)
            {
                ShowSnackbar("Falha ao processar lista recebida.");
            }
        }
    }
}
