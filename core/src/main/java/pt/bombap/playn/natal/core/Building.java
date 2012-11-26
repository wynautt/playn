package pt.bombap.playn.natal.core;

public class Building extends Entity {
	
	private SpriteAnimatorView view;
	
	//hack: lack of init method poses a major problem when trying to use the same entity with slightly different views
	public static SpriteAnimatorView currentView;
	
	
	public Building(GameWorld gameWorld, float px, float py, float pangle) {
		super(gameWorld, px, py, pangle);
	}
	
	
	public Building(GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
	}

	@Override
	public void initPreLoad(GameWorld gameWorld) {
		((NatalWorld)world).getStaticLayerBack().add(view.getLayer());
	}
	
	@Override
	public void initPostLoad(GameWorld gameWorld) {
		super.initPostLoad(gameWorld);
		view.start();
	}

	@Override
	protected View createView() {
		return view = currentView;
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
		view.stop();
	}

}
