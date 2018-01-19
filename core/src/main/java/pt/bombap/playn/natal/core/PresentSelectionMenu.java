package pt.bombap.playn.natal.core;

import java.util.Stack;

import playn.core.Gradient;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.ResourceCallback;
import playn.core.Pointer.Event;
import static playn.core.PlayN.*;

public class PresentSelectionMenu extends Widget<Integer>{
	private Stack<ImageView> views = new Stack<ImageView>();
	private Stack<ImageView> hiddenViews = new Stack<ImageView>();

	private SpriteView presentTypes;
	private int currentType = 0;

	public PresentSelectionMenu(final GameWorld gameWorld, final float width, final float height, final float px, final float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		
		final float presentW = width * 0.5f;
		final float presentH = height * 0.7f;

		final SpriteView v = new SpriteView("sprites/present_types.json");
		v.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() /2f);
				view.setScale(presentW / view.getWidth(), presentH / view.getHeight());
				view.setTranslation(px - width * 0.1f, py + height * 0.1f);
				((NatalWorld)gameWorld).getControlLayer().add(v.getLayer());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
		
		presentTypes = v;

	} 
	
	private void changePresent(int type) {
		presentTypes.changeSprite("" + type);
	}

	@Override
	public void setValue(Integer value) {
		if(value != currentType) {
			changePresent(value);
		}
	}

	@Override
	public Integer getValue() {
		return null;
	}

	public void initLayer(View view) {
		((NatalWorld)world).getControlLayer().add(view.getLayer());
	}

	@Override
	public void initPreLoad(GameWorld gameWorld) {
		((NatalWorld)gameWorld).getControlLayer().add(getView().getLayer());
	}

	@Override
	protected View createView() {
		return new ImageView("images/next_present_frame.png");
	}

	@Override
	protected void destroy(GameWorld gameWorld) {
	}


}
