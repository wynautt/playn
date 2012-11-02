package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;
import playn.core.Assets;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class ImageView extends View {
	private ImageLayer layer;
	
	public ImageView (String imagePath) {
		Image image = loadImage(imagePath); 
		layer = graphics().createImageLayer(image);
	}
	
	public Layer getLayer() {
		return layer;
	}

	@Override
	public void setTranslation(float x, float y) {
		layer.setTranslation(x, y);
	}

	@Override
	public void setRotation(float angle) {
		layer.setRotation(angle);
	}


	@Override
	public void setOrigin(float x, float y) {
		layer.setOrigin(x, y);
	}


	@Override
	public void setScale(float x, float y) {
		layer.setScale(x, y);
	}


	@Override
	public void addCallback(final ResourceCallback<? super View> callback) {
		layer.image().addCallback(new ResourceCallback<Image>() {

			@Override
			public void done(Image resource) {
				callback.done(ImageView.this);
			}

			@Override
			public void error(Throwable err) {
				callback.error(err);
			}
			
		});
	}


	@Override
	public float getWidth() {
		return layer.image().width();
	}


	@Override
	public float getHeight() {
		return layer.image().height();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void paint(float alpha) {
	}

	
}
