package com.vuforia.Util;

import android.graphics.Region;

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
    private static List Products;
    private static ArrayList<Location> Locations;

    public static void Init(PathFinderService pathFinderService, List products, ArrayList<Location> locations)
    {
        PathFinderService = pathFinderService;
        Products = products;
        Locations = locations;
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
                    if(c.getLocationId() != locationId)
                    {
                        destinations.remove(c);
                    }
                }
            }
        }
        return destinations;
    }
    //endregion

    //region ListMethods
    @Contract(pure = true)
    public static List getProducts()
    {
        return Products;
    }

    public static void setProducts(List products)
    {
        Data.Products = products;
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

    @Nullable
    public static Location getLocationByVuMarkId(String vuMarkId)
    {
        if(!IsNull(Locations))
        {
            for(Location l : Locations)
            {
                if(l.getVuMerkId().equals(vuMarkId)){
                    return l;
                }
            }
        }
        return null;
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

    @org.jetbrains.annotations.Contract(value = "null -> true; !null -> false", pure = true)
    private static boolean IsNull(Object obj)
    {
        return  obj == null;
    }
}
