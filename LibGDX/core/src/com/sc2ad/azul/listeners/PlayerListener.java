package com.sc2ad.azul.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sc2ad.azul.CameraWrapper;
import com.sc2ad.azul.Player;
import com.sc2ad.azul.screens.AzulGameScreen;

//TODO Make this actually do something (allow for tile placement/viewing)
public class PlayerListener extends ClickListener {
    private Player player;
    private AzulGameScreen screen;

    public PlayerListener(Player p, AzulGameScreen screen) {
        player = p;
        this.screen = screen;
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        CameraWrapper.Instance.focusCamera(player);
    }
}
