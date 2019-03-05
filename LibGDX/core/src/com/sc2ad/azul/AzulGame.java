package com.sc2ad.azul;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Arrays;
import java.util.LinkedList;

public class AzulGame {
    private final static int EXTRA_FACTORY_COUNT = 2;
    private final static int COMPLETED_ROW_BONUS = 2;
    private final static int COMPLETED_COLUMN_BONUS = 7;
    private final static int COMPLETED_ALL_CELL_BONUS = 10;
    private final static int FACTORY_INITIAL_COUNT = 4;

    // TODO Make these the variables that actually correspond to the screen (using Azul)
    private final static int WINDOW_WIDTH = 2048;
    private final static int WINDOW_HEIGHT = 768*2;
    private final static int FACTORY_RADIUS = 200;

    private Center center;
    private LinkedList<Player> players;
    private LinkedList<Factory> factories;
    private TileBag bag;

    /**
     * Sets up the game using default naming convention:
     * Player_1, Player_2, etc.
     * @param playerCount
     */
    public void setup(int playerCount) {
        // For now, minimum players is 2, max is 4
        assert playerCount >= 2 && playerCount <= 4;
        players = new LinkedList<Player>();
        factories = new LinkedList<Factory>();

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("Player_" + (i + 1), i));
            factories.add(new Factory(Arrays.asList(new Tile[0])));
        }
        for (int i = 0; i < EXTRA_FACTORY_COUNT; i++) {
            factories.add(new Factory(Arrays.asList(new Tile[0])));
        }
        bag = TileBag.Instance;
        center = Center.Instance;
        setupLocations();
    }

    private void setupLocations() {
        // Factory locations
        double delta = 2 * Math.PI / factories.size();
        for (int i = 0; i < factories.size(); i++) {
            factories.get(i).setPos((float) (WINDOW_WIDTH / 2 + FACTORY_RADIUS * Math.cos(delta * i)) - Factory.RADIUS, (float) (WINDOW_HEIGHT / 2 - FACTORY_RADIUS * Math.sin(delta * i)) - Factory.RADIUS);
        }
        // Placement locations
        delta = (double) WINDOW_WIDTH / (double) players.size();
        //TODO Make this use a Constants.something instead!
        float y = (float) (WINDOW_HEIGHT - 7.5 * (Tile.WIDTH + 5));
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPos((float) (delta * i + 0.5 * (Tile.WIDTH + 5)), y);
//            players.get(i).placement.setPos((float) (delta * i + 0.5 * (Tile.WIDTH + 5)), y);
//            players.get(i).board.setPos((float) (delta * i + 8 * Board.TILE_BUFFER), y);
        }
        center.setCenterPos(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
    }

    public void startRound() {
        for (Factory f : factories) {
            f.addTiles(Arrays.asList(bag.take(FACTORY_INITIAL_COUNT)));
        }
        center.reset();
    }

    public boolean factoriesEmpty() {
        for (Factory f : factories) {
            if (!f.getTileset().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEnd() {
        for (Player p : players) {
            if (p.board.completedRowCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public void draw(Batch batch) {
        for (Player p : players) {
            p.draw(batch);
        }
        for (Factory f : factories) {
            f.draw(batch);
        }
        center.draw(batch);
        //TODO Maybe add drawing for TileBag, other stuff?
    }

    public void finalScoring() {
        for (Player p : players) {
            p.score += p.board.completedRowCount() * COMPLETED_ROW_BONUS;
            p.score += p.board.completedColumnCount() * COMPLETED_COLUMN_BONUS;
            p.score += p.board.completedAllCellsCount() * COMPLETED_ALL_CELL_BONUS;
        }
    }
}
