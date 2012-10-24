package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.CanvasLayer;
import playn.core.Color;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class Natal implements Game {
	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;
	
	private Surface terrain;
	private SurfaceLayer terrainLayer;
	private ImageLayer image;

	@Override
	public void init() {
		// create and add background image layer
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);

		screenWidth = graphics().width();
		screenHeight = graphics().height();
		
		log().debug("" + (screenHeight - screenHeight / 3));
		log().debug("" + screenWidth * (3/4));
		
		bgLayer.setSize(screenWidth, screenHeight);

		graphics().rootLayer().add(bgLayer);

		terrainLayer = graphics().createSurfaceLayer(screenWidth, screenHeight / 3);
		terrainLayer.setTranslation(0, screenHeight - screenHeight / 3);
		terrainLayer.setScale(1, 1);
		graphics().rootLayer().add(terrainLayer);
		
		terrain = terrainLayer.surface();
		
		Image o = createObjectView(ObjectType.CHIMNEY);
		terrain.drawImage(o, screenWidth * (1f/4), terrain.height() - o.height());
		
		o = createObjectView(ObjectType.CHIMNEY);
		terrain.drawImage(o, screenWidth * (3f/4), terrain.height() - o.height());
		
		o = createObjectView(ObjectType.CHIMNEY);
		terrain.drawImage(o, screenWidth * (7f/8), terrain.height() - o.height());
		

		
		
	}


	public Image createObjectView(ObjectType type) {
		float w = screenWidth / 10;
		float h = screenHeight / 4;

		CanvasImage chimney = graphics().createImage(w, h);
		Canvas canvas = chimney.canvas();
		canvas.setFillColor(Color.rgb(0, 0, 255));
		canvas.fillRect(0, 0, w, h);
		return chimney;
	}



	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything here!
		//		float rot = (this.newRotation * alpha) + (this.rotation * (1f - alpha));
		//
		//		graphics().rootLayer().get(1).setRotation(rot);
		//
		//		graphics().rootLayer().get(2).setRotation(graphics().rootLayer().get(2).transform().rotation() + ((float)(Math.PI / 8f)) / 2.7f);
		//
		//		log().debug("paint called");
	}

	float rotation = 0;
	float newRotation = 0;
	float vx = 0.1f;
	float x = 0;
	@Override
	public void update(float delta) {
		x += vx * delta;
	
		terrainLayer.setTranslation(-x, terrainLayer.transform().ty());
		
		//terrain.setFillColor(Color.rgb(255, 0, 0));
		//terrain.fillRect(10, 10, terrain.width()-10, terrain.height()-10);
	}

	@Override
	public int updateRate() {
		//return 50; // 20fps
		return 0;
	}
}
