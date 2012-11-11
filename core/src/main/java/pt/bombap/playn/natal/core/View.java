package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;

import java.util.ArrayList;
import java.util.List;

import playn.core.Connection;
import playn.core.Image;
import playn.core.Layer;
import playn.core.ResourceCallback;
import playn.core.Pointer.Listener;
import playn.core.Touch.LayerListener;

public abstract class View {
	//private Layer layer;
	//TODO: source > generate delegated methods from layer	

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
