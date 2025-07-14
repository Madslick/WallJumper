package io.bitbucket.rift_runner.tools;

import io.bitbucket.rift_runner.game_objects.classes.ManipulatableObject;
import io.bitbucket.rift_runner.gui.SceneObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import io.bitbucket.rift_runner.WallJumper;
import io.bitbucket.rift_runner.screens.World;

public class InputManager extends InputAdapter {


	public static InputManager inputManager = new InputManager();

	public Array<ManipulatableObject> controllableObjects;

	private InputManager(){


	}
	public void init(){
		//Will send input to any objects that take it
		controllableObjects = new Array<ManipulatableObject>();
		Gdx.input.setInputProcessor(this); //Only this object receives player input
	}
	public void addObject(ManipulatableObject object){
		controllableObjects.add(object);
	}
	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Keys.ESCAPE){
			Gdx.app.exit();
			return false;
		}
		if(World.controller.countDown > 0){
			return false;
		}

		if(!WallJumper.currentScreen.handleKeyInput(keycode))
			return false;

		if (WallJumper.paused){
			return false;
		}


		//Send input to the controlled objects
		for(ManipulatableObject target:controllableObjects){
			target.actOnInputKeyDown(keycode);
		}

		return false;
	}


	@Override
	public boolean keyUp(int keycode) {

		//Send key up input to controlled objects
		for(ManipulatableObject target:controllableObjects){
			target.actOnInputKeyUp(keycode);
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {


		//Pass input to currentScreen
		WallJumper.currentScreen.handleTouchInputDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Sends all the on release touch coordinates to children
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			objects.touchUp(screenX, screenY, pointer, button);
		}
		//Pass input to currentScreen
		//WallJumper.currentScreen.handleTouchInput(screenX, screenY, pointer, button);

		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//Sends all the on release touch coordinates to children
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			objects.touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	private void checkPausePressed() {

	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
	public ManipulatableObject getPlayer() {
		// TODO Auto-generated method stub
		return controllableObjects.get(0);
	}

}
