package com.vuforia.Enums;

public enum CellValueEnum
{
    WALKABLE(0), OBSTACLE(1), STARTPOINT(2), ENDPOINT(3), UNMAPPED(4);

    public int Value;

    CellValueEnum(int value)
    {
        Value = value;
    }
}
