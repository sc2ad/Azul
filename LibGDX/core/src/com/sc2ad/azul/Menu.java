package com.sc2ad.azul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.LinkedList;

public class Menu implements AzulDrawable {
    private Sprite backgroundSprite;
    private static final float TOP_PAD = 10f;
    private LinkedList<Button> buttons;

    private Skin skin;

    private Table table;

    public Menu(int width, int height) {
        this("whitebox.png", width, height);
    }
    public Menu(String textureLocation, int width, int height) {
        backgroundSprite = new Sprite(new Texture(textureLocation), width, height);
        //TODO allow for anchoring and for larger font size
        backgroundSprite.setColor(Color.WHITE);
        backgroundSprite.setPosition(Gdx.graphics.getWidth() / 2 - width / 2, Gdx.graphics.getHeight() / 2);

        buttons = new LinkedList<Button>();
        table = new Table();
        AssetManager assetManager = new AssetManager();
        assetManager.load("uiskin.json", Skin.class);
        assetManager.finishLoading();

        skin = assetManager.get("uiskin.json", Skin.class);
    }
    public void addTextButton(String text, ClickListener listener) {
        TextButton b = new TextButton(text, skin);
        b.addListener(listener);
        buttons.add(b);
    }
    public void addToStage(Stage stage) {
        table.setFillParent(true);
        table.top();
        table.padTop(TOP_PAD);

        for (Button b : buttons) {
            table.add(b);
            table.row();
        }

        stage.addActor(table);
    }
    public void dispose() {
        skin.dispose();
    }

    @Override
    public Sprite getSprite() {
        return backgroundSprite;
    }
}
