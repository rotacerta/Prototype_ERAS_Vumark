package com.vuforia.Util;

public class Tuple<K, V>
{
    public final K key;
    public final V value;

    public Tuple(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
}