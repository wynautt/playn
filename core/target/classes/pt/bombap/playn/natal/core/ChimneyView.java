package pt.bombap.playn.natal.core;

import playn.core.Image;

public class ChimneyView extends ObjectView {
	private Image image;

	public ChimneyView(Image image) {
		this.image = image;
	}
	
	public Image getImage() {
		return image;
	}

	@Override
	public float getWidth() {
		return this.image.width();
	}

	@Override
	public float getHeight() {
		return this.image.height();
	}
	
	

}
