package com.vuforia.Enums;

public enum StreetsEnum
{
    STREET_1_1(
            "Rua 1", 15, 1, 0, 4, 0
    ),
    STREET_1_2(
            "Rua 1", 14, 3, 0.5f, 3.5f, 90
    ),
    STREET_1_3(
            "Rua 1", 18, 3, 0.3f, 3.5f, 90
    ),
    STREET_2(
            "Rua 2", 16, 8, 0, 1, 0
    ),
    STREET_3(
            "Rua 3", 16, 11, 0, 2, 0
    ),
    STREET_4(
            "Rua 4", 11, 15, 0, 2, 0
    ),
    STREET_5(
            "Rua 5", 24, 7, 0.5f, 1, 90
    );

    public String TEXT;
    public int XCOORD;
    public int YCOORD;
    public float XMOD;
    public float YMOD;
    public float ROTATION;

    StreetsEnum(String text, int xCoord, int yCoord, float xMod, float yMod, float rotation)
    {
        TEXT = text;
        XCOORD = xCoord;
        YCOORD = yCoord;
        XMOD = xMod;
        YMOD = yMod;
        ROTATION = rotation;
    }
}
