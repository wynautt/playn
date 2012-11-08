package pt.bombap.playn.natal.core;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public abstract class NatalEntity extends StaticPhysicsEntity {

	public NatalEntity(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
	}

	
	@Override
	public void initPreLoad(GameWorld gameWorld) {
		NatalWorld natalWorld = (NatalWorld) gameWorld;
		natalWorld.getStaticLayerFront().add(getView().getLayer());
	}

	@Override
	public void initPostLoad(GameWorld gameWorld) {
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
		((NatalWorld)gameWorld).getStaticLayerFront().remove(getView().getLayer());
	}
	
}
