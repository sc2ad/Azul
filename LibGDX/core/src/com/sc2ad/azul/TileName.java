package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum TileName {
    BLUE(0, Color.BLUE),
    YELLOW(1, Color.YELLOW),
    RED(2, Color.RED),
    BLACK(3, Color.BLACK),
    TEAL(4, Color.TEAL);

    private Texture texture;
    private int value;
    private Color c;
    TileName(Texture texture, int value) {
        this.texture = texture;
        this.value = value;
    }
    TileName(int value, Color c) {
        this.value = value;
        texture = new Texture("whitebox.png");
        this.c = c;
    }
    public Texture getTexture() {
        return texture;
    }
    public int getValue() {
        return value;
    }
    public Color getColor() {
        return c;
    }
    public static TileName getName(int value) {
        for (TileName t : TileName.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return null;
    }
}
