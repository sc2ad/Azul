package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player implements AzulDrawable {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private String name;
    private int id;
    public Placement placement;
    public Board board;
    public int score;
    private Sprite sprite;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, HEIGHT);
        //TODO CHANGE THIS LINE
        sprite.setColor(Color.CLEAR);
        placement = new Placement();
        board = new Board();
    }
    @Override
    public Sprite getSprite() {
        return sprite;
    }
    @Override
    public void draw(Batch batch) {
        // Draw border for player
        sprite.draw(batch);
        // Draw placement, board
        placement.draw(batch);
        board.draw(batch);
        // TODO Draw score on the top, along with name
    }
}
