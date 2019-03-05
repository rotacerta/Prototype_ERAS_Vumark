package com.vuforia.Service;

import android.annotation.SuppressLint;

import com.vuforia.Enums.CellValueEnum;
import com.vuforia.Models.Cell;
import com.vuforia.Util.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathFinder {
    private Cell nextCell;
    private Cell[][] matrix;
    private Cell currentCell;
    private Cell nextdestination;
    public final int LineLength = 14;
    public final int ColumnLength = 15;
    public final ArrayList<String> errors;
    private final boolean DIAGONALLY = true;
    private final ArrayList<Cell> destinations;

    /**
     * Constructor
     * @param currentCell The current cell that the user is
     * @param destinations An ArrayList filled with all destinations
     */
    public PathFinder(Tuple<Integer, Integer> currentCell, ArrayList<Tuple<Integer, Integer>> destinations)
    {
        this.matrix = InitMatrix(LineLength, ColumnLength);
        this.currentCell = GetStatPoint(currentCell);
        this.nextdestination = new Cell();
        FillMatrixObstacles();
        this.destinations = SortDestinations(this.currentCell, GetDestinations(destinations));
        if(this.destinations.size() > 0)
        {
            this.nextdestination = this.destinations.get(0);
        }
        this.errors = SetCurrentPoint(currentCell);
    }

    /**
     * Method to init the matrix map
     * @param lines Lines of the matrix
     * @param columns Columns of the matrix
     * @return The matrix initialized
     */
    private Cell[][] InitMatrix(int lines, int columns)
    {
        Cell[][] _matrix = new Cell[lines][columns];
        for (int i = 0; i < lines; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                _matrix[i][j] = initCell( i, j );
            }
        }
        return _matrix;
    }

    /**
     * Temporary method to fill the matrix with obstacles
     */
    private void FillMatrixObstacles()
    {
        //TODO: Delete this method when the API be completes
        ArrayList<Tuple<Integer, Integer>> obstacles = new ArrayList<>();
        obstacles.add(new Tuple<>(0, 0));
        obstacles.add(new Tuple<>(1, 0));
        obstacles.add(new Tuple<>(2, 0));
        obstacles.add(new Tuple<>(3, 0));
        obstacles.add(new Tuple<>(4, 0));
        obstacles.add(new Tuple<>(5, 0));
        obstacles.add(new Tuple<>(6, 0));
        obstacles.add(new Tuple<>(7, 0));
        obstacles.add(new Tuple<>(8, 0));
        obstacles.add(new Tuple<>(9, 0));
        obstacles.add(new Tuple<>(12, 0));
        obstacles.add(new Tuple<>(11, 0));
        obstacles.add(new Tuple<>(10, 0));
        obstacles.add(new Tuple<>(0, 11));
        obstacles.add(new Tuple<>(0, 1));
        obstacles.add(new Tuple<>(0, 2));
        obstacles.add(new Tuple<>(0, 3));
        obstacles.add(new Tuple<>(0, 4));
        obstacles.add(new Tuple<>(0, 5));
        obstacles.add(new Tuple<>(0, 6));
        obstacles.add(new Tuple<>(0, 7));
        obstacles.add(new Tuple<>(0, 8));
        obstacles.add(new Tuple<>(0, 9));
        obstacles.add(new Tuple<>(0, 10));
        obstacles.add(new Tuple<>(2, 3));
        obstacles.add(new Tuple<>(6, 3));
        obstacles.add(new Tuple<>(5, 3));
        obstacles.add(new Tuple<>(4, 3));
        obstacles.add(new Tuple<>(3, 3));
        obstacles.add(new Tuple<>(2, 6));
        obstacles.add(new Tuple<>(2, 4));
        obstacles.add(new Tuple<>(2, 5));
        obstacles.add(new Tuple<>(6, 6));
        obstacles.add(new Tuple<>(6, 4));
        obstacles.add(new Tuple<>(6, 5));
        obstacles.add(new Tuple<>(3, 6));
        obstacles.add(new Tuple<>(4, 6));
        obstacles.add(new Tuple<>(5, 6));
        obstacles.add(new Tuple<>(9, 3));
        obstacles.add(new Tuple<>(12, 3));
        obstacles.add(new Tuple<>(12, 6));
        obstacles.add(new Tuple<>(9, 6));
        obstacles.add(new Tuple<>(9, 4));
        obstacles.add(new Tuple<>(9, 5));
        obstacles.add(new Tuple<>(12, 4));
        obstacles.add(new Tuple<>(12, 5));
        obstacles.add(new Tuple<>(3, 8));
        obstacles.add(new Tuple<>(11, 8));
        obstacles.add(new Tuple<>(10, 8));
        obstacles.add(new Tuple<>(9, 8));
        obstacles.add(new Tuple<>(8, 8));
        obstacles.add(new Tuple<>(7, 8));
        obstacles.add(new Tuple<>(6, 8));
        obstacles.add(new Tuple<>(5, 8));
        obstacles.add(new Tuple<>(4, 8));
        obstacles.add(new Tuple<>(2, 12));
        obstacles.add(new Tuple<>(5, 12));
        obstacles.add(new Tuple<>(4, 12));
        obstacles.add(new Tuple<>(3, 12));
        obstacles.add(new Tuple<>(9, 12));
        obstacles.add(new Tuple<>(12, 12));
        obstacles.add(new Tuple<>(11, 12));
        obstacles.add(new Tuple<>(10, 12));
        obstacles.add(new Tuple<>(9, 14));
        obstacles.add(new Tuple<>(10, 14));
        obstacles.add(new Tuple<>(11, 14));
        obstacles.add(new Tuple<>(12, 14));
        for(Tuple<Integer, Integer> obstacle: obstacles)
        {
            this.matrix[obstacle.key][obstacle.value].setValue(CellValueEnum.OBSTACLE.Value);
        }
    }

    /**
     * Method to init a Cell object
     * @param x Position X of the Cell in matrix
     * @param y Position Y of the Cell in matrix
     * @return A new instance of a Cell object
     */
    private Cell initCell(int x, int y)
    {
        return new Cell( null, 0, 0, 0, false, CellValueEnum.WALKABLE.Value, x, y );
    }

    /**
     * Method to get a cell by a Tuple object
     * @param position A tuple object with the position of the cell that you want to get
     * @return <code>null</code> if the cell was not found; <code>Cell</code> if the cell was found
     */
    private Cell GetCellByTuple(Tuple<Integer, Integer> position)
    {
        if (((position.key >=0) && (position.key < LineLength)) && ((position.value >= 0) && (position.value < ColumnLength)))
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
        for(Tuple<Integer, Integer> destin: destinations)
        {
            Cell cellDestin = GetCellByTuple(destin);
            if(cellDestin != null)
            {
                cellDestin.setValue(CellValueEnum.ENDPOINT.Value);
                _destinations.add(cellDestin);
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
     * @param _matrix The matrix to clear
     * @return The matrix cleaned
     */
    private Cell[][] CleanMatrix(Cell[][] _matrix)
    {
        for (int i = 0; i < LineLength; i++)
        {
            for (int j = 0; j < ColumnLength; j++)
            {
                _matrix[i][j].setFcost(0);
                _matrix[i][j].setGcost(0);
                _matrix[i][j].setHcost(0);
                _matrix[i][j].setWay(false);
                _matrix[i][j].setFather(null);
            }
        }
        return _matrix;
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
                    CleanMatrix(matrix);
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
                if (destinations.size() > 0)
                {
                    nextdestination = this.destinations.get(0);
                }
                else
                {
                    nextdestination = null;
                }
            }
            if (nextdestination != null)
            {
                CleanMatrix(matrix);
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
                if ((neighbor.getValue() == CellValueEnum.OBSTACLE.Value) || (AnyMatchInList(closedList, neighbor)))
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
        if (father.getX() + 1 < LineLength)
        {
            neighbors.add(matrix[father.getX() + 1][father.getY()]);
            if (father.getY() + 1 < ColumnLength)
            {
                if ((matrix[father.getX()][father.getY() + 1].getValue() != CellValueEnum.OBSTACLE.Value || diagonally) && (matrix[father.getX() + 1][father.getY()].getValue() != CellValueEnum.OBSTACLE.Value || diagonally))
                {
                    neighbors.add(matrix[father.getX() + 1][father.getY() + 1]);
                }
            }
            if ((father.getY() - 1 < ColumnLength) && (father.getY() - 1 >= 0))
            {
                if ((matrix[father.getX()][father.getY() - 1].getValue() != CellValueEnum.OBSTACLE.Value || diagonally) && (matrix[father.getX() + 1][father.getY()].getValue() != CellValueEnum.OBSTACLE.Value || diagonally))
                {
                    neighbors.add(matrix[father.getX() + 1][father.getY() - 1]);
                }
            }
        }
        if (father.getY() + 1 < ColumnLength)
        {
            neighbors.add(matrix[father.getX()][father.getY() + 1]);
        }
        if ((father.getY() - 1 < ColumnLength) && (father.getY() - 1 >= 0))
        {
            neighbors.add(matrix[father.getX()][father.getY() - 1]);
        }
        if ((father.getX() - 1 < LineLength) && (father.getX() - 1 >= 0))
        {
            neighbors.add(matrix[father.getX() - 1][father.getY()]);
            if (father.getY() + 1 < ColumnLength)
            {
                if ((matrix[father.getX()][father.getY() + 1].getValue() != CellValueEnum.OBSTACLE.Value || diagonally) && (matrix[father.getX() - 1][father.getY()].getValue() != CellValueEnum.OBSTACLE.Value || diagonally))
                {
                    neighbors.add(matrix[father.getX() - 1][father.getY() + 1]);
                }
            }
            if ((father.getY() - 1 < ColumnLength) && (father.getY() - 1 >= 0))
            {
                if ((matrix[father.getX()][father.getY() - 1].getValue() != CellValueEnum.OBSTACLE.Value || diagonally) && (matrix[father.getX() - 1][father.getY()].getValue() != CellValueEnum.OBSTACLE.Value || diagonally))
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
}
