package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class Entity {
	final View view;
	float x, y, angle;

	public Entity(final GameWorld gameWorld, float px, float py, float pangle) {
		this.x = px;
		this.y = py;
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

	/**
	 * Perform pre-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPreLoad(final GameWorld gameWorld);

	/**
	 * Perform post-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPostLoad(final GameWorld gameWorld);

	public void paint(float alpha) {
		view.paint(alpha);
	}

	public void update(float delta) {
		view.update(delta);
	}

	public void setPos(float x, float y) {
		view.setTranslation(x, y);
	}

	public void setAngle(float a) {
		view.setRotation(a);
	}

	/**
	 * 
	 * @return Entity width in world coordinates.
	 */
	abstract float getWidth();

	/**
	 * 
	 * @return Entity height in world coordinates
	 */
	abstract float getHeight();

	public View getView() {
		return view;
	}
	
	protected abstract View createView();

}
