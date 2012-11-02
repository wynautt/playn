package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;
import playn.core.Image;

public class View {

	protected Image loadImage(String name) {
		return assets().getImage("peas/images/" + name);
	}
}
