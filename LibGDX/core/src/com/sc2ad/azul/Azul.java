package com.sc2ad.azul;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Azul extends ApplicationAdapter {
	SpriteBatch batch;
	private AzulGame game;
	private CameraWrapper camera;
	private BitmapFont font;
	
	@Override
	public void create () {
		game = new AzulGame();
		//TODO Change!
		game.setup(2);
		game.startRound();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = CameraWrapper.CreateCamera(w, h);
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	@Override
	public void render () {
		handleInput();
		camera.update();
		batch.setProjectionMatrix(camera.getCamera().combined);

//		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		game.draw(batch);
		font.draw(batch, "Camera: " + camera.getCamera().position, 10, 10);
		batch.end();
	}

	public void handleInput() {
		// https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.getCamera().translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.getCamera().translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.getCamera().translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.getCamera().translate(0, -3, 0);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			camera.focusCamera(null);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			game.focusPlayer(0);
		}
	}

	@Override
	public void resize(int width, int height) {
//		camera.getCamera().viewportHeight *= (height / width);
//		camera.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
