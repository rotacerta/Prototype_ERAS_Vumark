package com.vuforia.Navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Enums.Streets;
import com.vuforia.Models.Cell;
import com.vuforia.UI.R;
import com.vuforia.UI.ActivitySplashScreen;
import com.vuforia.Util.Tuple;

import java.util.ArrayList;

public class Map extends Navigate
{
    private Cell[][] matrix;
    private int MatrixRows = 28;
    private int MatrixColumns = 18;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.map);
        SetOnClick();

        InitMatrix();

        RelativeLayout contentLayout = findViewById(R.id.contentGridIBGLayout);
        Tuple<Integer, Integer> viewDimensions = GetDeviceDimension();
        int contentWidth = (viewDimensions.value * 9) / 10;
        int contentHeight = (viewDimensions.key * 8) / 10;

        GridLayout gridLayout = GenerateGridLayout(contentHeight, contentWidth);
        contentLayout.addView(gridLayout);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) gridLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        gridLayout.setLayoutParams(layoutParams);

        SetViewSize(contentLayout, contentHeight, contentWidth);
        SetViewSize(gridLayout, contentHeight, contentWidth);

        ImageView ivStreetLabel = GenerateStreetLabelImageView(contentHeight, contentWidth);
        contentLayout.addView(ivStreetLabel);
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

        for(Streets street : Streets.values())
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
        /*
         * A ideia eh que vc pegue os itens do enum streets e para cada um insira no bitmap de acordo com os valores
         *
         * As variaveis XCoord e YCoord vao ser usadas com base nos modificadores Ymodifier e Xmodifier, como eles guardam
         * o tamanho de cada celula na hora de inserir o text, espero, que seja so multiplicar o modificador pela coordenada do enum*/
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
        this.matrix = GetMatrixToGridLayout();
        FillMatrixObstacles();
        FillMatrixUnmappedArea();
    }

    /**
     * I think, maybe, the matrix could be in a local storage and not created statically
     * @return Matrix to be the base of GridLayout
     */
    private Cell[][] GetMatrixToGridLayout()
    {
        Cell[][] _matrix = new Cell[MatrixRows][MatrixColumns];
        for(int row = 0; row < MatrixRows; row++)
        {
            for(int col = 0; col < MatrixColumns; col++)
            {
                _matrix[row][col] = new Cell(row, col, CellValueEnum.WALKABLE.Value);
            }
        }
        return  _matrix;
    }

    /**
     * Temporary method to fill the matrix with obstacles
     */
    private void FillMatrixObstacles()
    {
        ArrayList<Tuple<Integer, Integer>> obstacles = new ArrayList<>();
        obstacles.add(new Tuple<>(0, 3));
        obstacles.add(new Tuple<>(0, 4));
        obstacles.add(new Tuple<>(0, 5));
        obstacles.add(new Tuple<>(0, 6));
        obstacles.add(new Tuple<>(0, 7));
        obstacles.add(new Tuple<>(0, 8));
        obstacles.add(new Tuple<>(0, 9));
        obstacles.add(new Tuple<>(0, 10));
        obstacles.add(new Tuple<>(0, 11));
        obstacles.add(new Tuple<>(0, 12));
        obstacles.add(new Tuple<>(1, 0));
        obstacles.add(new Tuple<>(2, 0));
        obstacles.add(new Tuple<>(2, 13));
        obstacles.add(new Tuple<>(2, 14));
        obstacles.add(new Tuple<>(2, 15));
        obstacles.add(new Tuple<>(3, 0));
        obstacles.add(new Tuple<>(3, 3));
        obstacles.add(new Tuple<>(3, 4));
        obstacles.add(new Tuple<>(3, 5));
        obstacles.add(new Tuple<>(3, 6));
        obstacles.add(new Tuple<>(3, 9));
        obstacles.add(new Tuple<>(3, 10));
        obstacles.add(new Tuple<>(3, 13));
        obstacles.add(new Tuple<>(3, 14));
        obstacles.add(new Tuple<>(3, 15));
        obstacles.add(new Tuple<>(4, 0));
        obstacles.add(new Tuple<>(4, 3));
        obstacles.add(new Tuple<>(4, 4));
        obstacles.add(new Tuple<>(4, 5));
        obstacles.add(new Tuple<>(4, 6));
        obstacles.add(new Tuple<>(4, 9));
        obstacles.add(new Tuple<>(4, 10));
        obstacles.add(new Tuple<>(4, 13));
        obstacles.add(new Tuple<>(4, 14));
        obstacles.add(new Tuple<>(4, 15));
        obstacles.add(new Tuple<>(5, 0));
        obstacles.add(new Tuple<>(5, 3));
        obstacles.add(new Tuple<>(5, 4));
        obstacles.add(new Tuple<>(5, 5));
        obstacles.add(new Tuple<>(5, 6));
        obstacles.add(new Tuple<>(5, 9));
        obstacles.add(new Tuple<>(5, 10));
        obstacles.add(new Tuple<>(5, 13));
        obstacles.add(new Tuple<>(5, 14));
        obstacles.add(new Tuple<>(5, 15));
        obstacles.add(new Tuple<>(6, 0));
        obstacles.add(new Tuple<>(6, 3));
        obstacles.add(new Tuple<>(6, 4));
        obstacles.add(new Tuple<>(6, 5));
        obstacles.add(new Tuple<>(6, 6));
        obstacles.add(new Tuple<>(6, 9));
        obstacles.add(new Tuple<>(6, 10));
        obstacles.add(new Tuple<>(7, 0));
        obstacles.add(new Tuple<>(7, 3));
        obstacles.add(new Tuple<>(7, 4));
        obstacles.add(new Tuple<>(7, 5));
        obstacles.add(new Tuple<>(7, 6));
        obstacles.add(new Tuple<>(8, 0));
        obstacles.add(new Tuple<>(8, 3));
        obstacles.add(new Tuple<>(8, 4));
        obstacles.add(new Tuple<>(8, 5));
        obstacles.add(new Tuple<>(8, 6));
        obstacles.add(new Tuple<>(8, 9));
        obstacles.add(new Tuple<>(8, 10));
        obstacles.add(new Tuple<>(9, 0));
        obstacles.add(new Tuple<>(9, 3));
        obstacles.add(new Tuple<>(9, 4));
        obstacles.add(new Tuple<>(9, 5));
        obstacles.add(new Tuple<>(9, 6));
        obstacles.add(new Tuple<>(9, 9));
        obstacles.add(new Tuple<>(9, 10));
        obstacles.add(new Tuple<>(10, 0));
        obstacles.add(new Tuple<>(10, 9));
        obstacles.add(new Tuple<>(10, 10));
        obstacles.add(new Tuple<>(11, 0));
        obstacles.add(new Tuple<>(11, 9));
        obstacles.add(new Tuple<>(11, 10));
        obstacles.add(new Tuple<>(12, 0));
        obstacles.add(new Tuple<>(12, 3));
        obstacles.add(new Tuple<>(12, 4));
        obstacles.add(new Tuple<>(12, 5));
        obstacles.add(new Tuple<>(12, 6));
        obstacles.add(new Tuple<>(13, 0));
        obstacles.add(new Tuple<>(13, 3));
        obstacles.add(new Tuple<>(13, 4));
        obstacles.add(new Tuple<>(13, 5));
        obstacles.add(new Tuple<>(13, 6));
        obstacles.add(new Tuple<>(14, 0));
        obstacles.add(new Tuple<>(15, 0));
        obstacles.add(new Tuple<>(16, 0));
        obstacles.add(new Tuple<>(16, 3));
        obstacles.add(new Tuple<>(16, 4));
        obstacles.add(new Tuple<>(16, 5));
        obstacles.add(new Tuple<>(16, 6));
        obstacles.add(new Tuple<>(17, 0));
        obstacles.add(new Tuple<>(17, 3));
        obstacles.add(new Tuple<>(17, 4));
        obstacles.add(new Tuple<>(17, 5));
        obstacles.add(new Tuple<>(17, 6));
        obstacles.add(new Tuple<>(17, 17));
        obstacles.add(new Tuple<>(18, 0));
        obstacles.add(new Tuple<>(18, 17));
        obstacles.add(new Tuple<>(19, 0));
        obstacles.add(new Tuple<>(19, 17));
        obstacles.add(new Tuple<>(20, 0));
        obstacles.add(new Tuple<>(20, 17));
        obstacles.add(new Tuple<>(23, 0));
        obstacles.add(new Tuple<>(24, 0));
        obstacles.add(new Tuple<>(25, 0));
        obstacles.add(new Tuple<>(26, 0));
        obstacles.add(new Tuple<>(27, 0));
        for(Tuple<Integer, Integer> obstacle: obstacles)
        {
            this.matrix[obstacle.key][obstacle.value].setValue(CellValueEnum.OBSTACLE.Value);
        }
        this.matrix[17][3].setValue(CellValueEnum.STARTPOINT.Value);
        this.matrix[0][9].setValue(CellValueEnum.ENDPOINT.Value);
    }

    /**
     * Temporary method to fill the matrix with unmapped area
     */
    private void FillMatrixUnmappedArea()
    {
        ArrayList<Tuple<Integer, Integer>> unmappedArea = new ArrayList<>();
        unmappedArea.add(new Tuple<>(2, 17));
        unmappedArea.add(new Tuple<>(3, 17));
        unmappedArea.add(new Tuple<>(4, 17));
        unmappedArea.add(new Tuple<>(5, 17));
        unmappedArea.add(new Tuple<>(6, 17));
        unmappedArea.add(new Tuple<>(7, 17));
        unmappedArea.add(new Tuple<>(8, 17));
        unmappedArea.add(new Tuple<>(9, 17));
        unmappedArea.add(new Tuple<>(10, 17));
        unmappedArea.add(new Tuple<>(11, 17));
        unmappedArea.add(new Tuple<>(12, 17));
        unmappedArea.add(new Tuple<>(15, 13));
        unmappedArea.add(new Tuple<>(15, 14));
        unmappedArea.add(new Tuple<>(15, 15));
        unmappedArea.add(new Tuple<>(16, 13));
        unmappedArea.add(new Tuple<>(16, 14));
        unmappedArea.add(new Tuple<>(16, 15));
        unmappedArea.add(new Tuple<>(17, 13));
        unmappedArea.add(new Tuple<>(17, 14));
        unmappedArea.add(new Tuple<>(17, 15));
        unmappedArea.add(new Tuple<>(18, 13));
        unmappedArea.add(new Tuple<>(18, 14));
        unmappedArea.add(new Tuple<>(18, 15));
        unmappedArea.add(new Tuple<>(19, 9));
        unmappedArea.add(new Tuple<>(19, 10));
        unmappedArea.add(new Tuple<>(19, 11));
        unmappedArea.add(new Tuple<>(19, 12));
        unmappedArea.add(new Tuple<>(19, 13));
        unmappedArea.add(new Tuple<>(19, 14));
        unmappedArea.add(new Tuple<>(19, 15));
        unmappedArea.add(new Tuple<>(20, 9));
        unmappedArea.add(new Tuple<>(20, 10));
        unmappedArea.add(new Tuple<>(20, 11));
        unmappedArea.add(new Tuple<>(20, 12));
        unmappedArea.add(new Tuple<>(20, 13));
        unmappedArea.add(new Tuple<>(20, 14));
        unmappedArea.add(new Tuple<>(20, 15));
        unmappedArea.add(new Tuple<>(21, 9));
        unmappedArea.add(new Tuple<>(21, 10));
        unmappedArea.add(new Tuple<>(21, 11));
        unmappedArea.add(new Tuple<>(21, 12));
        unmappedArea.add(new Tuple<>(21, 13));
        unmappedArea.add(new Tuple<>(21, 14));
        unmappedArea.add(new Tuple<>(21, 15));
        unmappedArea.add(new Tuple<>(22, 9));
        unmappedArea.add(new Tuple<>(22, 10));
        unmappedArea.add(new Tuple<>(22, 11));
        unmappedArea.add(new Tuple<>(22, 12));
        unmappedArea.add(new Tuple<>(22, 13));
        unmappedArea.add(new Tuple<>(22, 14));
        unmappedArea.add(new Tuple<>(22, 15));
        unmappedArea.add(new Tuple<>(23, 2));
        unmappedArea.add(new Tuple<>(23, 3));
        unmappedArea.add(new Tuple<>(23, 4));
        unmappedArea.add(new Tuple<>(23, 9));
        unmappedArea.add(new Tuple<>(23, 10));
        unmappedArea.add(new Tuple<>(23, 11));
        unmappedArea.add(new Tuple<>(24, 2));
        unmappedArea.add(new Tuple<>(24, 3));
        unmappedArea.add(new Tuple<>(24, 4));
        unmappedArea.add(new Tuple<>(24, 9));
        unmappedArea.add(new Tuple<>(24, 10));
        unmappedArea.add(new Tuple<>(24, 11));
        unmappedArea.add(new Tuple<>(25, 2));
        unmappedArea.add(new Tuple<>(25, 3));
        unmappedArea.add(new Tuple<>(25, 4));
        unmappedArea.add(new Tuple<>(26, 2));
        unmappedArea.add(new Tuple<>(26, 3));
        unmappedArea.add(new Tuple<>(26, 4));
        unmappedArea.add(new Tuple<>(27, 2));
        unmappedArea.add(new Tuple<>(27, 3));
        unmappedArea.add(new Tuple<>(27, 4));
        for(Tuple<Integer, Integer> obstacle: unmappedArea)
        {
            this.matrix[obstacle.key][obstacle.value].setValue(CellValueEnum.UNMAPPED.Value);
        }
    }

    public void SetOnClick()
    {
        FloatingActionButton btn_list = findViewById(R.id.FbtnBottomList);
        setOnClickInFloatingButton(btn_list, ListProducts.class);

        FloatingActionButton btn_camera = findViewById(R.id.FbtnBottomCam);
        setOnClickInFloatingButton(btn_camera, ActivitySplashScreen.class);
    }
}

