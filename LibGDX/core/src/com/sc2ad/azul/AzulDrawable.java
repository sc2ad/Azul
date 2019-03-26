package com.sc2ad.azul;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface AzulDrawable {
    Sprite getSprite();

    default Actor getActor() {
        //TODO Fix this somehow
        Actor a = new Actor();
        a.setPosition(getSprite().getX(), getSprite().getY());
        a.setSize(getSprite().getWidth(), getSprite().getHeight());
        return a;
    }

    default void draw(Batch batch) {
        getSprite().draw(batch);
    }

    /**
     * Sets the position of the current drawable object. Uses the bottom left corner.
     * @param newX
     * @param newY
     */
    default void setPos(float newX, float newY) {
        getSprite().setPosition(newX, newY);
    }

    default void setCenterPos(float newX, float newY) {
        getSprite().setPosition(newX - getSprite().getWidth() / 2, newY - getSprite().getHeight() / 2);
    }

    default float getCenterX() {
        return getSprite().getX() + getSprite().getWidth() / 2;
    }

    default float getCenterY() {
        return getSprite().getY() + getSprite().getHeight() / 2;
    }

    default void scale(float x, float y) {
        getSprite().setScale(x, y);
    }
}
