/*===============================================================================
Copyright (c) 2016-2018 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.vuforia.VuMark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuforia.CameraDevice;
import com.vuforia.DataSet;
import com.vuforia.FUSION_PROVIDER_TYPE;
import com.vuforia.HINT;
import com.vuforia.Models.Cell;
import com.vuforia.Models.Location;
import com.vuforia.Models.Product;
import com.vuforia.Navigation.ListProducts;
import com.vuforia.Navigation.Map;
import com.vuforia.Navigation.Navigate;
import com.vuforia.ObjectTracker;
import com.vuforia.PositionalDeviceTracker;
import com.vuforia.STORAGE_TYPE;
import com.vuforia.SampleApplication.SampleApplicationControl;
import com.vuforia.SampleApplication.SampleApplicationException;
import com.vuforia.SampleApplication.SampleApplicationSession;
import com.vuforia.SampleApplication.utils.LoadingDialogHandler;
import com.vuforia.SampleApplication.utils.SampleApplicationGLView;
import com.vuforia.SampleApplication.utils.Texture;
import com.vuforia.State;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Util.Data;
import com.vuforia.Vuforia;
import com.vuforia.UI.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;


public class VuMark extends Navigate implements SampleApplicationControl
{
    private static final String LOGTAG = "VuMark";

    private String imageRender = "arrow_right.png";

    private String dataBaseUrl = "Vumark_PickByPath.xml";
    
    private SampleApplicationSession vuforiaAppSession;
    
    private DataSet mCurrentDataset;

    private SampleApplicationGLView mGlView;

    private VuMarkRenderer mRenderer;
    
    private GestureDetector mGestureDetector;
    
    // The textures we will use for rendering:
    private Vector<Texture> mTextures;

    private boolean mDeviceTracker = false;

    private RelativeLayout mUILayout;
    
    final LoadingDialogHandler loadingDialogHandler = new LoadingDialogHandler(this);

    View _viewCard;
    private TextView _textType;
    private TextView _textValue;
    private ImageView _instanceImageView;

    // Alert Dialog used to display SDK errors
    private AlertDialog mErrorDialog;
    
    private boolean mIsDroidDevice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.camera_overlay_reticle);
        SetOnClick();

        vuforiaAppSession = new SampleApplicationSession(this);
        
        startLoadingAnimation();

        vuforiaAppSession.initAR( this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mGestureDetector = new GestureDetector(this, new GestureListener());
        
        // Load any sample specific textures:
        mTextures = new Vector<>();
        loadTextures();
        
        mIsDroidDevice = Build.MODEL.toLowerCase().startsWith(
            "droid");

        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        _viewCard = View.inflate(this, R.layout.card, null);
        _viewCard.setVisibility(View.INVISIBLE);

        Button cardLayout = (Button) _viewCard.findViewById(R.id.button_send_amount);

        cardLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean shouldHideCard = getAmoutOfProducts(view);
                if(shouldHideCard)
                {
                    hideCard();
                }
            }
        });

        addContentView(_viewCard, layoutParamsControl);

        _textType = (TextView) _viewCard.findViewById(R.id.text_type);
        _textValue = (TextView) _viewCard.findViewById(R.id.text_value);
        _instanceImageView = (ImageView) _viewCard.findViewById(R.id.instance_image);
    }

    public void SetOnClick() {
        FloatingActionButton btn_map = findViewById(R.id.FbtnBottomMap);
        setOnClickInFloatingButton(btn_map, Map.class);

        FloatingActionButton btn_list = findViewById(R.id.FbtnBottomList);
        setOnClickInFloatingButton(btn_list, ListProducts.class);
    }

    private class GestureListener extends
        GestureDetector.SimpleOnGestureListener
    {
        // Used to set autofocus one second after a manual focus is triggered
        private final Handler autofocusHandler = new Handler();
        
        
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }


        // Process Single Tap event to trigger autofocus
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            boolean result = CameraDevice.getInstance().setFocusMode(
                    CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);
            if (!result)
                Log.e("SingleTapUp", "Unable to trigger focus");

            // Generates a Handler to trigger continuous auto-focus
            // after 1 second
            autofocusHandler.postDelayed(new Runnable()
            {
                public void run()
                {
                    final boolean autofocusResult = CameraDevice.getInstance().setFocusMode(
                            CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

                    if (!autofocusResult)
                        Log.e("SingleTapUp", "Unable to re-enable continuous auto-focus");
                }
            }, 1000L);
            
            return true;
        }
    }


    // Load specific textures from the APK, which we will later use for rendering.
    private void loadTextures()
    {
        mTextures.add(Texture.loadTextureFromApk(this.imageRender,
                getAssets()));
    }
    

    @Override
    protected void onResume()
    {
        Log.d(LOGTAG, "onResume");
        super.onResume();

        showProgressIndicator(true);
        
        // This is needed for some Droid devices to force portrait
        if (mIsDroidDevice)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        
        vuforiaAppSession.onResume();
    }
    
    
    // Callback for configuration changes the activity handles itself
    @Override
    public void onConfigurationChanged(Configuration config)
    {
        Log.d(LOGTAG, "onConfigurationChanged");
        super.onConfigurationChanged(config);
        
        vuforiaAppSession.onConfigurationChanged();
    }
    

    @Override
    protected void onPause()
    {
        Log.d(LOGTAG, "onPause");
        super.onPause();
        
        if (mGlView != null)
        {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
        }

        vuforiaAppSession.onPause();
    }
    

    @Override
    protected void onDestroy()
    {
        Log.d(LOGTAG, "onDestroy");
        super.onDestroy();
        
        try
        {
            vuforiaAppSession.stopAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
        
        // Unload texture:
        mTextures.clear();
        mTextures = null;
        
        System.gc();
    }
    

    private void initApplicationAR()
    {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();
        
        mGlView = new SampleApplicationGLView(this);
        mGlView.init(translucent, depthSize, stencilSize);
        
        mRenderer = new VuMarkRenderer(this, vuforiaAppSession);
        mRenderer.setTextures(mTextures);
        mGlView.setRenderer(mRenderer);
        
    }
    
    
    private void startLoadingAnimation()
    {
        mUILayout = (RelativeLayout) View.inflate(this, R.layout.camera_overlay_reticle,
            null);
        
        mUILayout.setVisibility(View.VISIBLE);
        mUILayout.setBackgroundColor(Color.BLACK);

        loadingDialogHandler.mLoadingDialogContainer = mUILayout
            .findViewById(R.id.loading_indicator);
        
        // Shows the loading indicator at start
        loadingDialogHandler.sendEmptyMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG);

        addContentView(mUILayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

    }

    boolean isGoal(final String value, String[] productName)
    {
        ArrayList<Cell> vumarcells = Data.getCellsVumarkByVuMarkId(value.substring(0, 3));
        if(vumarcells != null && vumarcells.size() > 0)
        {
            ArrayList<Cell> destinations = Data.getPathFinderService().GetDestinations();
            for (Cell c: destinations)
            {
                for (Cell c2: vumarcells)
                {
                    if(c.Equals(c2))
                    {
                        ArrayList<Product> products = getProductsByCell(c);
                        if(products.size() > 0)
                        {
                            Product p = Data.getProductList().getProductById(products.get(0).getProductId());
                            try
                            {

                                productName[0] = p.getName();
                                productName[1] = Data.getLocationById(p.getLocationId()).ToString();
                            }
                            catch(Exception ignored) { }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // it is called from VuMarkRenderer class in method renderFrame
    void showCard(final String type, final String value, final Bitmap bitmap)
    {
        final Context context = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // if card is already visible with same VuMark, do nothing
                if ((_viewCard.getVisibility() == View.VISIBLE) && (_textValue.getText().equals(value)))
                {
                    return;
                }

                Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.bottom_up);

                _textType.setText(type);
                _textValue.setText(value);

                if (bitmap != null)
                {
                    _instanceImageView.setImageBitmap(bitmap);
                }

                _viewCard.bringToFront();
                _viewCard.setVisibility(View.VISIBLE);
                _viewCard.startAnimation(bottomUp);
            }
        });
    }

    public void hideCard()
    {
        final Context context = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // if card not visible, do nothing
                if (_viewCard.getVisibility() != View.VISIBLE)
                {
                    return;
                }

                _textType.setText("");
                _textValue.setText("");
                Animation bottomDown = AnimationUtils.loadAnimation(context,
                        R.anim.bottom_down);

                _viewCard.startAnimation(bottomDown);
                _viewCard.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean doLoadTrackersData()
    {
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
            .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset == null)
            mCurrentDataset = objectTracker.createDataSet();

        return mCurrentDataset != null
                && mCurrentDataset.load(dataBaseUrl, STORAGE_TYPE.STORAGE_APPRESOURCE)
                && objectTracker.activateDataSet(mCurrentDataset);
    }
    
    @Override
    public boolean doUnloadTrackersData()
    {
        // Indicate if the trackers were unloaded correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
            .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;
        
        if (mCurrentDataset != null && mCurrentDataset.isActive())
        {
            if (objectTracker.getActiveDataSets().at(0).equals(mCurrentDataset)
                && !objectTracker.deactivateDataSet(mCurrentDataset))
            {
                result = false;
            } else if (!objectTracker.destroyDataSet(mCurrentDataset))
            {
                result = false;
            }

            mCurrentDataset = null;
        }
        
        return result;
    }

    @Override
    public void onVuforiaResumed()
    {
        if (mGlView != null)
        {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }
    }


    // Called once Vuforia has been initialized or
    // an error has caused Vuforia initialization to stop
    @Override
    public void onInitARDone(SampleApplicationException exception)
    {
        
        if (exception == null)
        {
            initApplicationAR();

            mRenderer.setActive(true);

            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            addContentView(mGlView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
            
            // Sets the UILayout to be drawn in front of the camera
            mUILayout.bringToFront();

            mUILayout.setBackgroundColor(Color.TRANSPARENT);

            vuforiaAppSession.startAR(CameraDevice.CAMERA_DIRECTION.CAMERA_DIRECTION_DEFAULT);

        }
        else
        {
            Log.e(LOGTAG, exception.getString());
            showInitializationErrorMessage(exception.getString());
        }
    }


    @Override
    public void onVuforiaStarted()
    {
        mRenderer.updateRenderingPrimitives();

        // Set camera focus mode
        if(!CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO))
        {
            // If continuous autofocus mode fails, attempt to set to a different mode
            if(!CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO))
            {
                CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_NORMAL);
            }
        }

        showProgressIndicator(false);
    }


    private void showProgressIndicator(boolean show)
    {
        if (show)
        {
            loadingDialogHandler.sendEmptyMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG);
        }
        else
        {
            loadingDialogHandler.sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);
        }
    }
    

    private void showInitializationErrorMessage(String message)
    {
        final String errorMessage = message;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (mErrorDialog != null)
                {
                    mErrorDialog.dismiss();
                }
                
                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                    VuMark.this);
                builder
                    .setMessage(errorMessage)
                    .setTitle(getString(R.string.INIT_ERROR))
                    .setCancelable(false)
                    .setIcon(0)
                    .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                finish();
                            }
                        });
                
                mErrorDialog = builder.create();
                mErrorDialog.show();
            }
        });
    }
    

    // Called every frame
    @Override
    public void onVuforiaUpdate(State state)
    {
    }
    
    
    @Override
    public boolean doInitTrackers()
    {
        // Indicate if the trackers were initialized correctly
        boolean result = true;

        // For VuMark, the recommended fusion provider type is
        // the one recommended by the FUSION_OPTIMIZE_IMAGE_TARGETS_AND_VUMARKS enum
        if (!vuforiaAppSession.setFusionProviderType(
                FUSION_PROVIDER_TYPE.FUSION_OPTIMIZE_IMAGE_TARGETS_AND_VUMARKS))
        {
            return false;
        }
        
        TrackerManager tManager = TrackerManager.getInstance();
        Tracker tracker;
        
        // Trying to initialize the image tracker
        tracker = tManager.initTracker(ObjectTracker.getClassType());
        if (tracker == null)
        {
            Log.e(
                LOGTAG,
                "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else
        {
            Log.i(LOGTAG, "Tracker successfully initialized");
        }

        // Initialize the Positional Device Tracker
        PositionalDeviceTracker deviceTracker = (PositionalDeviceTracker)
                tManager.initTracker(PositionalDeviceTracker.getClassType());

        if (deviceTracker != null)
        {
            Log.i(LOGTAG, "Successfully initialized Device Tracker");
        }
        else
        {
            Log.e(LOGTAG, "Failed to initialize Device Tracker");
        }

        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 10);
        return result;
    }
    
    
    @Override
    public boolean doStartTrackers()
    {
        // Indicate if the trackers were started correctly
        boolean result = true;

        TrackerManager trackerManager = TrackerManager.getInstance();

        Tracker objectTracker = trackerManager.getTracker(ObjectTracker.getClassType());

        if (objectTracker != null && objectTracker.start())
        {
            Log.i(LOGTAG, "Successfully started Object Tracker");
        }
        else
        {
            Log.e(LOGTAG, "Failed to start Object Tracker");
            result = false;
        }

        if (isDeviceTrackingActive())
        {
            PositionalDeviceTracker deviceTracker = (PositionalDeviceTracker) trackerManager
                    .getTracker(PositionalDeviceTracker.getClassType());

            if (deviceTracker != null && deviceTracker.start())
            {
                Log.i(LOGTAG, "Successfully started Device Tracker");
            }
            else
            {
                Log.e(LOGTAG, "Failed to start Device Tracker");
            }
        }
        
        return result;
    }
    
    
    @Override
    public boolean doStopTrackers()
    {
        // Indicate if the trackers were stopped correctly
        boolean result = true;

        TrackerManager trackerManager = TrackerManager.getInstance();
        
        Tracker objectTracker = trackerManager.getTracker(ObjectTracker.getClassType());

        if (objectTracker != null)
        {
            objectTracker.stop();
            Log.i(LOGTAG, "Successfully stopped object tracker");
        }
        else
        {
            Log.e(LOGTAG, "Could not stop object tracker");
            result = false;
        }

        // Stop the device tracker
        if(isDeviceTrackingActive())
        {

            Tracker deviceTracker = trackerManager.getTracker(PositionalDeviceTracker.getClassType());

            if (deviceTracker != null)
            {
                deviceTracker.stop();
                Log.i(LOGTAG, "Successfully stopped device tracker");
            }
            else
            {
                Log.e(LOGTAG, "Could not stop device tracker");
            }
        }

        return result;
    }

    @Override
    public boolean doDeinitTrackers()
    {
        TrackerManager tManager = TrackerManager.getInstance();
        boolean result = tManager.deinitTracker(ObjectTracker.getClassType());
        tManager.deinitTracker(PositionalDeviceTracker.getClassType());
        
        return result;
    }
    
    private boolean isDeviceTrackingActive()
    {
        return mDeviceTracker;
    }

    public void setImageRender(String imageRender) {
        // TODO: verificar em que momento essa variavel eh chamada para chamar esse metodo antes
        switch (imageRender) {
            case "LEFT":
                this.imageRender = "arrow_left.png";
                break;
            case "RIGHT":
                this.imageRender = "arrow_right.png";
                break;
            case "TOP":
                this.imageRender = "arrow_top.png";
                break;
            case "BOTTOM":
                this.imageRender = "arrow_bottom.png";
                break;
            case "CHECKED":
                this.imageRender = "checked.png";
                hideCard();
                break;
        }
    }

    public boolean getAmoutOfProducts(View view)
    {
        EditText msgTextField = findViewById(R.id.amount);
        String amountS = msgTextField.getText().toString();
        hideKeyboardFrom(this, view);
        if(!amountS.isEmpty())
        {
            int amountI;
            try
            {
                amountI = Integer.parseInt(amountS);
            }
            catch(Exception e)
            {
                return false;
            }
            Cell currentCell = Data.getPathFinderService().GetCurrentCell();
            if(currentCell != null)
            {
                ArrayList<Product> products = getProductsByCell(currentCell);
                if(products.size() > 0)
                {
                    Data.getProductList().getProductById(products.get(0).getProductId()).setQuantityCatched(amountI);
                    msgTextField.setText("");
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Product> getProductsByCell(Cell cell)
    {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Cell> cells = Data.getCellsVumarkByCell(cell);
        for (Cell c: cells)
        {
            for (int lid: c.getLocationsId())
            {
                products.addAll(Data.getProductList().getProductsByLocationId(lid));
            }
        }
        return products;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setDataBaseUrl(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
    }
}
