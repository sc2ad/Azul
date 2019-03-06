package com.sc2ad.azul.util;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Util {
    public static double distance(float x, float y, float x2, float y2) {
        return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
    }
    public static double distance(int x, int y, int x2, int y2) {
        return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
    }
    public static double distance(Sprite s, Sprite s2) {
        return distance(s.getX(), s.getY(), s2.getX(), s2.getY());
    }
}
