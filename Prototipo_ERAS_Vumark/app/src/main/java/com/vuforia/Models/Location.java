package com.vuforia.Models;

import java.util.Locale;

public class Location
{
    private int LocationId;
    private int Structure;
    private int Street;
    private int Building;
    private int Flat;

    public Location(int locationId, int structure, int street, int building, int flat)
    {
        LocationId = locationId;
        Structure = structure;
        Street = street;
        Building = building;
        Flat = flat;
    }

    public int getLocationId()
    {
        return LocationId;
    }

    public int getStructure()
    {
        return Structure;
    }

    public int getStreet()
    {
        return Street;
    }

    public int getBuilding()
    {
        return Building;
    }

    public int getFlat()
    {
        return Flat;
    }

    public String ToString()
    {
        return String.format(Locale.getDefault() , "%d.%d.%d.%d", Structure, Street, Building, Flat);
    }
}
