package io.bitbucket.rift_runner.gui;

import io.bitbucket.rift_runner.game_objects.AbstractGameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.bitbucket.rift_runner.Constants;
import io.bitbucket.rift_runner.WallJumper;
import io.bitbucket.rift_runner.screens.World;
import io.bitbucket.rift_runner.tools.Assets;
import io.bitbucket.rift_runner.tools.InputManager;
import io.bitbucket.rift_runner.tools.WorldRenderer;

public class PauseButton extends AbstractGameObject {
	private TextureRegion pause;
	private TextureRegion play;
	
	public PauseButton(){
		pause = Assets.instance.pause.pause;
		play = Assets.instance.pause.play;
		image = play;
		
		scale = 10;
		dimension = new Vector2(41,  46);
		position = new Vector2(20, Constants.bgViewportHeight - Constants.bgViewportHeight / 10);
	}
	public void toggle(){
		
		if(World.controller.blackHoled)
			return;
		
		if(!WallJumper.paused){
			pause();
		}else{
			play();
		}
	}
	public void pause(){
		image = play;
		WallJumper.paused = true;
		WorldRenderer.renderer.pauseMenu();
		if(World.controller.cameraHelper.hasTarget())
			World.controller.cameraHelper.moveTowardsPosition(World.controller.cameraHelper.getTarget().position);
		World.controller.cameraHelper.setTarget(null);
		World.controller.renderAll = true;
		
	}
	public void play(){
		
		WorldRenderer.renderer.unDisplayToWorld();
		WallJumper.paused = false;
		image = pause;
		World.controller.renderAll = false;
		World.controller.cameraHelper.setTarget(InputManager.inputManager.getPlayer());
		World.controller.cameraHelper.transitionToZoom(Constants.defaultZoom);
		WorldRenderer.renderer.clearScene();
		
	}
	@Override
	public void render(SpriteBatch batch){
		// get correct image and draw the current proportions
		// Draw
		batch.draw(image.getTexture(), position.x, position.y, origin.x, origin.y,
				dimension.x, dimension.y, 1, 1,
				rotation, image.getRegionX(), image.getRegionY(),
				image.getRegionWidth(), image.getRegionHeight(),
				false, false);

	}
	
	
}
