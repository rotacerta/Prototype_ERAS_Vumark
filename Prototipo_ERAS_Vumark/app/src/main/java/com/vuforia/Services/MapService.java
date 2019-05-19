package com.vuforia.Services;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Enums.Map.MapBuildingsEnum;
import com.vuforia.Enums.Map.MapDefinitionsEnum;
import com.vuforia.Enums.Map.MapUnmappedEnum;
import com.vuforia.Models.Cell;

public class MapService
{
    private int Rows;
    private int Columns;
    private Cell[][] map;

    public MapService()
    {
        this.Rows = MapDefinitionsEnum.ROWS.Value;
        this.Columns = MapDefinitionsEnum.COLUMNS.Value;
        this.map = InitMap();
        FillMatrixBuildings();
        FillMatrixUnmappedArea();
    }

    /**
     * Method to get the map
     * @return Map
     */
    public Cell[][] GetMap()
    {
        return this.map;
    }

    /**
     * Method to initialize the map
     * @return The map initialized
     */
    private Cell[][] InitMap()
    {
        Cell[][] _matrix = new Cell[Rows][Columns];
        for(int row = 0; row < Rows; row++)
        {
            for(int col = 0; col < Columns; col++)
            {
                _matrix[row][col] = new Cell( null, 0, 0, 0, false, CellValueEnum.WALKABLE.Value, row, col, 0 );
            }
        }
        return  _matrix;
    }

    /**
     * Method to fill the matrix with Buildings
     */
    private void FillMatrixBuildings()
    {
        for(MapBuildingsEnum building : MapBuildingsEnum.values())
        {
            this.map[building.X][building.Y].setValue(CellValueEnum.OBSTACLE.Value);
        }
    }

    /**
     * Method to fill the matrix with unmapped area
     */
    private void FillMatrixUnmappedArea()
    {
        for(MapUnmappedEnum unmapped : MapUnmappedEnum.values())
        {
            this.map[unmapped.X][unmapped.Y].setValue(CellValueEnum.UNMAPPED.Value);
        }
    }
}
