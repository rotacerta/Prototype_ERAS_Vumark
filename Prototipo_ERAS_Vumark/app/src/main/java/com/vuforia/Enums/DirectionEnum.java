package com.vuforia.Enums;

public enum DirectionEnum
{
    BACK(0), LEFT(1), RIGHT(2), CHECKED(3);

    public int Value;

    DirectionEnum(int value)
    {
        Value = value;
    }
}