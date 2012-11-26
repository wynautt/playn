package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;
import static pt.bombap.playn.natal.core.MathUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
import pt.bombap.playn.extensions.sprites.SpriteAnimator;

public class Natal implements Game {
	public static final float INITIAL_VELOCITY = 2.5f;

	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;

	// main world
	private NatalWorld world = null;
	float ww = 0.0f;
	float wh = 0.0f;

	private int travelledDistance = 0;
	private int activeChimneys = 0;
	private List<Float> activeChimneysPositions;

	private float worldVelocity = -INITIAL_VELOCITY;
	private float cloudVelocity = -INITIAL_VELOCITY;

	private DynamicClould cloud;

	public abstract class Level {
		public abstract void create();

		public void destroy() {
			world.destroyEntities(new Filter<Entity>() {

				@Override
				public boolean isOk(Entity e) {
					return e instanceof StaticChimney || e instanceof Present;
				}
			});
		}


		public abstract boolean isSuccess();
	}

	int currentLevelDeliveredPresents = 0;
	int currentLevelNumber = 1;

	final IStateListener<Entity> stateListener = new IStateListener<Entity>() {

		@Override
		public void notify(Entity self, int code) {
			if(code == Code.PRESENT_DELIVERED) {
				currentLevelDeliveredPresents++;
			}
		}
	};

	List<Level> levels = null;
	Level currentLevel = null;


	private Function<Float, Entity> functionGetX = new Function<Float, Entity>() {

		@Override
		public Float apply(Entity e) {
			return e.getX();
		}
	};

	private Procedure<Entity> fnSetVelocity = new Procedure<Entity>() {

		@Override
		public void apply(Entity e) {
			((DynamicPhysicsEntity)e).setLinearVelocity(worldVelocity, 0);
		}
	};

	private Filter<Entity> filterChimneys = new Filter<Entity>() {

		@Override
		public boolean isOk(Entity e) {
			return e instanceof Chimney;
		}
	};

	private Function2D<Float, Entity> fnGetMaxX = new Function2D<Float, Entity>() {

		@Override
		public Float apply(Float partial, Entity e) {
			if(partial == null) {
				return e.getX();
			}
			return e.getX() > partial ? e.getX() : partial;
		}
	};


