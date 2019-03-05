package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Tile implements AzulDrawable {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static final Tile STARTING_TILE = new Tile();

    private Sprite sprite;
    private TileName name;
    private int value;


    public Tile(TileName name) {
        this.name = name;
        value = name.getValue();
        sprite = new Sprite(name.getTexture(), WIDTH, HEIGHT);
        // If and only if getTexture is whitebox:
        if (name.getTexture().toString().equals("whitebox.png")) {
            sprite.setColor(name.getColor());
        }
    }
    public Tile(int value) {
        this(TileName.getName(value));
    }
    public Tile() {
        // Placeholder constructor for STARTING_TILE
        this(TileName.STARTING_TILE);
    }

    public int getValue() {
        return value;
    }

    public TileName getName() {
        return name;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tile) {
            return ((Tile) o).value == value;
        } else if (o instanceof TileName)  {
            return name.equals(o);
        }
        return false;
    }
}
