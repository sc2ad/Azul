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
    private static final int TILE_COUNT = 10;
    private static final int WIDTH = 10 * Board.TILE_BUFFER;
    private static final int TILE_HEIGHT = Board.TILE_BUFFER;

    private Player startingPlayer;
    private boolean startingTaken;

    private Sprite sprite;

    private Center() {
        tiles = new LinkedList<Tile>();
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, TILE_HEIGHT);
        //TODO CHANGE THIS LINE
//        sprite.setColor(Color.BLACK);
        sprite.setColor(Color.WHITE);
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
            t.setPos(((index % TILE_COUNT) + 1) * TILE_COUNT + sprite.getX(), -(index / TILE_COUNT + 1) * TILE_COUNT + sprite.getY() + sprite.getHeight());
        }
        scale(1, ((tiles.size() / TILE_HEIGHT + 2) * TILE_COUNT) / sprite.getHeight());
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
