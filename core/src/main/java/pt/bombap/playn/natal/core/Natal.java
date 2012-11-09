package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;
import static pt.bombap.playn.natal.core.MathUtils.*;

import javax.sql.PooledConnection;

import org.jbox2d.collision.Distance;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.pooling.WorldPool;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class Natal implements Game {
	// scale difference between screen space (pixels) and world space (physics).
	
	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;

	private Surface terrain;
	private SurfaceLayer[] terrainLayer;

	
	// main world
	private NatalWorld world = null;


	@Override
	public void init() {
		// create and add background image layer
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);

		screenWidth = graphics().width();
		screenHeight = graphics().height();

		
		log().debug("" + screenWidth);
		log().debug("" + screenHeight);

		bgLayer.setSize(screenWidth, screenHeight);

		graphics().rootLayer().add(bgLayer);

		world = new NatalWorld(GameWorld.normalizeWidth(24));
		
		world.setDt(1.0f/40.0f);
		
		world.addEntity(new Chimney(world, 10.0f, 12.0f, 0.0f));
		world.addEntity(new Chimney(world, 15.0f, 12.0f, 0.0f));
		world.addEntity(new Chimney(world, 20.0f, 12.0f, 0.0f));
		
		world.addEntity(new Present(world, 10.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 11.5f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 12.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 13.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 14.0f, 0.0f, 0.0f));
		
		world.addEntity(new RandomCloud(world, 0.0f, 0.0f, 0.0f));
		
		world.addEntity(new DynamicClould(world, 10.f, 10.0f, 0.0f));
		
		pointer().setListener(new Pointer.Adapter(){

			@Override
			public void onPointerEnd(Event event) {
				super.onPointerEnd(event);
				Present present = new Present(world, world.getPhysUnitPerScreenUnit() * event.x(), world.getPhysUnitPerScreenUnit() * event.y(), 0.0f);
				world.addEntity(present);
				Vec2 currentV = present.getBody().getLinearVelocity();
				present.getBody().setLinearVelocity(new Vec2(10, currentV.y));
				
			}
		});

		
		//new Chimney(world, world.getWorldWidth(), world.getWorldHeight(), 90.0f * DEGTORAD);

//		terrainLayer = new SurfaceLayer[] {
//				graphics().createSurfaceLayer(screenWidth, screenHeight / 3),
//				graphics().createSurfaceLayer(screenWidth, screenHeight / 3),
//		};
//
//		terrainLayer[0].setTranslation(0, screenHeight - screenHeight / 3);
//		terrainLayer[1].setTranslation(0, screenHeight - screenHeight / 3);
//
//		graphics().rootLayer().add(terrainLayer[0]);
//		graphics().rootLayer().add(terrainLayer[1]);
//
//
//		terrain = terrainLayer[0].surface();
//
//		ObjectView o = createObjectView(ObjectType.CHIMNEY);
//		float y = terrain.height() - o.getHeight();
//		terrain.drawImage(o.getImage(), screenWidth * (1f/4), y);
//
//		o = createObjectView(ObjectType.CHIMNEY);
//		terrain.drawImage(o.getImage(), screenWidth * (3f/4), y);
//
//		o = createObjectView(ObjectType.CHIMNEY);
//		terrain.drawImage(o.getImage(), screenWidth * (7f/8), y);
//
//		o = createObjectView(ObjectType.CHIMNEY);
//		terrainLayer[1].surface().drawImage(o.getImage(), screenWidth * (1f/2), terrainLayer[1].surface().height() - o.getImage().height());
//
//		pointer().setListener(new Pointer.Adapter(){
//
//			@Override
//			public void onPointerEnd(Event event) {
//				super.onPointerEnd(event);
//				ObjectView present = createObjectView(ObjectType.PRESENT);
//
//				drawPresent(present.getLayer(), event.x(), event.y());
//
//			}
//		});
//
//		initWorld();

	}

	public void initWorld() {
		Vec2 gravity = new Vec2(0.0f, -10.0f);
		World world = new World(gravity, true);

		

		//1
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -10f);

		Body groundBody = world.createBody(groundBodyDef);
		//1
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(50.0f, 10.0f);

		groundBody.createFixture(groundBox, 0.0f);

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0.0f, 4.0f);
		bodyDef.type = BodyType.DYNAMIC;

		Body body = world.createBody(bodyDef);

		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(1.0f, 1.0f);

		FixtureDef bodyFixture = new FixtureDef();
		bodyFixture.density = 1.0f;
		bodyFixture.friction = 0.3f;
		bodyFixture.shape = bodyShape;
		

	}

	public void drawPresent(ImageLayer present, float x, float y) {
		present.setTranslation(x, y);
		graphics().rootLayer().add(present);
	}

	public ObjectView createObjectView(ObjectType type) {
		switch (type) {
		case PRESENT:
			return new PresentView();
		case CHIMNEY:
			return new ChimneyView();
		default:
			return null;
		}

	}



	@Override
	public void paint(float alpha) {
		world.paint(alpha);
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
	float vx = 0.15f;
	float x = 0;
	int activeTerrain = 0;
	int inactiveTerrain = 1;
	@Override
	public void update(float delta) {
		world.update(delta);
//		x += vx * delta;
//
//		terrainLayer[activeTerrain].setTranslation(-x, terrainLayer[activeTerrain].transform().ty());
//		terrainLayer[inactiveTerrain].setTranslation(-x + screenWidth, terrainLayer[inactiveTerrain].transform().ty());
//
//		if(x >= screenWidth) {
//			x = 0;
//			terrainLayer[activeTerrain].setTranslation(screenWidth, terrainLayer[activeTerrain].transform().ty());
//			terrainLayer[activeTerrain].surface().clear();
//
//			Surface surf = terrainLayer[activeTerrain].surface();
//			ObjectView o = createObjectView(ObjectType.CHIMNEY);
//			float y = surf.height() - o.getHeight();
//			float prev = 1;
//
//			surf.drawImage(o.getImage(), prev = screenWidth * random() *(1f/4), y);
//
//			o = createObjectView(ObjectType.CHIMNEY);
//			surf.drawImage(o.getImage(), prev = o.getWidth() + prev + (screenWidth - prev) * random() * (2f/3), y);
//
//			o = createObjectView(ObjectType.CHIMNEY);
//			surf.drawImage(o.getImage(), o.getWidth() + prev + (screenWidth - prev) * random(), y);
//
//
//			int tmp = activeTerrain;
//			activeTerrain = inactiveTerrain;
//			inactiveTerrain = tmp;
//		}
		
	}



	@Override
	public int updateRate() {
		//return 50; // 20fps
		return 25; // 40fps
		//return 16; //60fps
	}
}
