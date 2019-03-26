package com.sc2ad.azul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sc2ad.azul.*;
import com.sc2ad.azul.listeners.CenterListener;
import com.sc2ad.azul.listeners.FactoryListener;
import com.sc2ad.azul.listeners.PlayerListener;
import com.sc2ad.azul.listeners.ResetListener;

import java.util.Arrays;
import java.util.LinkedList;

public class AzulGameScreen implements Screen {
    private final static int EXTRA_FACTORY_COUNT = 2;
    private final static int COMPLETED_ROW_BONUS = 2;
    private final static int COMPLETED_COLUMN_BONUS = 7;
    private final static int COMPLETED_ALL_CELL_BONUS = 10;
    private final static int FACTORY_INITIAL_COUNT = 4;

    // TODO Make these the variables that actually correspond to the screen (using Azul)
    private final static int WINDOW_WIDTH = 2048;
    private final static int WINDOW_HEIGHT = 768*2;
    private final static int FACTORY_RADIUS = 150;

    private Center center;
    private LinkedList<Player> players;
    private LinkedList<Factory> factories;
    private TileBag bag;
    protected Stage stage;

    private SpriteBatch batch;
    private FitViewport viewport;
    private boolean paused;
    private boolean showing;

    public AzulGameScreen() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), CameraWrapper.Instance.getCamera());
        viewport.apply();

        CameraWrapper.Instance.update();
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
    }

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
        // Setup background actor
        Actor background = new Actor();
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, 0);
        background.addListener(new ResetListener());
        stage.addActor(background);
        // Factory locations
        double delta = 2 * Math.PI / factories.size();
        for (int i = 0; i < factories.size(); i++) {
            factories.get(i).setPos((float) (WINDOW_WIDTH / 2 + FACTORY_RADIUS * Math.cos(delta * i)) - Factory.RADIUS, (float) (WINDOW_HEIGHT / 2 - FACTORY_RADIUS * Math.sin(delta * i)) - Factory.RADIUS);

            Actor f = factories.get(i).getActor();
            f.addListener(new FactoryListener(factories.get(i), this));
            stage.addActor(f);
        }
        // Player locations
        delta = (double) WINDOW_WIDTH / (double) players.size();
        //TODO Make this use a Constants.something instead!
        float y = (float) (WINDOW_HEIGHT - 7.5 * (Tile.WIDTH + 5));
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPos((float) (delta * i + 0.5 * (Tile.WIDTH + 5)), y);

            Actor p = players.get(i).getActor();
            p.addListener(new PlayerListener(players.get(i), this));
            stage.addActor(p);
        }
        center.setCenterPos(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);

        Actor c = center.getActor();
        c.addListener(new CenterListener(center, this));
        stage.addActor(c);
    }

    public void focusPlayer(int value) {
        for (Player p : players) {
            if (p.getValue() == value) {
                CameraWrapper.Instance.focusCamera(p);
            }
        }
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

    public void finalScoring() {
        for (Player p : players) {
            p.score += p.board.completedRowCount() * COMPLETED_ROW_BONUS;
            p.score += p.board.completedColumnCount() * COMPLETED_COLUMN_BONUS;
            p.score += p.board.completedAllCellsCount() * COMPLETED_ALL_CELL_BONUS;
        }
    }

    @Override
    public void show() {
        showing = true;
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(CameraWrapper.Instance.getCamera().combined);

        batch.begin();
        if (showing) {
            for (Player p : players) {
                p.draw(batch);
            }
            for (Factory f : factories) {
                f.draw(batch);
            }
            center.draw(batch);
            //TODO Maybe add drawing for TileBag, other stuff?
            //TODO Add input handling for AzulGameScreen (only when not paused?)
        }
        batch.end();
        CameraWrapper.Instance.update();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        CameraWrapper.Instance.getPosition().set(CameraWrapper.Instance.getCamera().viewportWidth / 2, CameraWrapper.Instance.getCamera().viewportHeight / 2, 0);
        CameraWrapper.Instance.update();
    }

    private void handleInput() {
        if (paused) {
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            CameraWrapper.Instance.focusCamera(null);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            focusPlayer(0);
        }
    }

    @Override
    public void pause() {
        paused = true;
        //TODO Add a dialog box indicating the game is paused?
    }

    @Override
    public void resume() {
        paused = false;
        //TODO nothing? Simply called when the dialog box is closed successfully?
    }

    @Override
    public void hide() {
        showing = false;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
