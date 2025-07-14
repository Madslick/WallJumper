package io.bitbucket.rift_runner.game_objects.terrain.traps;

import io.bitbucket.rift_runner.game_objects.AbstractGameObject;
import io.bitbucket.rift_runner.tools.LevelStage;

public class Projectile extends AbstractGameObject {
	
	public Projectile(float x, float y, float width, float height, float rotation, float speed){
		super(x, y, width, height);
		this.rotation = rotation;
		velocity.set((float)(Math.cos(rotation) * speed), (float)(Math.sin(rotation) * speed));
	}
	@Override
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		
	}

}
