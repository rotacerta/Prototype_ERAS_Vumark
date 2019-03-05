package com.vuforia.Enums;

public enum Streets
{
    STREET_1_1(
            "Rua 1", 14, 2, 0, 0, 0
    ),
    STREET_1_2(
            "Rua 1", 13, 4, -0.5f, 0, 90
    ),
    STREET_1_3(
            "Rua 1", 17, 4, -0.5f, 0, 90
    ),
    STREET_2(
            "Rua 2", 13, 8, 0, 0, 0
    ),
    STREET_3(
            "Rua 3", 13, 12, 0, 0, 0
    ),
    STREET_4(
            "Rua 4", 7, 17, 0, -0.5f, 0
    ),
    STREET_5(
            "Rua 5", 26, 7, -0.5f, 0, 90
    );

    public String TEXT;
    public int XCOORD;
    public int YCOORD;
    public float XMOD;
    public float YMOD;
    public float ROTATION;

    Streets(String text, int xCoord, int yCoord, float xMod, float yMod, float rotation)
    {
        TEXT = text;
        XCOORD = xCoord;
        YCOORD = yCoord;
        XMOD = xMod;
        YMOD = yMod;
        ROTATION = rotation;
    }
}
