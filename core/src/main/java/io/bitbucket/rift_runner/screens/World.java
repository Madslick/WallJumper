package io.bitbucket.rift_runner.screens;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.bitbucket.rift_runner.Constants;
import io.bitbucket.rift_runner.DirectedGame;
import io.bitbucket.rift_runner.WallJumper;
import io.bitbucket.rift_runner.game_objects.AbstractGameObject;
import io.bitbucket.rift_runner.game_objects.classes.ManipulatableObject;
import io.bitbucket.rift_runner.game_objects.terrain.Portal;
import io.bitbucket.rift_runner.gui.Button;
import io.bitbucket.rift_runner.gui.Image;
import io.bitbucket.rift_runner.gui.PauseButton;
import io.bitbucket.rift_runner.gui.SceneObject;
import io.bitbucket.rift_runner.tools.Assets;
import io.bitbucket.rift_runner.tools.AudioManager;
import io.bitbucket.rift_runner.tools.CameraHelper;
import io.bitbucket.rift_runner.tools.InputManager;
import io.bitbucket.rift_runner.tools.LevelStage;
import io.bitbucket.rift_runner.tools.WorldRenderer;

public class World  {
	public static World controller;
	public CameraHelper cameraHelper;
	public LevelStage levelStage;
	private ManipulatableObject player;
	public float countDown, levelTimer;
	public boolean started;
	public boolean blackHoled;
	public boolean failedLoad;
	public static Portal portal;
	public static boolean restart;
	public Button button;

	private ManipulatableObject from;
	public AbstractGameObject to;
	private DirectedGame game;
	public AbstractScreen gameScreen;
	public boolean renderAll, riftFragCollected;
	private TweenManager tween;
	public boolean nextLevel;
	public boolean backTolevelMenu;
	public boolean camOnTarget;

	private Vector2 spawnPoint;
	private boolean faceLeft;
	public boolean backToHomeMenu;



	public World(DirectedGame game, AbstractScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
	}

	public void init() {

		//PlayMusic
		if(!AudioManager.instance.isPlaying())
			AudioManager.instance.playMusic(Assets.instance.music.world0);

		nextLevel = false;
		backTolevelMenu = false;
		riftFragCollected = false;
		countDown = 0;
		restart = false;
		levelTimer = 0;

		//Init other necessary classes
		WorldRenderer.renderer.init();
		cameraHelper = new CameraHelper();// Essentially makes the camera follow
		cameraHelper.setZoom(Constants.defaultZoom, true);
		InputManager.inputManager.init();
		levelStage = new LevelStage();

		//USED AS IN GAME MENU DURING PLAY
 		tween = new TweenManager();

		if(failedLoad){
			failedLoad = false;
			return;
		}

		// have a player variable here
		player = InputManager.inputManager.getPlayer();
		if(spawnPoint != null)
			player.position.set(spawnPoint);



		//Set up camera helper
		cameraHelper.setTarget(LevelStage.player);
		cameraHelper.applyTo(WorldRenderer.renderer.camera);
		cameraHelper.setPosition(LevelStage.player.position.x, LevelStage.player.position.y);

		//Set up pause button
		WallJumper.paused = true;
		WorldRenderer.renderer.pauseButton = new PauseButton();
		WorldRenderer.renderer.pauseMenu();

		levelStage.update(0);
		started = false;
		blackHoled = false;
		renderAll = true;

	}

	public void render(SpriteBatch batch) {

		levelStage.render(batch);
	}

	public void update(float deltaTime) {

		levelStage.update(deltaTime);
		cameraHelper.update(deltaTime);
		WorldRenderer.renderer.weather.update(deltaTime);
		WorldRenderer.renderer.updateScene(deltaTime);
		blackHoleMovement(deltaTime);
		checkDeath();
	}

	private void checkDeath() {
		if(backTolevelMenu){
			gameScreen.backToLevelMenu();
		}if(backToHomeMenu){
			gameScreen.backToHomeMenu();
		} else if(restart){
			gameScreen.restartLevel();
		}else if(nextLevel){
			gameScreen.nextLevel();
		}
	}
	private void blackHoleMovement(float deltaTime){
		if(from != null && !from.continueToHole(deltaTime)){

			if(portal.isDeathPortal()){
				gameScreen.restartLevel();
				return;
			}
			//Play the success screen, watch it out, it may run every from if from != null
			WorldRenderer.renderer.levelCompleteMenu();
			from = null;
		}
	}

