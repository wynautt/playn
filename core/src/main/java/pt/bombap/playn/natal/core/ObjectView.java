package pt.bombap.playn.natal.core;

import static playn.core.PlayN.log;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.ResourceCallback;
import pt.bombap.playn.extensions.sprites.Sprite;
import pt.bombap.playn.extensions.sprites.SpriteLoader;


//TODO: rename to sprite-animator or something similar
public class ObjectView {

	protected Sprite sprite;
	protected int spriteIndex;
	protected boolean hasLoaded; // set to true when resources have loaded and we can update

	public ObjectView() {
		
	}
	
	public ObjectView(String configPath, final Sprite.Origin origin) {
		this(null, configPath, origin);
	}
	
	public ObjectView(String imagePath, String configPath, final Sprite.Origin origin) {
		super();

		spriteIndex = 0;
		hasLoaded = false;

		if(imagePath == null) {
			sprite = SpriteLoader.getSprite(configPath);
		} else {
			sprite = SpriteLoader.getSprite(imagePath, configPath);
		}

		// Add a callback for when the image loads.
		// This is necessary because we can't use the width/height (to center the
		// image) until after the image has been loaded
		sprite.addCallback(new ResourceCallback<Sprite>() {
			@Override
			public void done(Sprite sprite) {
				sprite.setSprite(spriteIndex);
				setSpriteOrigin(origin);
				//sprite.layer().setTranslation(x, y);
				//peaLayer.add(sprite.layer());
				hasLoaded = true;
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


	public boolean isReady() {
		return hasLoaded;
	}

	public ImageLayer getLayer() {
		return sprite.layer();
	}
	
	public Image getImage() {
		return sprite.layer().image();
	}

	
	public float getWidth() {
		return getImage().width();
	}

	public float getHeight() {
		return getImage().height();
	}
	
	public void setTranslation(float x, float y) {
		
	}
	
	public void setRotation(float angle) {
		
	}


}
