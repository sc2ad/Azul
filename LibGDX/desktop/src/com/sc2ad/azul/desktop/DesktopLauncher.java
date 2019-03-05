package com.sc2ad.azul.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sc2ad.azul.Azul;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 2048;
		config.height = 768*2;
		new LwjglApplication(new Azul(), config);
	}
}
