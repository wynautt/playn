package pt.bombap.playn.natal.core;

import static playn.core.PlayN.log;

import java.util.HashMap;
import java.util.Map;

import playn.core.Image;
import playn.core.Layer;
import playn.core.ResourceCallback;
import pt.bombap.playn.extensions.sprites.Sprite;
import pt.bombap.playn.extensions.sprites.SpriteLoader;

public class SpriteView extends View {

	protected Sprite sprite;
	protected int spriteIndex;
	protected boolean hasLoaded; // set to true when resources have loaded and we can update
	private static Map<String, Sprite> spritesCache = new HashMap<String, Sprite>();
	
	/**
	 * because sprite supports only one callback at a time we have to save the SpriteView callback 
	 * to invoke when the sprite callback is invoked
	 */
	private ResourceCallback<? super View> callback = null;

	public SpriteView(final String spriteDefPath) {
		spriteIndex = 0;
		hasLoaded = false;

		if(spritesCache.containsKey(spriteDefPath)) {
			sprite = spritesCache.get(spriteDefPath).clone();
		} else {
			sprite = SpriteLoader.getSprite(spriteDefPath);
		}

		// Add a callback for when the image loads.
		// This is necessary because we can't use the width/height (to center the
		// image) until after the image has been loaded
		sprite.addCallback(new ResourceCallback<Sprite>() {
			@Override
			public void done(Sprite sprite) {
				sprite.setSprite(spriteIndex);
				setSpriteOrigin(Sprite.Origin.CENTER);
				//sprite.layer().setTranslation(x, y);
				//peaLayer.add(sprite.layer());
				hasLoaded = true;
				if(!spritesCache.containsKey(spriteDefPath)) {
					spritesCache.put(spriteDefPath, sprite);
				}
				
				if(callback != null) {
					callback.done(SpriteView.this);
				}
			}

			@Override
			public void error(Throwable err) {
				log().error("Error loading image!", err);
			}
		});
	}

	private void setSpriteOrigin(Sprite.Origin origin) {
		switch (origin) {
		case CENTER:
			sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
			break;
		case BOTTOMRIGHT:
			sprite.layer().setOrigin(sprite.width(), sprite.height());
			break;
		case TOPLEFT:
			log().debug("set top left");
			//default
		}
	}
	
	public void setRandomSprite() {
		sprite.setRandomSprite();
	}
	
	public void changeSprite(int index) {
		sprite.setSprite(index);
	}
	
	public void changeSprite(String id) {
		sprite.setSprite(id);
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public boolean isReady() {
		return hasLoaded;
	}


	@Override
	public Layer getLayer() {
		return sprite.layer();
	}

	@Override
	public void addCallback(final ResourceCallback<? super View> callback) {
		if(isReady()) {
			callback.done(this);
		} else {
			this.callback = callback;
		}
	}
	
	/**
	 * 
	 * @return current image displayed in the sprite
	 */
	public Image getImage() {
		return sprite.layer().image();
	}

	@Override
	public float getWidth() {
		return getImage().width();
	}

	@Override
	public float getHeight() {
		return getImage().height();
	}

	@Override
	public void setOrigin(float x, float y) {
		getLayer().setOrigin(x, y);
	}

	@Override
	public void setScale(float x, float y) {
		getLayer().setScale(x, y);
	}

	@Override
	public void setTranslation(float x, float y) {
		getLayer().setTranslation(x, y);
	}

	@Override
	public void setRotation(float angle) {
		getLayer().setRotation(angle);
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void paint(float alpha) {
	}

}
