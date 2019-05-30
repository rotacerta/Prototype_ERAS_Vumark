package com.vuforia.Enums;

public enum HttpConnectionMethodEnum
{
    GET(0), POST(1);

    public int Value;

    HttpConnectionMethodEnum(int value)
    {
        Value = value;
    }
}
