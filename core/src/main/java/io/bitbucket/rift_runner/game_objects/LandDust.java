package io.bitbucket.rift_runner.game_objects;

import io.bitbucket.rift_runner.tools.Assets;

public class LandDust extends AbstractGameObject {
	
	public LandDust(float x, float y, float width, float height,
			boolean flipX, boolean flipY){
		super(x, y, width, height, flipX, flipY);
		setAnimation(Assets.instance.particle.landAnimation);
		
	}

}
