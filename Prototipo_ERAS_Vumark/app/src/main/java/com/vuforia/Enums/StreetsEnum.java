package com.vuforia.Enums;

public enum StreetsEnum
{
    STREET_1_1(
            "Rua 1", 16, 2, 0, 4, 0
    ),
    STREET_1_2(
            "Rua 1", 16, 4, 0, 3, 90
    ),
    STREET_1_3(
            "Rua 1", 20, 4, 0, 3, 90
    ),
    STREET_2(
            "Rua 2", 16, 9, 0, 1, 0
    ),
    STREET_3(
            "Rua 3", 16, 13, 0, 1, 0
    ),
    STREET_4(
            "Rua 4", 11, 18, 0, 0, 0
    ),
    STREET_5(
            "Rua 5", 27, 9, -0.2f, 0, 90
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
