package com.vuforia.Enums;

public enum OrientationEnum {

    NORTH(0), SOUTH(1), LEST(2), WEST(3);

    public int Value;

    OrientationEnum(int value)
    {
        Value = value;
    }
}