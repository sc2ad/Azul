package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board implements AzulDrawable {
    private static final int ROWS = 5;
    private static final int COLUMNS = 5;

    public static final int TILE_BUFFER = 25;

    private static final int WIDTH = ROWS * TILE_BUFFER;
    private static final int HEIGHT = COLUMNS * TILE_BUFFER;

    private Sprite sprite;
    private Tile[][] board;

    public Board() {
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, HEIGHT);
        // TODO CHANGE THE NEXT LINE
//        sprite.setColor(Color.CLEAR);
        sprite.setColor(Color.WHITE);
        board = new Tile[ROWS][COLUMNS];
    }
    public int scoreTile(int row, int index) {
        int score = 0;
        int q = index;
        // Checking on same row
        while (q >= 0) {
            if (board[row][q] == null) {
                break;
            }
            score++;
            q--;
        }
        q = index + 1;
        while (q <= COLUMNS) {
            if (board[row][q] == null) {
                break;
            }
            score++;
            q++;
        }
        // Checking on same column
        int r = row;
        while (r >= 0) {
            if (board[r][index] == null) {
                break;
            }
            score++;
            r--;
        }
        r = row + 1;
        while (r <= ROWS) {
            if (board[r][index] == null) {
                break;
            }
            score++;
            r++;
        }
        return score - 1;
    }
    public int scoreBoard(Tile[] tilesToAdd) {
        // ASSUMING tilesToAdd.length == ROWS
        int score = 0;
        for (int row = 0; row < ROWS; row++) {
            Tile t = tilesToAdd[row];
            if (t != null) {
                addTile(t, row);
                score += scoreTile(row, getIndex(row, t));
            }
        }
        return score;
    }
    public void addTile(Tile tile, int row) {
        board[row][getIndex(row, tile)] = tile;
        updateTileLocations();
    }
    public Set<Tile> getTilesInRow(int row) {
        HashSet<Tile> tiles = new HashSet<Tile>(Arrays.asList(board[row]));
        tiles.remove(null); // Remove null, if it exists
        return tiles;
    }

    /**
     * Returns the number of completed rows
     * @return
     */
    public int completedRowCount() {
        int rows = 0;
        for (int row = 0; row < ROWS; row++) {
//            if (!Arrays.asList(row).contains(null)) {
            if (getTilesInRow(row).size() == ROWS) {
                rows++;
            }
        }
        return rows;
    }

    /**
     * Returns the number of completed columns
     * @return
     */
    public int completedColumnCount() {
        int cols = 0;
        for (int i = 0; i < COLUMNS; i++) {
            boolean complete = true;
            for (int j = 0; j < ROWS; j++) {
                if (board[j][i] == null) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                cols++;
            }
        }
        return cols;
    }
    public int completedAllCellsCount() {
        int count = 0;
        for (TileName t : TileName.values()) {
            int countPerName = 0;
            for (int row = 0; row < ROWS; row++) {
                // This works because .contains calls .equals, which is valid for Tile.equals(TileName)
                if (getTilesInRow(row).contains(t)) {
                    countPerName++;
                }
            }
            if (countPerName == ROWS) {
                count++;
            }
        }
        return count;
    }
    public void updateTileLocations() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] != null) {
                    board[i][j].setCenterPos(getIndex(i, board[i][j]) * TILE_BUFFER + sprite.getX(), (i + 1) * TILE_BUFFER + sprite.getY());
                }
            }
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
        // Draw tiles and "hollow" tiles
        // TODO
        for (Tile[] ti : board) {
            for (Tile t : ti) {
                if (t != null) {
                    t.draw(batch);
                }
            }
        }
    }

    public static int getIndex(int row, Tile tile) {
        return (tile.getValue() + row) % ROWS;
    }
    public static int getIndex(int row, int value) {
        return (value + row) % ROWS;
    }
}
