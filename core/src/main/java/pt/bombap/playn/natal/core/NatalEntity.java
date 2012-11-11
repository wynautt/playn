package pt.bombap.playn.natal.core;


public abstract class NatalEntity extends StaticPhysicsEntity {	
	
	public NatalEntity(GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
	}


	public NatalEntity(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
	}

	
	@Override
	public void initPreLoad(GameWorld gameWorld) {
		NatalWorld natalWorld = (NatalWorld) gameWorld;
		natalWorld.getStaticLayerFront().add(getView().getLayer());
	}


	@Override
	protected void destroy(GameWorld gameWorld) {
		super.destroy(gameWorld);
		((NatalWorld)gameWorld).getStaticLayerFront().remove(getView().getLayer());
	}
	
}
