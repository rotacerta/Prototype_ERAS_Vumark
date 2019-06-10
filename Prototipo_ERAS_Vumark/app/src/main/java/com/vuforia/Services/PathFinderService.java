package com.vuforia.Services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Enums.DirectionEnum;
import com.vuforia.Enums.Map.MapDefinitionsEnum;
import com.vuforia.Enums.OrientationEnum;
import com.vuforia.Models.Cell;
import com.vuforia.Util.Tuple;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathFinderService
{
    private Cell nextCell;
    private DirectionEnum nextDirection;
    private Cell[][] matrix;
    private Cell currentCell;
    private Cell nextdestination;
    private final int MatrixRows;
    private final int MatrixColumns;
    private final ArrayList<String> errors;
    private final boolean DIAGONALLY = true;
    private ArrayList<Cell> destinations;

    /**
     * Constructor
     * @param currentCell The current cell that the user is
     * @param destinations An ArrayList filled with all destinations
     */
    public PathFinderService(Tuple<Integer, Integer> currentCell, ArrayList<Tuple<Integer, Integer>> destinations)
    {
        MatrixRows = MapDefinitionsEnum.ROWS.Value;
        MatrixColumns = MapDefinitionsEnum.COLUMNS.Value;

        MapService mapService = new MapService();
        this.matrix = mapService.GetMap();

        this.currentCell = GetStatPoint(currentCell);
        this.nextdestination = new Cell();
        this.destinations = SortDestinations(this.currentCell, GetDestinations(destinations));
        if(this.destinations.size() > 0)
        {
            this.nextdestination = this.destinations.get(0);
        }
        this.errors = SetCurrentPoint(currentCell);
    }

    public DirectionEnum GetNextDirection() {
        return nextDirection;
    }

    /**
     * Method to get a cell by a Tuple object
     * @param position A tuple object with the position of the cell that you want to get
     * @return <code>null</code> if the cell was not found; <code>Cell</code> if the cell was found
     */
    private Cell GetCellByTuple(Tuple<Integer, Integer> position)
    {
        if ((position != null) && ((position.key >=0) && (position.key < MatrixRows)) && ((position.value >= 0) && (position.value < MatrixColumns)))
        {
            return matrix[position.key][position.value];
        }
        return null;
    }

    /**
     * Method to get an ArrayList of Cell with destinations
     * @param destinations An ArrayList with Tuples of destionations
     * @return ArrayList with Cell destinations
     */
    private ArrayList<Cell> GetDestinations(ArrayList<Tuple<Integer, Integer>> destinations)
    {
        ArrayList<Cell> _destinations = new ArrayList<>();
        if(destinations != null && destinations.size() > 0)
        {
            for(Tuple<Integer, Integer> destin: destinations)
            {
                Cell cellDestin = GetCellByTuple(destin);
                if(cellDestin != null)
                {
                    cellDestin.setValue(CellValueEnum.ENDPOINT.Value);
                    _destinations.add(cellDestin);
                }
            }
        }
        return _destinations;
    }

    /**
     * @return The list with the destinations sorted
     */
    public ArrayList<Cell> GetDestinations()
    {
        return this.destinations;
    }

    /**
     * Method to get a Cell object that be the start point of the path
     * @param position A tuple object with the position of the cell on metrix
     * @return <code>null</code> if the cell was not found; <code>Cell</code> if the cell was found
     */
    private Cell GetStatPoint(Tuple<Integer, Integer> position)
    {
        return GetCellByTuple(position);
    }

    /**
     * Method to clear all properties of each cell that not is an obstacle in matrix
     */
    private void CleanMatrix()
    {
        for (int i = 0; i < MatrixRows; i++)
        {
            for (int j = 0; j < MatrixColumns; j++)
            {
                matrix[i][j].setFcost(0);
                matrix[i][j].setGcost(0);
                matrix[i][j].setHcost(0);
                matrix[i][j].setWay(false);
                matrix[i][j].setFather(null);
            }
        }
    }

    /**
     * Method to sort an ArrayList by the shortest distance between each item
     * @param startPoint A cell object that is the starting point
     * @param _destinations An ArrayList of Cells with all destinations
     * @return An ArrayList with all destinations sorted by the shortest distance between them
     */
    private ArrayList<Cell> SortDestinations(Cell startPoint, ArrayList<Cell> _destinations)
    {
        ArrayList<Cell> destinationsSorted = new ArrayList<>();
        if((startPoint != null) && (_destinations != null && _destinations.size() > 0))
        {
            if (_destinations.size() == 1)
            {
                destinationsSorted.add(_destinations.get(0));
            }
            else
            {
                Cell destinationWithLeastCost = null;
                ArrayList<Tuple<Tuple<Integer, Integer>, Integer>> destinationsCost = new ArrayList<>();
                for(Cell destination: _destinations)
                {
                    if(FindPath(matrix, startPoint, destination))
                    {
                        destinationsCost.add(
                                new Tuple<>(new Tuple<>(destination.getX(), destination.getY()),
                                        matrix[destination.getX()][destination.getY()].getFcost()));
                    }
                    CleanMatrix();
                }
                if(destinationsCost.size() > 0)
                {
                    Collections.sort(destinationsCost, new Comparator<Tuple<Tuple<Integer, Integer>, Integer>>() {
                        @Override
                        public int compare(Tuple<Tuple<Integer, Integer>, Integer> o1, Tuple<Tuple<Integer, Integer>, Integer> o2) {
                            return o1.value - o2.value;
                        }
                    });
                    for(Cell obj: _destinations)
                    {
                        if(obj.getX() == destinationsCost.get(0).key.key && obj.getY() == destinationsCost.get(0).key.value)
                        {
                            destinationWithLeastCost = obj;
                            break;
                        }
                    }
                    destinationsSorted.add(destinationWithLeastCost);
                    _destinations.remove(destinationWithLeastCost);
                    destinationsSorted.addAll(SortDestinations(destinationWithLeastCost, _destinations));
                }
            }
        }
        return destinationsSorted;
    }

    /**
     * Method to set the current point of the user
     * @param currentCell A Tuple that represents the current position of the user
     * @return An ArrayList of string with errors that occurred during execution of the algorithm, or an empty ArrayList
     */
    @SuppressLint("DefaultLocale")
    public ArrayList<String> SetCurrentPoint(Tuple<Integer, Integer> currentCell)
    {
        ArrayList<String> errors = new ArrayList<>();
        this.currentCell = GetStatPoint(currentCell);
        if(this.currentCell != null)
        {
            if (this.currentCell.Equals(nextdestination))
            {
                this.destinations.remove(nextdestination);

                nextDirection = DirectionEnum.CHECKED;

                if (destinations.size() > 0)
                {
                    nextdestination = this.destinations.get(0);
                }
                else
                {
                    nextdestination = null;
                    nextDirection = null;
                }
            }
            else if(IsDestination(this.currentCell)) {
                this.destinations.remove(this.currentCell);
                this.destinations = SortDestinations(this.currentCell, this.destinations);
                if(this.destinations.size() > 0)
                {
                    this.nextdestination = this.destinations.get(0);
                }
                else{
                    nextdestination = null;
                    nextDirection = null;
                }
            }
            else if(!this.currentCell.Equals(this.nextCell)) {
                this.destinations = SortDestinations(this.currentCell, this.destinations);
                if(this.destinations.size() > 0)
                {
                    this.nextdestination = this.destinations.get(0);
                }
                else
                {
                    nextdestination = null;
                }
            }
            if (nextdestination != null)
            {
                CleanMatrix();
                if (FindPath(this.matrix, this.currentCell, this.nextdestination))
                {
                    this.matrix = HighlightPath(this.matrix, this.nextdestination);
                    ArrayList<Cell> neighbors = FindNeighbors(this.currentCell, DIAGONALLY);
                    this.nextCell = null;
                    for(Cell cell: neighbors)
                    {
                        if(cell.isWay())
                        {
                            this.nextCell = cell;
                            DefineDirection();
                            break;
                        }
                    }
                    if (this.nextCell == null)
                    {
                        errors.add("Não foi possível encontrar uma das células;");
                    }
                }
                else
                {
                    errors.add(String.format("O caminho entre [%d,%d] e [%d,%d] não foi encontrado;", this.currentCell.getX(), this.currentCell.getY(), this.nextdestination.getX(), this.nextdestination.getY()));
                }
            }
            else
            {
                nextCell = null;
            }
        }
        else
        {
            errors.add(String.format("Não foi possível encontrar a célula [%d,%d];", currentCell.key, currentCell.value));
        }
        return errors;
    }

    private void DefineDirection() {
        if (currentCell == null || nextCell == null) return;

        if (isHorizontal(nextCell)) {
            nextDirection = DirectionEnum.BACK;
        } else {
            nextDirection = DefineDirectionByDiagonal();
        }
    }

    private DirectionEnum DefineDirectionByDiagonal() {
        DirectionEnum directionEnum = null;
        OrientationEnum orientation = currentCell.getOrientation();

        if (orientation != null) {
            switch (orientation) {
                case LEST:
                    directionEnum = getNextDirectionInLest(nextCell);
                    break;
                case NORTH:
                    directionEnum = getNextDirectionInNorth(nextCell);
                    break;
                case SOUTH:
                    directionEnum = getNextDirectionInSouth(nextCell);
                    break;
                case WEST:
                    directionEnum = getNextDirectionInWest(nextCell);
                    break;
            }
        }

        return directionEnum;
    }

    @Nullable
    private DirectionEnum getNextDirectionInLest(Cell cell) {
        boolean isDiagonal = false;

        if (currentCell.getY() > cell.getY()) {
            isDiagonal = true;
        }

        if (currentCell.getX() < cell.getX()) {
            return (isDiagonal)? DirectionEnum.DIAGONAL_LEFT : DirectionEnum.LEFT;
        } else if (currentCell.getX() > cell.getX()) {
            return (isDiagonal)? DirectionEnum.DIAGONAL_RIGHT : DirectionEnum.RIGHT;
        }
        return null;
    }

    @Nullable
    private DirectionEnum getNextDirectionInWest(Cell cell) {
        boolean isDiagonal = false;

        if (currentCell.getY() < cell.getY()) {
            isDiagonal = true;
        }

        if (currentCell.getX() < cell.getX()) {
            return (isDiagonal) ? DirectionEnum.DIAGONAL_RIGHT : DirectionEnum.RIGHT;
        } else if (currentCell.getX() > cell.getX()) {
            return (isDiagonal) ? DirectionEnum.DIAGONAL_LEFT : DirectionEnum.LEFT;
        }
        return null;
    }

    @Nullable
    private DirectionEnum getNextDirectionInNorth(Cell cell) {
        if (currentCell.getY() > cell.getY()) {
            return DirectionEnum.RIGHT;
        } else if (currentCell.getY() < cell.getY()) {
            return DirectionEnum.LEFT;
        }
        return null;
    }

    @Nullable
    private DirectionEnum getNextDirectionInSouth(Cell cell) {
        if (currentCell.getY() < cell.getY()) {
            return DirectionEnum.RIGHT;
        } else if (currentCell.getY() > cell.getY()) {
            return DirectionEnum.LEFT;
        }
        return null;
    }

    private boolean isHorizontal(Cell cell) {
        return (currentCell.getX() == cell.getX() || currentCell.getY() == cell.getY())
                && currentCell.getOrientation() != cell.getOrientation();
    }

    /**
     * @return currentCell
     */
    public Cell GetCurrentCell()
    {
        return this.currentCell;
    }

    /**
     * @return nextCell
     */
    public Cell GetNextCell()
    {
        return this.nextCell;
    }

    /**
     * @return nextdestination
     */
    public Cell GetNextdestination()
    {
        return this.nextdestination;
    }

    /**
     * @return An ArrayList with erros strings
     */
    public ArrayList<String> GetErrors()
    {
        return errors;
    }

    /**
     * Method to find the short path between two points
     * @param matrix A matrix that represents a map
     * @param startPoint A Cell object that is the start point of the path
     * @param endPoint A Cell object that is the end point of the path
     * @return An boolean that represents of the algorithm found the path
     */
    private boolean FindPath(Cell[][] matrix, Cell startPoint, Cell endPoint)
    {
        Cell current;
        boolean found = false;
        ArrayList<Cell> openList = new ArrayList<>();
        ArrayList<Cell> closedList = new ArrayList<>();
        openList.add(startPoint);
        do
        {
            current = openList.get(0);
            if (current.Equals(endPoint) && current.getValue() == CellValueEnum.ENDPOINT.Value)
            {
                found = true;
                continue;
            }
            if (openList.isEmpty())
            {
                continue;
            }
            openList.remove(0);
            closedList.add(current);
            ArrayList<Cell> neighborsOfCurrentCell = FindNeighbors(current, this.DIAGONALLY);
            for (Cell neighbor : neighborsOfCurrentCell)
            {
                if ((IsObstacle(neighbor)) || (AnyMatchInList(closedList, neighbor)))
                {
                    continue;
                }
                if (!AnyMatchInList(openList, neighbor))
                {
                    matrix[neighbor.getX()][neighbor.getY()].setFather(current);
                    matrix[neighbor.getX()][neighbor.getY()].setHcost(CalcuLateHCost(endPoint, neighbor));
                    matrix[neighbor.getX()][neighbor.getY()].setGcost(CalcuLateGCost(current, neighbor));
                    matrix[neighbor.getX()][neighbor.getY()].setFcost(matrix[neighbor.getX()][neighbor.getY()].getHcost() + matrix[neighbor.getX()][neighbor.getY()].getGcost());
                    openList.add(neighbor);
                }
                else
                {
                    int newGCost = CalcuLateGCost(current, neighbor);
                    if (newGCost <= matrix[neighbor.getX()][neighbor.getY()].getGcost())
                    {
                        matrix[neighbor.getX()][neighbor.getY()].setGcost(newGCost);
                        matrix[neighbor.getX()][neighbor.getY()].setFcost(matrix[neighbor.getX()][neighbor.getY()].getHcost() + matrix[neighbor.getX()][neighbor.getY()].getGcost());
                        matrix[neighbor.getX()][neighbor.getY()].setFather(current);
                    }
                }
            }
            Collections.sort(openList);
        } while ((!found) && (!openList.isEmpty()));
        return found;
    }

    /**
     * Method to find the neighbors of a father
     * @param father The cell that you want to get the neighbors
     * @param diagonally <code> true </ code> if the algorithm should consider walk diagonally with obstacles
     * @return An ArrayList with the neighbors of the father
     */
    private ArrayList<Cell> FindNeighbors(Cell father, boolean diagonally)
    {
        ArrayList<Cell> neighbors = new ArrayList<>();
        if (father.getX() + 1 < MatrixRows)
        {
            neighbors.add(matrix[father.getX() + 1][father.getY()]);
            if (father.getY() + 1 < MatrixColumns)
            {
                if (((!IsObstacle(matrix[father.getX()][father.getY() + 1])) || diagonally) && ((!IsObstacle(matrix[father.getX() + 1][father.getY()])) || diagonally))
                {
                    neighbors.add(matrix[father.getX() + 1][father.getY() + 1]);
                }
            }
            if ((father.getY() - 1 < MatrixColumns) && (father.getY() - 1 >= 0))
            {
                if (((!IsObstacle(matrix[father.getX()][father.getY() - 1])) || diagonally) && ((!IsObstacle(matrix[father.getX() + 1][father.getY()])) || diagonally))
                {
                    neighbors.add(matrix[father.getX() + 1][father.getY() - 1]);
                }
            }
        }
        if (father.getY() + 1 < MatrixColumns)
        {
            neighbors.add(matrix[father.getX()][father.getY() + 1]);
        }
        if ((father.getY() - 1 < MatrixColumns) && (father.getY() - 1 >= 0))
        {
            neighbors.add(matrix[father.getX()][father.getY() - 1]);
        }
        if ((father.getX() - 1 < MatrixRows) && (father.getX() - 1 >= 0))
        {
            neighbors.add(matrix[father.getX() - 1][father.getY()]);
            if (father.getY() + 1 < MatrixColumns)
            {
                if (((!IsObstacle(matrix[father.getX()][father.getY() + 1])) || diagonally) && ((!IsObstacle(matrix[father.getX() - 1][father.getY()])) || diagonally))
                {
                    neighbors.add(matrix[father.getX() - 1][father.getY() + 1]);
                }
            }
            if ((father.getY() - 1 < MatrixColumns) && (father.getY() - 1 >= 0))
            {
                if (((!IsObstacle(matrix[father.getX()][father.getY() - 1])) || diagonally) && ((!IsObstacle(matrix[father.getX() - 1][father.getY()])) || diagonally))
                {
                    neighbors.add(matrix[father.getX() - 1][father.getY() - 1]);
                }
            }
        }
        return neighbors;
    }

    /**
     * Method to calculate the HCost of the cell (distance between the current cell and the end point)
     * @param endPoint The end point of the path
     * @param current The current cell that the algorithm is analyzing
     * @return An Integer that represents the HCost
     */
    private int CalcuLateHCost(Cell endPoint, Cell current)
    {
        int sum = 0;
        sum += Math.abs(endPoint.getX() - current.getX());
        sum += Math.abs(endPoint.getY() - current.getY());
        return sum;
    }

    /**
     * Method to celculate the GCost of the cell (distance between the current cell and the start point)
     * @param father The current cell that the algorithm is analyzing
     * @param neighbor neighbor of the father Cell
     * @return An Integer that represents the GCost
     */
    private int CalcuLateGCost(Cell father, Cell neighbor)
    {
        int sum;
        if ((father.getX() != neighbor.getX()) && (father.getY() != neighbor.getY()))
        {
            sum = father.getGcost() + 14;
        }
        else
        {
            sum = father.getGcost() + 10;
        }
        return sum;
    }

    /**
     * Method to highlight the path
     * @param _matrix The metrix after the algorithm finds the path
     * @param cell The end point of the path
     * @return The matrix with the path highlighted
     */
    private Cell[][] HighlightPath(Cell[][] _matrix, Cell cell)
    {
        while ((cell != null) && (cell.getValue() != CellValueEnum.STARTPOINT.Value))
        {
            _matrix[cell.getX()][cell.getY()].setWay(true);
            cell = _matrix[cell.getX()][cell.getY()].getFather();
        }
        return _matrix;
    }

    /**
     * Method to find any match of object in some list
     * @param list The list that you want to search the object
     * @param obj The object that you want to find in list
     * @return <code>true</code> if the object was found; <code>false</code> if not
     */
    private boolean AnyMatchInList(List<Cell> list, Cell obj)
    {
        for(Cell element: list)
        {
            if(element.Equals(obj))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if a cell is a destination
     * @param cell cell to check
     * @return <code>true</code> if the cell is a destination; <code>false</code> if not
     */
    private boolean IsDestination(Cell cell)
    {
        for (Cell c: this.destinations)
        {
            if(cell.Equals(c))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if a cell is an obstacle (CellValueEnum == OBSTACLE || CellValueEnum == UNMAPPED)
     * @param cell cell to check
     * @return <code>true</code> if the cell is an obstacle; <code>false</code> if not
     */
    private boolean IsObstacle(Cell cell)
    {
        return ((cell.getValue() == CellValueEnum.OBSTACLE.Value) || (cell.getValue() == CellValueEnum.UNMAPPED.Value));
    }

    /**
     * @return Map
     */
    public Cell[][] GetMap()
    {
        return matrix;
    }
}
