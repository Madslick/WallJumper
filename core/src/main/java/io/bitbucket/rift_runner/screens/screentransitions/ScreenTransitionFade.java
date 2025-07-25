package io.bitbucket.rift_runner.screens.screentransitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionFade implements ScreenTransition {

	private static final ScreenTransitionFade instance = new ScreenTransitionFade();
	private float duration;
	
	public static ScreenTransitionFade init(float duration){
		instance.duration = duration;
		return instance;
		
	}
	@Override
	public float getDuration() {
		return duration;
	}

	@Override
	public void render(SpriteBatch batch, Texture curScreen, Texture nextScreen, float alpha) {
		
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		alpha = Interpolation.fade.apply(alpha);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		batch.draw(curScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, curScreen.getWidth(), curScreen.getHeight(), false, true);
		batch.setColor(1, 1, 1, alpha);
		batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, curScreen.getWidth(), curScreen.getHeight(), false, true);
		batch.end();
	}

}
