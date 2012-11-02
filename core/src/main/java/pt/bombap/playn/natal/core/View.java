package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;

import java.util.ArrayList;
import java.util.List;

import playn.core.Image;
import playn.core.Layer;
import playn.core.ResourceCallback;

public abstract class View {
	
	public View() {
	
	}
	
	protected Image loadImage(String name) {
		return assets().getImage(name);
	}
	
	
	
	public abstract Layer getLayer();
	
	public abstract void addCallback(ResourceCallback<? super View> callback);

	public abstract float getWidth();
	public abstract float getHeight();
	public abstract void setOrigin(float x, float y);
	public abstract void setScale(float x, float y);
	public abstract void setTranslation(float x, float y);
	public abstract void setRotation(float angle);
	
	public abstract void update(float delta);
	public abstract void paint(float alpha);
	
}
