package io.bitbucket.rift_runner.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import io.bitbucket.rift_runner.DirectedGame;
import io.bitbucket.rift_runner.ProfileLoader;
import io.bitbucket.rift_runner.WallJumper;
import io.bitbucket.rift_runner.gui.SceneAssets;
import io.bitbucket.rift_runner.screens.screentransitions.ScreenTransition;
import io.bitbucket.rift_runner.screens.screentransitions.ScreenTransitionFade;
import io.bitbucket.rift_runner.screens.screentransitions.ScreenTransitionSlice;
import io.bitbucket.rift_runner.tools.AudioManager;
import io.bitbucket.rift_runner.tools.InputManager;

public class GameScreen extends AbstractScreen {
	
	public GameScreen(DirectedGame game) {
		super(game);
	}
	@Override
	public void render(float delta) {
		World.controller.render(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		World.controller.resize(width, height);
	}

	@Override
	public void show() {
		World.controller = new World(game, this);
		World.controller.show();
		WallJumper.currentScreen = this;
		
	}

	@Override
	public void hide() {
		// World.controller.hide();
		
		
	}

	@Override
	public void pause() {
		super.pause();
		World.controller.pause();
	}

	@Override
	public void resume() {
		World.controller.resume();
	}

	@Override
	public void dispose() {
		World.controller.dispose();
	}
	
	public boolean handleTouchInputDown(int screenX, int screenY, int pointer, int button){
		return World.controller.handleTouchInput(screenX, screenY, pointer, button);
		
	}
	
	public boolean handleKeyInput(int keycode) {
		
		return World.controller.handleKeyInput(keycode);
	}
	
	//CHANGE LEVEL METHODS
	public void nextScreen(){
		ScreenTransition transition = ScreenTransitionSlice.init(.6f, ScreenTransitionSlice.UP_DOWN, 10,
				Interpolation.pow2Out);
		game.setScreen(new GameScreen(game), transition);
	}
	
	
	//Set spawnpoint to null, destroy and init world controller and go to next level
	public void nextLevel(){
		
		WallJumper.level++;
		World.controller.setSpawnPoint(null, false);
		World.controller.destroy();
		// SceneAssets.instance.dispose();
		// World.controller.init();
		nextScreen();
	}
	
	public void changeScreen(AbstractScreen screen) {
		((Game) Gdx.app.getApplicationListener()).setScreen(screen);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return InputManager.inputManager;
	}

}
