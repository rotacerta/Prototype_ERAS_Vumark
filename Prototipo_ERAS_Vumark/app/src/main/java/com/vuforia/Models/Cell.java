package com.vuforia.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable
{
    private int X;
    private int Y;
    private int Gcost;
    private int Hcost;
    private int Fcost;
    private int Value;
    private Cell Father;
    private boolean Way;
    private ArrayList<Integer> LocationsId;

    public Cell()
    {
        this.X = -1;
        this.Y = -1;
    }

    public Cell(Cell Father, int Gcost, int Fcost, int Hcost, boolean IsAWay, int Value, int X, int Y, ArrayList<Integer> LocationsId)
    {
        this.Gcost = Gcost;
        this.Hcost = Hcost;
        this.Fcost = Fcost;
        this.Value = Value;
        this.Father = Father;
        this.Way = IsAWay;
        this.X = X;
        this.Y = Y;
        this.LocationsId = LocationsId;
    }

    public int getY()
    {
        return Y;
    }

    public void setY(int Y)
    {
        this.Y = Y;
    }

    public int getGcost()
    {
        return Gcost;
    }

    public void setGcost(int Gcost)
    {
        this.Gcost = Gcost;
    }

    public int getHcost()
    {
        return Hcost;
    }

    public void setHcost(int Hcost)
    {
        this.Hcost = Hcost;
    }

    public int getFcost()
    {
        return Fcost;
    }

    public void setFcost(int Fcost)
    {
        this.Fcost = Fcost;
    }

    public int getValue()
    {
        return Value;
    }

    public void setValue(int Value)
    {
        this.Value = Value;
    }

    public Cell getFather()
    {
        return this.Father;
    }

    public void setFather(Cell Father)
    {
        this.Father = Father;
    }

    public boolean isWay()
    {
        return Way;
    }

    public void setWay(boolean Way)
    {
        this.Way = Way;
    }

    public int getX()
    {
        return X;
    }

    public void setX(int X)
    {
        this.X = X;
    }

    public ArrayList<Integer> getLocationsId()
    {
        return LocationsId;
    }

    public void setLocationsId(ArrayList<Integer> locationsId)
    {
        LocationsId = locationsId;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        int FCostToCompare =((Cell)o).getFcost();
        return this.Fcost - FCostToCompare;
    }

    public boolean Equals(Cell o)
    {
        try {return (this.X == o.getX()) && (this.Y == o.getY());}
        catch (Exception ignore){ return false; }
    }

    public boolean IsEmpty()
    {
        return ((this.X == -1) && (this.Y == -1));
    }
}
