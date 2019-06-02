package com.vuforia.Models;

import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.ArrayList;

public class List
{
    private int ListId;
    private String Name;
    private String Requester;
    private Time RunningTime;
    private ArrayList<Product> Products;

    public List(int listId, String name, String requester, Time runningTime, ArrayList<Product> products)
    {
        ListId = listId;
        Name = name;
        Requester = requester;
        RunningTime = runningTime;
        Products = products;
    }

    public int getListId()
    {
        return ListId;
    }

    public String getName()
    {
        return Name;
    }

    public String getRequester()
    {
        return Requester;
    }

    public Time getRunningTime()
    {
        return RunningTime;
    }

    @Nullable
    public Product getProductById(int pruductId)
    {
        if(Products != null && Products.size() > 0)
        {
            for (Product p: Products)
            {
                if(p.getProductId() == pruductId)
                {
                    return p;
                }
            }
        }
        return null;
    }

    public ArrayList<Product> getProducts()
    {
        return Products;
    }

    public ArrayList<Product> getProductsByLocationId(int locationId)
    {
        ArrayList<Product> products = getProducts();
        ArrayList<Product> productstoReturn = new ArrayList<>();
        if(products != null && products.size() > 0)
        {
            for(Product p: products)
            {
                if(p.getLocationId() == locationId)
                {
                    productstoReturn.add(p);
                }
            }
        }
        return productstoReturn;
    }

    public void setRequester(String requester)
    {
        Requester = requester;
    }

    public void setRunningTime(Time runningTime)
    {
        RunningTime = runningTime;
    }

    public void setProducts(ArrayList<Product> products)
    {
        Products = products;
    }

    public void setProduct(Product product)
    {
        if(product != null && getProductById(product.getProductId()) == null)
        {
            Products.add(product);
        }
    }
}
