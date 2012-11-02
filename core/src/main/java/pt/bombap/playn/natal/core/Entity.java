package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class Entity {
	final ObjectView layer;
	float x, y, angle;

	public Entity(final GameWorld gameWorld, float px, float py, float pangle) {
		this.x = px;
		this.y = py;
		this.angle = pangle;
		layer = createObjectView();
		initPreLoad(gameWorld);
//		getImage().addCallback(new ResourceCallback<Image>() {
//			@Override
//			public void done(Image image) {
//				// since the image is loaded, we can use its width and height
//				layer.setOrigin(image.width() / 2f, image.height() / 2f);
//				layer.setScale(getWidth() / image.width(), getHeight() / image.height());
//				layer.setTranslation(x, y);
//				layer.setRotation(angle);
//				initPostLoad(peaWorld);
//			}
//
//			@Override
//			public void error(Throwable err) {
//				PlayN.log().error("Error loading image: " + err.getMessage());
//			}
//		});
	}

	/**
	 * Perform pre-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPreLoad(final GameWorld peaWorld);

	/**
	 * Perform post-image load initialization (e.g., attaching to PeaWorld layers).
	 *
	 * @param peaWorld
	 */
	public abstract void initPostLoad(final GameWorld peaWorld);

	public void paint(float alpha) {
	}

	public void update(float delta) {
	}

	public void setPos(float x, float y) {
		layer.setTranslation(x, y);
	}

	public void setAngle(float a) {
		layer.setRotation(a);
	}

	abstract float getWidth();

	abstract float getHeight();

	public abstract Image getImage();
	
	public abstract ObjectView createObjectView();

	protected static Image loadImage(String name) {
		return assets().getImage("peas/images/" + name);
	}

}
