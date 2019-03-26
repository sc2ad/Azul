package com.sc2ad.azul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sc2ad.azul.Azul;
import com.sc2ad.azul.CameraWrapper;
import com.sc2ad.azul.Menu;

public class MainMenuScreen implements Screen {
    private static final int MENU_WIDTH = 200;
    private static final int MENU_HEIGHT = 1000;

    Menu mainMenu;
    protected Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    private ClickListener startClickListener;
    private ClickListener settingsClickListener;
    private ClickListener exitClickListener;

    public MainMenuScreen(Azul azul) {
        mainMenu = new Menu(MENU_WIDTH, MENU_HEIGHT);
        //TODO Constants!
        viewport = new FitViewport(2048, 768*2, CameraWrapper.Instance.getCamera());
        viewport.apply();

        CameraWrapper.Instance.update();
        batch = new SpriteBatch();

        stage = new Stage(viewport, batch);

        //TODO CREATE BETTER CLICK LISTENERS
        startClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AzulGameScreen game = new AzulGameScreen();
                game.setup(2);
                game.startRound();
                azul.setScreen(game);
            }
        };

        settingsClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                azul.setScreen(new AzulSettingsScreen(azul));
            }
        };

        exitClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        };
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainMenu.addTextButton("Start", startClickListener);
        mainMenu.addTextButton("Settings", settingsClickListener);
        mainMenu.addTextButton("Exit", exitClickListener);

        mainMenu.addToStage(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(CameraWrapper.Instance.getCamera().combined);

        batch.begin();
        mainMenu.draw(batch);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        CameraWrapper.Instance.getPosition().set(CameraWrapper.Instance.getCamera().viewportWidth / 2, CameraWrapper.Instance.getCamera().viewportHeight / 2, 0);
        CameraWrapper.Instance.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mainMenu.dispose();
    }
}
