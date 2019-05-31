package com.vuforia.Util;

import com.vuforia.Models.Cell;
import com.vuforia.Models.List;
import com.vuforia.Models.Location;
import com.vuforia.Services.PathFinderService;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Data
{
    private static PathFinderService PathFinderService;
    private static List ProductList;
    private static ArrayList<Location> Locations;
    private static ArrayList<Tuple<String, Cell>> Cells_VuMarks;
    private static ArrayList<Tuple<Integer, Tuple<Integer, Integer>>> Cells_Locations;

    public static void Init(PathFinderService pathFinderService, List products, ArrayList<Location> locations)
    {
        PathFinderService = pathFinderService;
        ProductList = products;
        Locations = locations;
    }

    public static String getAPIUrl()
    {
        return "http://192.168.15.6:80/pbpapi/api/values";
    }

    //region PFMethods
    @Contract(pure = true)
    public static PathFinderService getPathFinderService()
    {
        return PathFinderService;
    }

    public static void setPathFinderService(PathFinderService pathFinderService)
    {
        Data.PathFinderService = pathFinderService;
    }

    public static ArrayList<Cell> getDestinationsByLocationId(int locationId)
    {
        ArrayList<Cell> destinations = null;
        if(!IsNull(Data.PathFinderService))
        {
            destinations = Data.PathFinderService.GetDestinations();
            if(!IsNull(destinations) && destinations.size() > 0)
            {
                for(Cell c: destinations)
                {
                    ArrayList<Integer> lids = c.getLocationsId();
                    for (Integer id: c.getLocationsId())
                    {
                        if(id != locationId)
                        {
                            destinations.remove(c);
                        }
                    }
                }
            }
        }
        return destinations;
    }
    //endregion

    //region ListMethods
    @Contract(pure = true)
    public static List getProductList()
    {
        return ProductList;
    }

    public static void setProductList(List productList)
    {
        Data.ProductList = productList;
    }
    //endregion

    //region LocationsMethods
    @Contract(pure = true)
    public static ArrayList<Location> getLocations()
    {
        return Locations;
    }

    public static void setLocations(ArrayList<Location> locations)
    {
        Locations = locations;
    }

    public static boolean addLocation(Location location)
    {
        if(!IsNull(location) && !IsNull(Locations) && getLocationById(location.getLocationId()) == null)
        {
            Locations.add(location);
            return true;
        }
        return false;
    }

    @Nullable
    public static Location getLocationById(int locationId)
    {
        if(!IsNull(Locations) && Locations.size() > 0)
        {
            for(Location l: Locations)
            {
                if(l.getLocationId() == locationId)
                {
                    return l;
                }
            }
        }
        return null;
    }
    //endregion

    //region Cells_VuMarksMethods
    public static void setCells_VuMarks(ArrayList<Tuple<String, Cell>> cells_VuMarks)
    {
        Data.Cells_VuMarks = cells_VuMarks;
    }

    public static ArrayList<Cell> getCellsVumarkByVuMarkId(String vuMarkId)
    {
        ArrayList<Cell> cells = new ArrayList<>();
        if(Data.Cells_VuMarks != null && Data.Cells_VuMarks.size() > 0)
        {
            for(Tuple<String, Cell> r: Data.Cells_VuMarks)
            {
                if(r.key.equals(vuMarkId))
                {
                    cells.add(r.value);
                }
            }
        }
        return cells;
    }

    public static ArrayList<Cell> getCellsVumarkByCell(Cell cell)
    {
        return Data.getCellsVumarkByVuMarkId(getVumarkIdByCell(cell));
    }

    private static String getVumarkIdByCell(Cell cell)
    {
        if(Data.Cells_VuMarks != null && Data.Cells_VuMarks.size() > 0)
        {
            for (Tuple<String, Cell> r: Data.Cells_VuMarks)
            {
                if(r.value.Equals(cell))
                {
                    return r.key;
                }
            }
        }
        return null;
    }
    //endregion

    //region Cells_LocationsMethods
    private static void LoadCells_Locations()
    {
        Cells_Locations = new ArrayList<>();
        Cells_Locations.add(new Tuple<>(1, new Tuple<>(14, 6)));
        Cells_Locations.add(new Tuple<>(2, new Tuple<>(14, 5)));
        Cells_Locations.add(new Tuple<>(3, new Tuple<>(0, 11)));
        Cells_Locations.add(new Tuple<>(4, new Tuple<>(5, 13)));
    }

    public static ArrayList<Tuple<Integer, Tuple<Integer, Integer>>> getCells_Locations()
    {
        if(Cells_Locations == null || Cells_Locations.size() == 0)
            LoadCells_Locations();
        return Cells_Locations;
    }

    public static void setCells_Locations(ArrayList<Tuple<Integer, Tuple<Integer, Integer>>> cells_Locations)
    {
        Cells_Locations = cells_Locations;
    }

    public static ArrayList<Tuple<Integer, Integer>> getCellsByLocations(ArrayList<Integer> _locations)
    {
        ArrayList<Tuple<Integer, Integer>> _destinations = new ArrayList<>();
        if(_locations != null && _locations.size() > 0)
        {
            ArrayList<Tuple<Integer, Tuple<Integer, Integer>>> cellsTuple = Data.getCells_Locations();
            if(cellsTuple != null && cellsTuple.size() > 0)
            {
                for(Tuple<Integer, Tuple<Integer, Integer>> cellTuple: cellsTuple)
                {
                    for(int l: _locations)
                    {
                        if(l == cellTuple.key)
                            _destinations.add(cellTuple.value);
                    }
                }
            }
        }
        return _destinations;
    }
    //endregion

    @org.jetbrains.annotations.Contract(value = "null -> true; !null -> false", pure = true)
    private static boolean IsNull(Object obj)
    {
        return  obj == null;
    }
}
