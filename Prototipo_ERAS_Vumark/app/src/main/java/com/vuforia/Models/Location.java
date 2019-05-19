package com.vuforia.Models;

import java.util.Locale;

public class Location
{
    private int LocationId;
    private int Structure;
    private int Street;
    private int Building;
    private int Flat;
    private String VuMerkId;

    public Location(int locationId, int structure, int street, int building, int flat, String vuMerkId)
    {
        LocationId = locationId;
        Structure = structure;
        Street = street;
        Building = building;
        Flat = flat;
        VuMerkId = vuMerkId;
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

    public String getVuMerkId()
    {
        return VuMerkId;
    }

    public String ToString()
    {
        return String.format(Locale.getDefault() , "%d.%d.%d.%d", Structure, Street, Building, Flat);
    }
}