	public void setSpawnPoint(Vector2 spawnPoint, boolean faceLeft){
		this.spawnPoint = spawnPoint;
		this.faceLeft = faceLeft;
	}
	public void render(float delta) {

		renderAll = cameraHelper.zoom != Constants.defaultZoom ? true : renderAll;
		//MAIN GAME UPDATE CALL
		if (!WallJumper.paused && cameraHelper.zoom == Constants.defaultZoom && camOnTarget) {
			renderAll = false;
			delta = delta < .25f ? delta : .25f;

			if(!blackHoled)
				levelTimer += delta;

			update(delta);


		//Update countdown
		}else {
			controller.cameraHelper.update(delta);
		}

		Gdx.gl.glClearColor(255, 255, 255, 0); // Default background color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(WorldRenderer.renderer.camera != null){
			WorldRenderer.renderer.render();
		}

	}
	public InputProcessor getInputProcessor(){
		return InputManager.inputManager;
	}
	public float getLevelTime(){
		return levelTimer;
	}

	public void resize(int width, int height) {
		WorldRenderer.renderer.resize(width, height);
	}
	public boolean handleKeyInput(int keycode){
  		if(Keys.SPACE == keycode){
			return startLevel();
		}
		return true;
	}

	//return false jumps back to handleTouchInput which
	//instantle goes to inputManager.touchInput or keyInput
	private boolean startLevel(){
		if(!started){
			levelTimer = 0;
			WorldRenderer.renderer.pauseButton.play();
			if(faceLeft)
				LevelStage.player.moveLeft();
			else
				LevelStage.player.moveRight();
			started = true;

			return false;
		}
		return true;
	}
	//Return false to do a return in the method calling this
	public boolean handleTouchInput(int screenX, int screenY, int pointer, int button) {
		//Sends all touch down coordinates to the children
		for(SceneObject objects : WorldRenderer.renderer.getSceneObjects()){
			if(objects.touchDown(screenX, screenY, pointer, button)){
				return false;
			}
		}
		if(!startLevel())
			return false;


		// Top left corner is a pause button
		if (screenX < Gdx.graphics.getWidth() / 10
				&& screenY < Gdx.graphics.getHeight() / 10 && !blackHoled) {
			WorldRenderer.renderer.pauseButton.toggle();
			return false;
		//
		}else if(WallJumper.paused){
			WorldRenderer.renderer.pauseButton.toggle();
			return false;
		}

		//send input to riftrunner
		for(ManipulatableObject target:InputManager.inputManager.controllableObjects){
			target.actOnInputTouch(screenX, screenY, pointer, button);
		}

		return true;

	}

	public void show() {
		World.controller.init();
		World.controller.setSpawnPoint(null, false);

	}

	public void hide() {
		World.controller.destroy();
	}

	public void pause() {

	}

	public void resume() {
	}

	public void dispose() {
		destroy();
		levelStage = null;
		InputManager.inputManager = null;
		AudioManager.instance.stopMusic();
		AudioManager.instance = null;
		Assets.instance.dispose();
		Assets.instance = null;
		WorldRenderer.renderer = null;
	}

	public Rectangle getPlayerRect() {
		return player.bounds;
	}

	public void destroy() {
		from = null;
		to = null;
		cameraHelper.destroy();
		if(levelStage != null)
		levelStage.destroy();
		player = null;
		riftFragCollected = false;
		// WorldRenderer.renderer.destroy();
		InputManager.inputManager.controllableObjects.clear();

	}
	//When called, automatically pauses game until countdown < 0
	public void countDown(float time) {
		countDown = time;
	}

	//Transitions into blackhole, only called once due to blackHoled bool
	public void moveTowards(ManipulatableObject from, AbstractGameObject to, float time) {
		if(!blackHoled){
			this.from = from;
			this.to = to;
			from.time = time;
			from.stopMove();
			from.velocity.set(0,0);
			from.setAnimation(from.aniJumping);
			blackHoled = true;
			from.acceleration.set(0, 0);
			from.moveToSomethingOverTime = time;
			from.bhOriginPos = new Vector2(from.position);
			from.deltaPosition = new Vector2(to.position.x + to.dimension.x / 2 - (from.position.x + from.dimension.x / 2) + .5f,
					to.position.y + to.dimension.y / 2  - (from.position.y + from.dimension.y / 2) + .6f);

		}
	}


}
