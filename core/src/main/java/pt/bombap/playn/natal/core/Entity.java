package pt.bombap.playn.natal.core;

import static playn.core.PlayN.log;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class Entity {
	protected final View view;
	/**
	 * entity properties may not represent entity view position on screen all the times
	 * you can use these properties to do calculations over the position of the entity 
	 * but if you want the entity to reflect these positions on the screen you must call setPos(x, y) and setRotation(angle) 
	 */
	protected float x, y, angle;
	protected final float width, height;

	/**
	 * here for future use
	 */
	protected boolean dirty = false;
	protected boolean autoDestroyWhenOutOfWorld = false;
	protected final GameWorld world;
	
	/**
	 * hack to add this entity to the world only when his body is constructed (applies only to physics entities)
	 */
	protected boolean hasLoaded = false;

	public Entity(final GameWorld gameWorld, final float width, float height, float px, float py, float pangle) {
		this.world = gameWorld;
		this.x = px;
		this.y = py;
		this.width = width;
		this.height = height;
		this.angle = pangle;
		view = createView();
		initPreLoad(gameWorld);
		view.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() / 2f);
				view.setScale(getWidth() / view.getWidth(), getHeight() / view.getHeight());
				view.setTranslation(x, y);
				view.setRotation(angle);
				initPostLoad(gameWorld);
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
	}

	public Entity(final GameWorld gameWorld, float px, float py, float pangle) {
		this(gameWorld, 0f, 0f, px, py, pangle);
	}

	/**
	 * Perform pre-image load initialization (e.g., attaching to World layers).
	 *
	 * @param GameWorld
	 */
	public abstract void initPreLoad(final GameWorld gameWorld);

	/**
	 * Perform post-image load initialization (e.g., attaching to World layers).
	 *
	 * @param GameWorld
	 */

	public void initPostLoad(final GameWorld gameWorld) {
		gameWorld.addEntity(this);
	}

	public void paint(float alpha) {
		view.paint(alpha);
	}

	public void update(float delta) {
		view.update(delta);
		if(autoDestroyWhenOutOfWorld) {
			destroyWhenOutOfWorld();
		}
	}

	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
		view.setTranslation(x, y);
	}


	public void setAngle(float a) {
		this.angle = a;
		view.setRotation(a);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getAngle() {
		return angle;
	}

	/**
	 * 
	 * @return Entity width in world coordinates.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * 
	 * @return Entity height in world coordinates
	 */
	public float getHeight() {
		return height;
	}

	public View getView() {
		return view;
	}

	protected boolean isOutOfWorld() {
		float x = getView().getLayer().transform().tx();

		if(x > world.getWorldWidth() + getWidth() / 2f) return true;
		if(x < -getWidth() / 2f) return true;

		float y = getView().getLayer().transform().ty();

		if(y > world.getWorldHeight() + getHeight() / 2f) return true;
		if(y < -getHeight() / 2f) return true;

		return false;
	}

	private void destroyWhenOutOfWorld() {
		if(isOutOfWorld()) {
			dirty = true;
			world.markEntityDirty(this);
			destroy(world);
			log().debug("out: " + this);
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	protected abstract View createView();
	protected abstract void destroy(GameWorld gameWorld);

}
