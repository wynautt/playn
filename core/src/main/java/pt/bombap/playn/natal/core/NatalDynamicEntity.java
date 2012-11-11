package pt.bombap.playn.natal.core;


public abstract class NatalDynamicEntity extends DynamicPhysicsEntity {

	public NatalDynamicEntity(GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
	}


	public NatalDynamicEntity(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
	}

	
	@Override
	public void initPreLoad(GameWorld gameWorld) {
		NatalWorld natalWorld = (NatalWorld) gameWorld;
		natalWorld.getDynamicLayer().add(getView().getLayer());
	}

	
	@Override
	protected void destroy(GameWorld gameWorld) {
		super.destroy(gameWorld);
		((NatalWorld)gameWorld).getDynamicLayer().remove(getView().getLayer());
	}
	
}
