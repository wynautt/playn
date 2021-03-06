package pt.bombap.playn.natal.core;

import java.util.Stack;

import playn.core.Gradient;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import static playn.core.PlayN.*;

public class LifesCounter extends Widget<Integer>{
	private Stack<ImageView> views = new Stack<ImageView>();
	private Stack<ImageView> hiddenViews = new Stack<ImageView>();
	
	public LifesCounter(final GameWorld gameWorld, final float width, final float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		
		final ImageView v = new ImageView("images/mini_present.png");
		
		v.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() /2f);
				view.setScale(width / view.getWidth(), height / view.getHeight());
				view.setTranslation(x - 1.2f * width, y);
				
				((NatalWorld)gameWorld).getControlLayer().add(v.getLayer());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
		
		final ImageView v2 = new ImageView("images/mini_present.png");
		
		v2.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() /2f);
				view.setScale(width / view.getWidth(), height / view.getHeight());
				view.setTranslation(x - 2.4f * width, y);
				
				((NatalWorld)gameWorld).getControlLayer().add(v2.getLayer());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
		
		hiddenViews.push(v2);
		hiddenViews.push(v);
		hiddenViews.push((ImageView) getView());
				
	} 

	@Override
	public void setValue(Integer value) {
		assert value >= -3 && value <= 3;
		
		for(int i = 0; i < value; i++) {
			views.push(hiddenViews.pop());
		}
		for(int i = 0; i > value; i--) {
			hiddenViews.push(views.pop());
		}
		
		for(View v: views) {
			v.getLayer().setVisible(true);
		}
		for(View v: hiddenViews) {
			v.getLayer().setVisible(false);
		}
	}

	@Override
	public Integer getValue() {
		return null;
	}

	@Override
	public void initPreLoad(GameWorld gameWorld) {
		((NatalWorld)gameWorld).getControlLayer().add(getView().getLayer());
	}

	@Override
	protected View createView() {
		return new ImageView("images/mini_present.png");
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
	}
	

}
