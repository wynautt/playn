package pt.bombap.playn.natal.core;

import playn.core.Image;
import playn.core.ImageLayer;
import pt.bombap.playn.extensions.sprites.Sprite;

public class PresentView extends ObjectView {
	public static String IMAGE = "sprites/Block-Gel.png";
	public static String JSON = "sprites/presents.json";

	
	public PresentView() {
		super(JSON, Sprite.Origin.CENTER);

	}
	
}


