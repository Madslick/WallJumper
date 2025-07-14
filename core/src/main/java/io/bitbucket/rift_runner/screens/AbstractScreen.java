package io.bitbucket.rift_runner.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import io.bitbucket.rift_runner.DirectedGame;
import io.bitbucket.rift_runner.ProfileLoader;
import io.bitbucket.rift_runner.WallJumper;
import io.bitbucket.rift_runner.screens.screentransitions.ScreenTransition;
import io.bitbucket.rift_runner.screens.screentransitions.ScreenTransitionSlice;
import io.bitbucket.rift_runner.tools.AudioManager;

public abstract class AbstractScreen implements Screen{
	protected DirectedGame game;
	
	public AbstractScreen(DirectedGame game){
		this.game = game;
	}
	@Override
	public void render(float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		WallJumper.paused = true;
	}

	@Override
	public void resume() {
		WallJumper.paused = false;
	}

	@Override
	public void dispose() {
		
	}
	
	public boolean handleTouchInputDown(int screenX, int screenY, int pointer, int button){
		return false;
	}

	public boolean handleKeyInput(int keycode) {
		return false;
	}
	public void changeScreen(AbstractScreen screen) {
		((DirectedGame) Gdx.app.getApplicationListener()).setScreen(screen);

	}

	public abstract InputProcessor getInputProcessor();
	public void backToLevelMenu(){
		
		ProfileLoader.profileLoader.saveProfile();
		AudioManager.instance.stopMusic();
		ScreenTransition transition = ScreenTransitionSlice.init(.6f, ScreenTransitionSlice.UP_DOWN, 10,
				Interpolation.pow2Out);
		game.setScreen(new LevelMenu(game), transition);
	}
	public void nextLevel() {
		
	}
	public void restartLevel(){

		World.controller.destroy();
		World.controller.init();
	}
	public void backToMainMenu() {
		AudioManager.instance.stopMusic();
		ScreenTransition transition = ScreenTransitionSlice.init(.6f, ScreenTransitionSlice.UP_DOWN, 10,
				Interpolation.pow2Out);
		game.setScreen(new MainMenu(game), transition);
	}
	public void backToHomeMenu() {
		ScreenTransition transition = ScreenTransitionSlice.init(.6f, ScreenTransitionSlice.UP_DOWN, 10,
				Interpolation.pow2Out);
		game.setScreen(new WorldScreen(game), transition);
	}
	public void handleTouchInputUp(int screenX, int screenY, int pointer,
			int button) {
		
	}

}