	@Override
	public void init() {
		// create and add background image layer
		Image bgImage = assets().getImage("images/full_moon_sky1.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);

		screenWidth = graphics().width();
		screenHeight = graphics().height();


		log().debug("" + screenWidth);
		log().debug("" + screenHeight);

		bgLayer.setSize(screenWidth, screenHeight);

		graphics().rootLayer().add(bgLayer);

		world = new NatalWorld(GameWorld.normalizeWidth(24));
		world.setDt(1.0f/40.0f);

		ww = world.getWorldWidth();
		wh = world.getWorldHeight();
		
		//wind meter radius
		float wmr = 4.0f;
		WindMeter windMeter = new WindMeter(world, wmr, wmr, ww - wmr / 2.0f, 0.0f + wmr / 2.0f, 0.0f);
		windMeter.setValue(7.5f);

		levels = new ArrayList<Natal.Level>(100);
		
		levels.add(new Level() {

			@Override
			public boolean isSuccess() {
				return currentLevelDeliveredPresents == 3;
			}

			@Override
			public void create() {
				StaticChimney c;
				float w = 3f, h = 5f;

				new StaticChimney(world, w, h, ww * (1 / 4f), 12.0f, 0.0f).setStateListener(stateListener);
				new StaticChimney(world, w, h, ww * (1 / 2f), 12.0f, 0.0f).setStateListener(stateListener);
				new StaticChimney(world, w, h, ww * (3 / 4f), 12.0f, 0.0f).setStateListener(stateListener);
			}
		});
		
		levels.add(new Level() {

			@Override
			public boolean isSuccess() {
				return currentLevelDeliveredPresents == 2;
			}

			@Override
			public void create() {
				StaticChimney c;
				float w = 3f, h = 5f;
				
				cloudVelocity *= -1.5;
				cloud.setLinearVelocity(cloudVelocity, 0);

				new StaticChimney(world, w, h, ww * (1 / 4f), 14.0f, 0.0f).setStateListener(stateListener);
				new StaticChimney(world, w, h, ww * (1 / 2f), 10.0f, 0.0f).setStateListener(stateListener);
			}
		});
		
		
		currentLevel = levels.remove(0);
		currentLevel.create();
		log().debug("New level: " + currentLevelNumber);

		//		world.addEntity(new Chimney(world, 10.0f, 12.0f, 0.0f));
		//		world.addEntity(new Chimney(world, 15.0f, 12.0f, 0.0f));
		//		world.addEntity(new Chimney(world, 20.0f, 12.0f, 0.0f));
		//
		//		world.addEntity(new Present(world, 10.0f, 0.0f, 0.0f));
		//		world.addEntity(new Present(world, 11.5f, 0.0f, 0.0f));
		//		world.addEntity(new Present(world, 12.0f, 0.0f, 0.0f));
		//		world.addEntity(new Present(world, 13.0f, 0.0f, 0.0f));
		//		world.addEntity(new Present(world, 14.0f, 0.0f, 0.0f));

		//world.addEntity(new RandomCloud(world, 0.0f, 0.0f, 0.0f));
		cloud = new DynamicClould(world, 10.f, 10.0f, 0.0f);
		cloud.setLinearVelocity(cloudVelocity, 0);

		float w = 6f, h = 4f;

		SpriteView buildings = new SpriteView("sprites/buildings.json");
		Building.currentView = new SpriteAnimatorView(buildings, 2, 3);
		Building.currentView.setAnimationRate(500);
		new Building(world, w, h, 0f, world.getWorldHeight() / 2f + 0.2f, 0f);

		buildings = new SpriteView("sprites/buildings.json");
		Building.currentView = new SpriteAnimatorView(buildings, 0, 1);
		Building.currentView.setAnimationRate(600);
		new Building(world, w, h, 0f + w + 0.3f, world.getWorldHeight() / 2f, 0f);

		buildings = new SpriteView("sprites/buildings.json");
		Building.currentView = new SpriteAnimatorView(buildings, 0, 1);
		Building.currentView.setAnimationRate(300);
		new Building(world, w, h, 0f + w + 0.3f + 15f, world.getWorldHeight() / 2f, 0f);

		pointer().setListener(new Pointer.Adapter(){

			@Override
			public void onPointerEnd(Event event) {
				super.onPointerEnd(event);
				Present present = new Present(world, world.getPhysUnitPerScreenUnit() * event.x(), world.getPhysUnitPerScreenUnit() * event.y(), 0.0f);
				Vec2 currentV = present.getLinearVelocity();
				present.setLinearVelocity(cloudVelocity, currentV.y);


			}
		});

	}



	@Override
	public void paint(float alpha) {
		world.paint(alpha);
	}

	Float chimneyWidth[] = {3f, 4f, 5f};
	Float chimneyHeight[] = {5f, 6f, 7f};
	Float chimneyDistance[] = {1f, 3f, 5f};

	<T> T randomValue(T a[]) {
		return a[(int)(random() * a.length)];
	}

	int transitionDistance = 20;




	@Override
	public void update(float delta) {
		world.update(delta);
//		if(cloud.getTravelledDistance() > transitionDistance) {
//			cloudVelocity *= 1.2;
//			worldVelocity = cloudVelocity;
//			cloud.setLinearVelocity(cloudVelocity, 0);
//			transitionDistance *= 2;
//
//		}


		if(currentLevel.isSuccess()) {
			currentLevel.destroy();
			if(levels.isEmpty()) {
				log().debug("Finish!!!");
			} else {
				currentLevel = levels.remove(0);
				currentLevel.create();
				currentLevelNumber++;
				currentLevelDeliveredPresents = 0;
				log().debug("New level: " + currentLevelNumber);
			}
		}





	}




	//	@Override
	//	public void update(float delta) {
	//		world.update(delta);
	//		
	////	 	activeChimneysPositions = world.map(filterChimneys, functionGetX);
	////	 	float maxX = 0f;
	////	 	for(float x: activeChimneysPositions) {
	////	 		if(x > maxX) {
	////	 			maxX = x;
	////	 		}
	////	 	}
	//	 	
	//	 	Float maxX = world.apply(filterChimneys, fnGetMaxX);
	//	 	if(maxX == null) {
	//	 		maxX = 0f;
	//	 	}
	//	 	
	//	 	Chimney c;
	//	 	float distanceToEdge = world.getWorldWidth() - maxX;
	//	 	if(distanceToEdge > 5 + randomValue(chimneyDistance)) {
	//	 		float w = randomValue(chimneyWidth);
	//	 		float h = randomValue(chimneyHeight);
	//	 		c = new Chimney(world, w, h, world.getWorldWidth() + w / 2f, 12.0f, 0.0f);
	//	 		c.setLinearVelocity(worldVelocity, 0);
	//	 		
	//	 	}
	//	 	
	//	 	if(cloud.getTravelledDistance() > transitionDistance) {
	//	 		cloudVelocity *= 1.2;
	//	 		worldVelocity = cloudVelocity;
	//	 		cloud.setLinearVelocity(cloudVelocity, 0);
	//	 		transitionDistance *= 2;
	//	 		
	//	 		world.apply(filterChimneys, fnSetVelocity);
	//	 	}
	//	 	
	//	 	log().debug("Travelled distance: " + cloud.getTravelledDistance());
	//	
	//	}



	@Override
	public int updateRate() {
		//return 50; // 20fps
		return 25; // 40fps
		//return 16; //60fps
	}
}
