package com.vuforia.Services;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Enums.Map.MapBuildingsEnum;
import com.vuforia.Enums.Map.MapDefinitionsEnum;
import com.vuforia.Enums.Map.MapUnmappedEnum;
import com.vuforia.Models.Cell;
import com.vuforia.Util.Data;
import com.vuforia.Util.Tuple;

import java.util.ArrayList;

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
        RelateCellsWithVuMarks();
        RelateCellsWithLocations();
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
                _matrix[row][col] = new Cell( null, 0, 0, 0, false, CellValueEnum.WALKABLE.Value, row, col, new ArrayList<Integer>());
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

    /**
     * Method to relate matrix cells with VuMarks ids
     */
    private void RelateCellsWithVuMarks()
    {
        ArrayList<Tuple<String, Cell>> cell_vuMark = new ArrayList<>();
        cell_vuMark.add(new Tuple<>("000", this.map[27][8]));
        cell_vuMark.add(new Tuple<>("001", this.map[15][0]));
        cell_vuMark.add(new Tuple<>("001", this.map[16][0]));
        cell_vuMark.add(new Tuple<>("002", this.map[13][0]));
        cell_vuMark.add(new Tuple<>("002", this.map[14][0]));
        cell_vuMark.add(new Tuple<>("003", this.map[14][4]));
        cell_vuMark.add(new Tuple<>("003", this.map[14][3]));
        cell_vuMark.add(new Tuple<>("004", this.map[14][6]));
        cell_vuMark.add(new Tuple<>("004", this.map[14][5]));
        cell_vuMark.add(new Tuple<>("005", this.map[11][9]));
        cell_vuMark.add(new Tuple<>("005", this.map[10][9]));
        cell_vuMark.add(new Tuple<>("006", this.map[9][9]));
        cell_vuMark.add(new Tuple<>("006", this.map[8][9]));
        cell_vuMark.add(new Tuple<>("007", this.map[6][9]));
        cell_vuMark.add(new Tuple<>("007", this.map[5][9]));
        cell_vuMark.add(new Tuple<>("008", this.map[3][9]));
        cell_vuMark.add(new Tuple<>("008", this.map[4][9]));
        cell_vuMark.add(new Tuple<>("009", this.map[0][3]));
        cell_vuMark.add(new Tuple<>("009", this.map[0][4]));
        cell_vuMark.add(new Tuple<>("010", this.map[0][5]));
        cell_vuMark.add(new Tuple<>("010", this.map[0][6]));
        cell_vuMark.add(new Tuple<>("011", this.map[0][7]));
        cell_vuMark.add(new Tuple<>("011", this.map[0][8]));
        cell_vuMark.add(new Tuple<>("012", this.map[0][9]));
        cell_vuMark.add(new Tuple<>("012", this.map[0][10]));
        cell_vuMark.add(new Tuple<>("013", this.map[0][11]));
        cell_vuMark.add(new Tuple<>("013", this.map[0][12]));
        cell_vuMark.add(new Tuple<>("014", this.map[2][13]));
        cell_vuMark.add(new Tuple<>("014", this.map[3][13]));
        cell_vuMark.add(new Tuple<>("015", this.map[4][13]));
        cell_vuMark.add(new Tuple<>("015", this.map[5][13]));
        Data.setCells_VuMarks(cell_vuMark);
    }

    /**
     * Method to relate matrix cells with Locations ids
     */
    private void RelateCellsWithLocations()
    {
        ArrayList<Integer> lid = new ArrayList<>();
        lid.add(1);
        this.map[14][6].setLocationsId(lid);
        lid = new ArrayList<>();
        lid.add(2);
        this.map[14][5].setLocationsId(lid);
        lid = new ArrayList<>();
        lid.add(3);
        this.map[0][11].setLocationsId(lid);
        lid = new ArrayList<>();
        lid.add(4);
        this.map[5][13].setLocationsId(lid);
    }
}
