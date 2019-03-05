package com.sc2ad.azul;

import java.util.Collection;
import java.util.LinkedList;

//TODO IMPLEMENTS AZULDRAWABLE
public class TileBag extends TileCollection {
    public static TileBag Instance = new TileBag();
    private final static int TILE_COUNT = 20;

    private LinkedList<Tile> discard;
    private TileBag() {
        discard = new LinkedList<Tile>();
        reset();
    }

    /**
     * Resets the TileBag entirely, completely destroys bag and discard.
     */
    public void reset() {
        tiles.clear();
        discard.clear();
        for (int i = 0; i < TILE_COUNT; i++) {
            for (int j = 0; j < TileName.values().length; j++) {
                if (TileName.values()[j].getValue() < 0) {
                    continue;
                }
                tiles.add(new Tile(TileName.values()[j]));
            }
        }
    }

    /**
     * Returns a list of random tiles from the TileBag. Also shuffles discard back in if out of tiles.
     * @param count
     * @return
     */
    public Tile[] take(int count) {
        return take(new TakeItem<Integer, Object>(count));
    }
    public void addToDiscard(Collection<Tile> tiles) {
        if (tiles.contains(Tile.STARTING_TILE)) {
            for (Tile t : tiles) {
                if (!t.equals(Tile.STARTING_TILE)) {
                    discard.add(t);
                }
            }
        }
        discard.addAll(tiles);
    }
    @Override
    public Tile[] take(TakeItem item) {
        TakeItem<Integer, Object> it = (TakeItem<Integer, Object>) item;
        Tile[] out = new Tile[it.t];
        for (int i = 0; i < it.t; i++) {
            if (tiles.isEmpty()) {
                if (discard.isEmpty()) {
                    // Out of discard and tiles
                    return out;
                }
                tiles.addAll(discard);
            }
            out[i] = ((LinkedList<Tile>) tiles).remove((int) (Math.random() * tiles.size()));
        }
        return out;
    }

    @Override
    public void updateTileLocations() {
        // TODO Can add stuff here
    }
}
