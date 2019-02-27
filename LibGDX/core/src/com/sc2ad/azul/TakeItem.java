package com.sc2ad.azul;

public class TakeItem<T, K> {
    public T t;
    public K k;
    public TakeItem(T t) {
        this.t = t;
    }
    public TakeItem(T t, K k) {
        this.t = t;
        this.k = k;
    }
}
