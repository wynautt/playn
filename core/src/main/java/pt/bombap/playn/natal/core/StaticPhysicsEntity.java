package pt.bombap.playn.natal.core;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public abstract class StaticPhysicsEntity extends Entity implements PhysicsEntity {
	private Body body;

	public StaticPhysicsEntity(GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		body = initPhysicsBody(gameWorld.getWorld(), x, y, angle);

		if(hasLoaded) {
			gameWorld.addEntity(this);
		}
	}

	public StaticPhysicsEntity(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
		body = initPhysicsBody(gameWorld.getWorld(), x, y, angle);

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
	public void setPos(float x, float y) {
		throw new RuntimeException("Error setting position on static entity");
	}

	@Override
	public void setAngle(float a) {
		throw new RuntimeException("Error setting angle on static entity");
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
