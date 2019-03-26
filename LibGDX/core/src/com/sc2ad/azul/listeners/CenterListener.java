package com.sc2ad.azul.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sc2ad.azul.CameraWrapper;
import com.sc2ad.azul.Center;
import com.sc2ad.azul.screens.AzulGameScreen;

//TODO Make this actually do something (allow for tile taking)
public class CenterListener extends ClickListener {
    private Center center;
    private AzulGameScreen screen;

    public CenterListener(Center c, AzulGameScreen screen) {
        center = c;
        this.screen = screen;
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        CameraWrapper.Instance.focusCamera(center);
    }
}
