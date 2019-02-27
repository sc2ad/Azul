package com.sc2ad.azul;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class TileCollection {
    public Collection<Tile> tiles;

    public TileCollection() {
        tiles = new LinkedList<Tile>();
    }
    public abstract Tile[] take(TakeItem item);
    public abstract void updateTileLocations();

    /**
     * Adds tiles to the Collection of Tiles
     * @param tiles
     */
    public void addTiles(Collection<Tile> tiles) {
        tiles.addAll(tiles);
        updateTileLocations();
    }

    /**
     * Adds tile to the Collection of Tiles
     * @param tile
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
        updateTileLocations();
    }
    /**
     * Returns if the given tilename is in the factory
     * @param tileName
     * @return
     */
    public boolean contains(TileName tileName) {
        for (Tile t : tiles) {
            if (t.getName().equals(tileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the given tilevalue is in the factory
     * @param tileValue
     * @return
     */
    public boolean contains(int tileValue) {
        for (Tile t : tiles) {
            if (t.getValue() == tileValue) {
                return true;
            }
        }
        return false;
    }

    public Set<Tile> getTileset() {
        return new HashSet<Tile>(tiles);
    }
}
