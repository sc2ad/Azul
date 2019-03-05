package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Center extends TileCollection implements AzulDrawable {
    public static Center Instance = new Center();
    private static final int WIDTH = 200;
    private static final int TILE_COUNT = 25;
    private static final int TILE_WIDTH = (WIDTH - TILE_COUNT) / TILE_COUNT;

    private Player startingPlayer;
    private boolean startingTaken;

    private Sprite sprite;

    private Center() {
        tiles = new LinkedList<Tile>();
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, TILE_WIDTH * 2);
        //TODO CHANGE THIS LINE
        sprite.setColor(Color.BLACK);
        reset();
    }
    public void reset() {
        startingPlayer = null;
        startingTaken = false;
    }
    public Tile[] take(TileName tileName, Player player) {
        return take(new TakeItem<TileName, Player>(tileName, player));
    }

    @Override
    public Tile[] take(TakeItem item) {
        TakeItem<TileName, Player> it = (TakeItem<TileName, Player>) item;
        LinkedList<Tile> out = new LinkedList<Tile>();
        LinkedList<Tile> temp = new LinkedList<Tile>();
        Iterator<Tile> iter = tiles.iterator();
        while (iter.hasNext()) {
            Tile t = iter.next();
            if (t.getValue() == it.t.getValue()) {
                out.addLast(t);
            } else {
                temp.addLast(t);
            }
        }
        tiles = temp;
        if (!startingTaken) {
            startingPlayer = it.k;
            startingTaken = true;
        }
        updateTileLocations();
        return (Tile[]) out.toArray();
    }

    @Override
    public void updateTileLocations() {
        int index = 0;
        Iterator<Tile> iter = tiles.iterator();
        while (iter.hasNext()) {
            Tile t = iter.next();
            t.setPos(((index % TILE_WIDTH) + 1) * TILE_COUNT + getSprite().getX(), (index / TILE_WIDTH + 1) * TILE_COUNT + getSprite().getY());
        }
        scale(1, ((tiles.size() / TILE_WIDTH + 2) * TILE_COUNT) / sprite.getHeight());
    }

    @Override
    public void addTiles(Collection<Tile> tiles) {
        super.addTiles(tiles);
        updateTileLocations();
    }

    @Override
    public void addTile(Tile tile) {
        super.addTile(tile);
        updateTileLocations();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void draw(Batch batch) {
        // Draw border
        sprite.draw(batch);
        // Draw tiles
        for (Tile t : tiles) {
            t.draw(batch);
        }
    }
}
