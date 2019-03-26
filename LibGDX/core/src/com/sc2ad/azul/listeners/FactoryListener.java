package com.sc2ad.azul.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sc2ad.azul.CameraWrapper;
import com.sc2ad.azul.Factory;
import com.sc2ad.azul.screens.AzulGameScreen;

//TODO Make this actually do something (allow for Tile clicking, actually give Tiles)
public class FactoryListener extends ClickListener {
    private Factory factory;
    private AzulGameScreen screen;

    public FactoryListener(Factory f, AzulGameScreen screen) {
        factory = f;
        this.screen = screen;
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        CameraWrapper.Instance.focusCamera(factory);
    }
}
