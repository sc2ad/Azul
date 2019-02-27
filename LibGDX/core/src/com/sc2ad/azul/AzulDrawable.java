package com.sc2ad.azul;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public interface AzulDrawable {
    Sprite getSprite();
    default void draw(Batch batch) {
        getSprite().draw(batch);
    }
    default void setPos(float newX, float newY) {
        getSprite().setPosition(newX, newY);
    }

    default void scale(float x, float y) {
        getSprite().setScale(x, y);
    }
}
