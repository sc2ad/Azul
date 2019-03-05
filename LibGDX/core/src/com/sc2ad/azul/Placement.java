package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Placement implements AzulDrawable {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 200;
    private static final int ROWS = 5;
    private Sprite sprite;

    private PlacementRow[] rows;
    private LinkedList<Tile> lossTiles;
    private int loss;

    private class PlacementRow extends TileCollection implements AzulDrawable {
        private static final int HEIGHT = Placement.HEIGHT / 6;
        private static final int TILE_BUFFER = Tile.WIDTH + 5;

        private Sprite sprite;
        private int length;

        public PlacementRow(int length) {
            sprite = new Sprite(new Texture("whitebox.png"), length * TILE_BUFFER, HEIGHT);
            //TODO CHANGE FOLLOWING LINE
            sprite.setColor(Color.CLEAR);
            tiles = new LinkedList<Tile>();
            this.length = length;
        }

        public void reset() {
            tiles.clear();
        }

        public TileName getTileType() {
            if (tiles.size() == 0) {
                return null;
            }
            return tiles.iterator().next().getName();
        }

        public boolean possiblePlacement() {
            return length == tiles.size();
        }

        @Override
        public Tile[] take(TakeItem item) {
            reset();
            return new Tile[0];
        }

        @Override
        public void addTile(Tile tile) {
            if (!possiblePlacement()) {
                throw new RuntimeException("Tried to add a tile to a row while it was already full!");
            }
            super.addTile(tile);
        }

        // DESTRUCTIVE!
        @Override
        public void addTiles(Collection<Tile> tiles) {
            Iterator<Tile> iter = tiles.iterator();
            while (possiblePlacement() && iter.hasNext()) {
                Tile t = iter.next();
                tiles.remove(t);
                addTile(t);
            }
        }

        @Override
        public void updateTileLocations() {
            Iterator<Tile> iter = tiles.iterator();
            int index = 0;
            while (iter.hasNext()) {
                iter.next().setPos((index + 1) * TILE_BUFFER + sprite.getX(), length * TILE_BUFFER + sprite.getY());
                index++;
            }
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

    public Placement() {
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, HEIGHT);
        //TODO CHANGE THIS LINE
        sprite.setColor(Color.CLEAR);
        rows = new PlacementRow[ROWS];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new PlacementRow(i+1);
        }
        lossTiles = new LinkedList<Tile>();
        reset();
    }
    public void reset() {
        for (PlacementRow r : rows) {
            r.reset();
        }
        lossTiles.clear();
        loss = 0;
    }
    public void addTile(Tile tile, int row) {
        rows[row].addTile(tile);
    }
    public boolean possiblePlacement(Collection<Tile> tiles) {
        for (PlacementRow r : rows) {
            for (Tile t : tiles) {
                if (t.getName().equals(r.getTileType())) {
                    // Matching names. Return whether it is not yet full.
                    return r.possiblePlacement();
                } else if (r.getTileType() == null) {
                    // If no tile exists yet, can place in this row.
                    return true;
                }
            }
        }
        return false;
    }
    // DESTRUCTIVE
    public void addTiles(Collection<Tile> tiles, int row) {
        // ASSUME tiles are all of the same type of tile
        if (rows[row].possiblePlacement() && tiles.iterator().next().getName().equals(rows[row].getTileType())) {
            rows[row].addTiles(tiles);
        }
        updateTileLocations();
    }
    public Tile[] moveTiles() {
        Tile[] out = new Tile[ROWS];
        for (int i = 0; i < ROWS; i++) {
            PlacementRow row = rows[i];
            if (!row.possiblePlacement()) {
                // Full row, move a tile from this row
                out[i] = row.tiles.iterator().next();
                // Remove the tile from the row
                row.tiles.remove(out[i]);
            }
        }
        return out;
    }
    public void discardTiles(TileBag tileBag, Tile[] movedTiles) {
        for (int i = 0; i < ROWS; i++) {
            if (movedTiles[i] != null) {
                tileBag.addToDiscard(rows[i].tiles);
                rows[i].reset();
            }
        }
        tileBag.addToDiscard(lossTiles);
        lossTiles.clear();
        reset();
    }
    public void addScoreLoss(Collection<Tile> tiles) {
        lossTiles.addAll(tiles);
        calculateScoreLoss();
    }
    public int calculateScoreLoss() {
        loss = lossTiles.size();
        //TODO CHANGE LOSS TO BE OF A FUNCTION/SOMETHING
        for (int i = 2; i <= 4; i += 2) {
            if (lossTiles.size() > i) {
                loss++;
            }
        }
        return loss;
    }
    public void updateTileLocations() {
        for (PlacementRow r : rows) {
            r.updateTileLocations();
        }
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
        for (PlacementRow r : rows) {
            r.draw(batch);
        }
    }
}
