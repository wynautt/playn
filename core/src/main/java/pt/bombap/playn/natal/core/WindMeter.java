package pt.bombap.playn.natal.core;

import playn.core.PlayN;
import playn.core.ResourceCallback;

/**
 * TODO: rethink generic widget class
 *
 */
public class WindMeter extends Widget<Float> {
	private ImageView neddle;

	public WindMeter(final GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		neddle = new ImageView("images/neddle.png");
		
		final float nw = 0.4f;
		final float nh = 1.8f;
		
		neddle.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() * (3 / 4f));
				view.setScale(nw / view.getWidth(), nh / view.getHeight());
				view.setTranslation(x, y);
				view.setRotation(-135 * MathUtils.DEGTORAD);
				
				((NatalWorld)gameWorld).getControlLayer().add(neddle.getLayer());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
	}
	
	private float speedToDeg(float speed) {
		return -135 + speed * 90 / 10;
	}

	@Override
	public void setValue(Float value) {
		this.value = value;
		neddle.setRotation(speedToDeg(value) * MathUtils.DEGTORAD);
	}
	
	float lastUpdate = 0;
	boolean up = true;
	@Override
	public void update(float delta) {
		super.update(delta);
		lastUpdate += delta;
		
		if(lastUpdate > 100) {
			neddle.setRotation(speedToDeg(value + (up ? 0.2f : -0.2f)) * MathUtils.DEGTORAD);
			lastUpdate = 0;
			up = !up;
		}
	}

	@Override
	public Float getValue() {
		return value;
	}

	@Override
	public void initPreLoad(GameWorld gameWorld) {
		((NatalWorld)gameWorld).getControlLayer().add(getView().getLayer());
	}

	@Override
	protected View createView() {
		return new ImageView("images/meter_f.png");
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
	}
	
	

}
