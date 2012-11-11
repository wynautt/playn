/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pt.bombap.playn.natal.core;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public abstract class DynamicPhysicsEntity extends Entity implements PhysicsEntity {
	// for calculating interpolation
	private float prevX, prevY, prevA;
	private Body body;
	
	public DynamicPhysicsEntity(GameWorld gameWorld, float width, float height,	float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		body = initPhysicsBody(gameWorld.getWorld(), x, y, angle);
		setPos(x, y);
		setAngle(angle);
		
		if(hasLoaded) {
			gameWorld.addEntity(this);
		}
	}

	public DynamicPhysicsEntity(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
		body = initPhysicsBody(gameWorld.getWorld(), x, y, angle);
		setPos(x, y);
		setAngle(angle);
		
		if(hasLoaded) {
			gameWorld.addEntity(this);
		}
	}

	protected abstract Body initPhysicsBody(World world, float x, float y, float angle);

	@Override
	public void initPostLoad(GameWorld gameWorld) {
		if(body != null) {
			gameWorld.addEntity(this);
		} else {
			hasLoaded = true;
		}
	}
	
	@Override
	public void paint(float alpha) {
		// interpolate based on previous state
		super.paint(alpha);
		float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
		float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
		float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
		view.getLayer().setTranslation(x, y);
		view.getLayer().setRotation(a);
	}

	@Override
	public void update(float delta) {
		// store state for interpolation in paint()
		super.update(delta);
		x = prevX = body.getPosition().x;
		y = prevY = body.getPosition().y;
		angle = prevA = body.getAngle();
	}

	
	public void setLinearVelocity(float x, float y) {
		body.setLinearVelocity(new Vec2(x, y));
	}

	public void setAngularVelocity(float w) {
		body.setAngularVelocity(w);
	}

	@Override
	public void setPos(float x, float y) {
		super.setPos(x, y);
		getBody().setTransform(new Vec2(x, y), getBody().getAngle());
		prevX = x;
		prevY = y;
	}
	
	public Vec2 getLinearVelocity() {
		return body.getLinearVelocity();
	}
	
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}
	
	@Override
	public void setAngle(float a) {
		super.setAngle(a);
		getBody().setTransform(getBody().getPosition(), a);
		prevA = a;
	}
	
	@Override
	protected void destroy(GameWorld gameWorld) {
		world.getWorld().destroyBody(getBody());
		world.markEntityDirty(this);
	}

	@Override
	public Body getBody() {
		return body;
	}
}
