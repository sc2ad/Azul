package com.sc2ad.azul;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player implements AzulDrawable {
    private static final int WIDTH = 12 * Board.TILE_BUFFER;
    private static final int HEIGHT_OF_SCORE = 100;
    private static final int HEIGHT = 6 * Board.TILE_BUFFER + HEIGHT_OF_SCORE;

    private static final int NAME_OFFSET = 10;
    private static final int SCORE_OFFSET = 50;

    private String name;
    private int id;
    public Placement placement;
    public Board board;
    public int score;
    private Sprite sprite;
    private BitmapFont nameFont;
    private BitmapFont scoreFont;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        sprite = new Sprite(new Texture("whitebox.png"), WIDTH, HEIGHT);
        //TODO CHANGE THIS LINE
//        sprite.setColor(Color.CLEAR);
        sprite.setColor(Color.RED);
        placement = new Placement();
        board = new Board();
        nameFont = new BitmapFont();
        scoreFont = new BitmapFont();
    }
    public int getValue() {
        return id;
    }
    @Override
    public Sprite getSprite() {
        return sprite;
    }
    @Override
    public void setPos(float newX, float newY) {
        // Sets pos of the current sprite, as well as the placement and board which are children of this
        sprite.setPosition(newX, newY);
        placement.setPos(newX, newY);
        board.setPos(newX + 7 * Board.TILE_BUFFER, newY + (Tile.HEIGHT + 5));
    }
    @Override
    public void draw(Batch batch) {
        // Draw border for player
        sprite.draw(batch);
        // Draw placement, board
        placement.draw(batch);
        board.draw(batch);
        // TODO Draw score on the top, along with name
        nameFont.draw(batch, name, getCenterX(), sprite.getY() + sprite.getHeight() - NAME_OFFSET);
        scoreFont.draw(batch, String.valueOf(score), getCenterX(), sprite.getY() + sprite.getHeight() - SCORE_OFFSET);
    }
}
