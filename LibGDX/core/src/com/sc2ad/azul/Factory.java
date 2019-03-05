package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class Factory extends TileCollection implements AzulDrawable {
    private static final int MAX_TILES = 4;
    public static final float RADIUS = 50;

    private Sprite sprite;

    public Factory(Collection<Tile> tiles) {
        this.tiles = tiles;
        sprite = new Sprite(new Texture("whitebox.png"));
        // TODO CHANGE THIS LINE
//        sprite.setColor(Color.CLEAR);
        sprite.setSize(2 * RADIUS, 2 * RADIUS);
    }

    public Tile[] take(TileName tileName, TileCollection center) {
        return take(new TakeItem<TileName, TileCollection>(tileName, center));
    }

    @Override
    public void updateTileLocations() {
        Iterator<Tile> iter = tiles.iterator();
        int index = 0;
        while (iter.hasNext()) {
            iter.next().setCenterPos(getCenterX() - RADIUS / 3 + 2 * RADIUS / 3 * (index % 2), getCenterY() - RADIUS / 3 + 2 * RADIUS / 3 * (index / 2));
            index++;
        }
    }

    @Override
    public void addTiles(Collection<Tile> tiles) {
        if (tiles.size() > MAX_TILES) {
            return;
        }
        this.tiles = new ArrayList<Tile>(tiles);
        updateTileLocations();
    }

    @Override
    public void addTile(Tile tile) {
        if (tiles.size() < MAX_TILES) {
            tiles.add(tile);
        }
    }

    @Override
    public Tile[] take(TakeItem item) {
        TakeItem<TileName, TileCollection> it = (TakeItem<TileName, TileCollection>) item;
        LinkedList<Tile> out = new LinkedList<Tile>();
        Iterator<Tile> iter = tiles.iterator();
        while (iter.hasNext()) {
            Tile t = iter.next();
            if (t.getValue() == it.t.getValue()) {
                out.addLast(iter.next());
            } else {
                it.k.addTile(iter.next());
            }
        }
        tiles.clear();
        return (Tile[]) out.toArray();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void draw(Batch batch) {
        // Draw the border
        sprite.draw(batch);
        // Draw the tiles
        for (Tile t : tiles) {
            t.getSprite().draw(batch);
        }
    }
}
