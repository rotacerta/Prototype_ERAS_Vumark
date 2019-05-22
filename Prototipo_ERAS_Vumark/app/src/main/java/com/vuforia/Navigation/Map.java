package com.vuforia.Navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Enums.Map.MapBuildingsEnum;
import com.vuforia.Enums.Map.MapDefinitionsEnum;
import com.vuforia.Enums.Map.MapUnmappedEnum;
import com.vuforia.Enums.StreetsEnum;
import com.vuforia.Models.Cell;
import com.vuforia.Services.MapService;
import com.vuforia.UI.R;
import com.vuforia.Util.Tuple;
import com.vuforia.VuMark.VuMark;

public class Map extends DrawerNavigation
{
    private Cell[][] matrix;
    private int MatrixRows;
    private int MatrixColumns;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.map);

        MatrixRows = MapDefinitionsEnum.ROWS.Value;
        MatrixColumns = MapDefinitionsEnum.COLUMNS.Value;

        InitMatrix();

        AddGridLayoutInLayout((RelativeLayout) findViewById(R.id.contentGridIBGLayout));

        navDrawer = (NavigationView) findViewById(R.id.navView_map);
        initializeDrawer();
    }

    private void AddGridLayoutInLayout(RelativeLayout contentLayout)
    {
        if(contentLayout != null)
        {
            Tuple<Integer, Integer> viewDimensions = GetDeviceDimension();
            int contentWidth = (viewDimensions.value * 8) / 10;
            int contentHeight = (viewDimensions.key * 8) / 10;

            GridLayout gridLayout = GenerateGridLayout(contentHeight, contentWidth);
            contentLayout.addView(gridLayout);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) gridLayout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            gridLayout.setLayoutParams(layoutParams);

            SetViewSize(gridLayout, contentHeight, contentWidth);

            ImageView ivStreetLabel = GenerateStreetLabelImageView(contentHeight, contentWidth);
            contentLayout.addView(ivStreetLabel);
        }
    }

    /**
     * Method to generate a grid Layout
     * @param maxCellHeight The max Height of a cell (maybe could be a height size of complete layout)
     * @param maxCellWidth The max Width of a cell (maybe could be a width size of complete layout)
     * @return A GridLayout
     */
    private GridLayout GenerateGridLayout(int maxCellHeight, int maxCellWidth)
    {
        GridLayout gridLayout = new GridLayout(this);
        int gridRows = MatrixColumns;
        int gridColumns = MatrixRows;
        gridLayout.setRowCount(gridRows);
        gridLayout.setColumnCount(gridColumns);
        int cellHeight = maxCellHeight / gridRows;
        int cellWidth = maxCellWidth / gridColumns;
        for(int col = 0, itemRow = 0; col < MatrixColumns; col++, itemRow++)
        {
            for(int row = MatrixRows - 1, itemCol = 0; row > -1; row--, itemCol++)
            {
                GridLayout.Spec rowSpec = GridLayout.spec(itemRow);
                GridLayout.Spec colSpec = GridLayout.spec(itemCol);
                GridLayout.LayoutParams gidLayoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
                ImageView cellImage = new ImageView(this);
                if(matrix[row][col].getValue() == CellValueEnum.OBSTACLE.Value)
                {
                    cellImage.setBackgroundResource(R.drawable.cell_normal);
                }
                else if(matrix[row][col].getValue() == CellValueEnum.WALKABLE.Value)
                {
                    cellImage.setBackgroundResource(R.drawable.white_square);
                }
                else if(matrix[row][col].getValue() == CellValueEnum.ENDPOINT.Value)
                {
                    cellImage.setBackgroundResource(R.drawable.cell_destin);
                }
                else if(matrix[row][col].getValue() == CellValueEnum.STARTPOINT.Value)
                {
                    cellImage.setBackgroundResource(R.drawable.cell_current);
                }
                else if(matrix[row][col].getValue() == CellValueEnum.UNMAPPED.Value)
                {
                    cellImage.setBackgroundResource(R.drawable.grey_square);
                }
                gridLayout.addView(cellImage, gidLayoutParams);
                cellImage.getLayoutParams().height = cellHeight;
                cellImage.getLayoutParams().width = cellWidth;
            }
        }
        return gridLayout;
    }

    /**
     * Method to create the labels with identifiers of the streets
     * @param height The max height of the view
     * @param width The max width of the view
     * @return A ImageView with the street labels
     */
    private ImageView GenerateStreetLabelImageView(int height, int width)
    {
        ImageView imageView = new ImageView(this);
        Bitmap bmStreetLabel = CreateStreetLabelBitmap(height, width);
        imageView.setImageBitmap(bmStreetLabel);
        return imageView;
    }

    /**
     * Method to create a Bitmap with identifiers of the streets
     * @param height The max height of the view
     * @param width The max width of the view
     * @return A Bitmap with the street labels
     */
    private Bitmap CreateStreetLabelBitmap(int height, int width)
    {
        Bitmap streetLabels = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(streetLabels);

        float Ymodifier = height / MatrixColumns;
        float Xmodifier = width / MatrixRows;

        for(StreetsEnum street : StreetsEnum.values())
        {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.street_label_size));
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawBitmap(streetLabels, 0, 0, paint);
            canvas.save();
            float x = (street.XCOORD * Xmodifier) + (street.XMOD * Xmodifier);
            float y = (street.YCOORD * Ymodifier) + (street.YMOD * Xmodifier);
            canvas.rotate(street.ROTATION, x, y);
            canvas.drawText(street.TEXT, x, y, paint);
            canvas.restore();
        }
        return streetLabels;
    }

    /**
     * Method to set height and width to a view
     * @param view View that you want to set size
     * @param height height
     * @param width width
     */
    private void SetViewSize(View view, int height, int width)
    {
        view.getLayoutParams().height = height;
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    /**
     * Method to get screen dimensions
     * @return a Tuple object with height (key) and width (value) values
     */
    private Tuple<Integer, Integer> GetDeviceDimension()
    {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Tuple<>(dm.heightPixels, dm.widthPixels);
    }

    /**
     * Temporary method
     */
    private void InitMatrix()
    {
        MapService mapService = new MapService();
        this.matrix = mapService.GetMap();
        //TODO: Delete these rows below when necessary
        //These values will be currentCell and nextdestination (variables of PathFinderService)
        this.matrix[17][0].setValue(CellValueEnum.STARTPOINT.Value);
        this.matrix[0][9].setValue(CellValueEnum.ENDPOINT.Value);
    }
}

