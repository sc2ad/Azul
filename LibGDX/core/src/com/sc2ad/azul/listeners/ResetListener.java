package com.sc2ad.azul.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sc2ad.azul.CameraWrapper;

public class ResetListener extends ClickListener {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        CameraWrapper.Instance.focusCamera(null);
    }
}
