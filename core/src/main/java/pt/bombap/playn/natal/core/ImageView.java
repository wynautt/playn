package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;
import playn.core.Assets;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class ImageView extends ObjectView {
	private ImageLayer layer;
	private float width;
	private float height;
	
	public ImageView (String imagePath, final float width, final float height) {
		this.width = width;
		this.height = height;
		
		Image image = loadImage(imagePath);
		layer = graphics().createImageLayer(image);
		image.addCallback(new ResourceCallback<Image>() {
			@Override
			public void done(Image image) {
				// since the image is loaded, we can use its width and height
				layer.setOrigin(image.width() / 2f, image.height() / 2f);
				layer.setScale(width / image.width(), height / image.height());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading image: " + err.getMessage());
			}
		});
	}
	
	
	public ImageLayer getLayer() {
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



	public Image loadImage(String path) {
		return assets().getImage(path);
	}
}
