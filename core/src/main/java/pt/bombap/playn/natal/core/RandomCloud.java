package pt.bombap.playn.natal.core;

public class RandomCloud extends Entity {
	private NatalWorld world;
	
	public RandomCloud(GameWorld gameWorld, float px, float py, float pangle) {
		super(gameWorld, px, py, pangle);
		world = (NatalWorld) gameWorld;
		y = (float) (Math.random() * getMaximumHeight());
		x = (float) (Math.random() * getMaximumWidth());
	}

	private float getMaximumWidth() {
		return world.getWorldWidth();
	}

	private float getMaximumHeight() {
		return world.getWorldHeight() / 2.0f;
	}

	@Override
	public void initPreLoad(GameWorld gameWorld) {
		((NatalWorld)gameWorld).getDynamicLayer().add(getView().getLayer());
	}

	@Override
	public void initPostLoad(GameWorld gameWorld) {
	}

	@Override
	public float getWidth() {
		return 12.0f;
	}

	@Override
	public float getHeight() {
		return 3.0f;
	}

	@Override
	protected View createView() {
		return new ImageView("sprites/Cloud1.png");
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
	}

	public float getVelocity() {
		return 0.002f;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		x += getVelocity() * delta;
		setPos(x, y);
		
		if(x > getWidth() / 2f + world.getWorldWidth()) {
			x = -getWidth() / 2f;
			y = (float) (Math.random() * getMaximumHeight());
		}
	}

}
