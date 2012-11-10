package pt.bombap.playn.natal.core;

import pt.bombap.playn.extensions.sprites.Sprite;

public class ChimneyView extends ObjectView {
	public static String IMAGE = "sprites/Block-Normal.png";
	public static String JSON = "sprites/chimneys.json";

	
	public ChimneyView() {
		super(JSON, Sprite.Origin.TOPLEFT);

	}
	
		

}
