package com.vuforia.Models;

import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.ArrayList;

public class List
{
    private int ListId;
    private String Name;
    private Time RunningTime;
    private ArrayList<Product> Products;

    public List(int listId, String name, Time runningTime, ArrayList<Product> products)
    {
        ListId = listId;
        Name = name;
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

    public Time getRunningTime()
    {
        return RunningTime;
    }

    @Nullable
    private Product getProductById(int pruductId)
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

    public Product[] getMockProducts()
    {
        Product[] products = new Product[4];
        products[0] = new Product(1, "Camisa CAI TEC INDUSTRIAL", 0, 35, 0);
        products[1] = new Product(1, "Computadores Usados", 0, 7, 0);
        products[2] = new Product(1, "Água mineral", 0, 55, 0);
        products[3] = new Product(1, "Detergente Neutro", 0, 15, 0);
        return products;
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
