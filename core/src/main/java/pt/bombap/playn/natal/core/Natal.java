package pt.bombap.playn.natal.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.random;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.Pointer.Event;

public class Natal implements Game {
	public static final float MAX_VELOCITY = 20f;
	public static final float MIN_VELOCITY = 2f;
	public static final float MAX_METER_VELOCITY = 30f;
	public static final float MIN_METER_VELOCITY = 0f;
	public static final int MAX_LIFES = 3;


	public static final float INITIAL_VELOCITY = -2f;

	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;

	// main world
	private NatalWorld world = null;
	float ww = 0.0f;
	float wh = 0.0f;
	float wGroundTop = 0.0f;
	float wGroundHeight = 0.0f;
	float wSkyHeight = 0.0f;
	int lifes = MAX_LIFES;

	private LifesCounter lifesCounter;
	private PresentSelectionMenu presentSelectionMenu; 

	private int travelledDistance = 0;
	private int activeChimneys = 0;
	private List<Float> activeChimneysPositions;

	private ImageLayer gameAreaLayer;

	private float worldVelocity = -INITIAL_VELOCITY;
	private float cloudVelocity = -INITIAL_VELOCITY;

	private WindMeter windMeter;

	private Menu startMenu;

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
		
		public abstract int nextPresentType();
	}

	int currentLevelDeliveredPresents = 0;
	int currentLevelNumber = 1;

	final IStateListener<Entity> stateListener = new IStateListener<Entity>() {

		@Override
		public void notify(Entity self, int code) {
			switch (code) {
			case Code.PRESENT_DELIVERED:
				currentLevelDeliveredPresents++;
				presentSelectionMenu.setValue(currentLevel.nextPresentType());
				break;
			case Code.OUT_OF_WORLD:
				lifesCounter.setValue(-1);
				lifes--;
			default:
				break;
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

	/**
	 * world ground height
	 * @return
	 */
	public float wgh(float percent) {
		return wGroundHeight * percent + wGroundTop;
	}

	public float ww(float percent) {
		return ww * percent;
	}

	/**
	 * @return world sky height
	 */
	public float wsh(float percent) {
		return wSkyHeight * percent;
	}

	public float windSpeedToMeterSpeed(float windSpeed) {
		if(windSpeed < 0) windSpeed = -windSpeed;
		return windSpeed * MAX_METER_VELOCITY / MAX_VELOCITY;
	}

	public float meterSpeedToWindSpeedTo(float meterSpeed) {
		return meterSpeed * MAX_VELOCITY / MAX_METER_VELOCITY;
	}

	@Override
	public void init() {
		// create and add background image layer
		Image bgImage = assets().getImage("images/full_moon_sky_16_9.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);

		Image gameAreaImage = assets().getImage("images/touch_area.png");
		gameAreaLayer = graphics().createImageLayer(gameAreaImage);

		screenWidth = graphics().width();
		screenHeight = graphics().height();


		log().debug("" + screenWidth);
		log().debug("" + screenHeight);

		//bgLayer.setSize(screenWidth, screenHeight);
		//graphics().rootLayer().add(bgLayer);


		startMenu = new Menu();

		world = new NatalWorld(GameWorld.normalizeWidth(24));
		world.setDt(1.0f/40.0f);

		ww = world.getWorldWidth();
		wh = world.getWorldHeight();

		wGroundTop = wh * (2 / 3f);
		wGroundHeight = wh - wGroundTop;
		wSkyHeight = wGroundTop;

		bgLayer.setSize(ww, wh);
		world.getBackgroundLayer().add(bgLayer);

		gameAreaLayer.setSize(ww, wsh(0.20f));
		world.getBackgroundLayer().add(gameAreaLayer);

		//wind meter diameter
		float wmd = 3.0f;
		windMeter = new WindMeter(world, wmd, wmd, ww - wmd / 2.0f, 0.0f + wmd / 2.0f, 0.0f);
		windMeter.setValue(windSpeedToMeterSpeed(cloudVelocity));

		float nextPresentFrameW = 0.8f * wmd;
		float nextPresentFrameH = 0.6f * wmd;
		presentSelectionMenu = new PresentSelectionMenu(world, nextPresentFrameW, nextPresentFrameH, ww - nextPresentFrameW / 1.8f, wmd * 1.4f, 0f);

		float lifesW = 0.2f * wmd;
		float lifesH = 0.2f * wmd;

		lifesCounter = new LifesCounter(world, lifesW, lifesH, ww -  lifesW / 2.0f, wmd * 1.9f, 0.0f);
		lifesCounter.setValue(lifes);

		
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

				//cloudVelocity = 10f;
				cloud.setLinearVelocity(cloudVelocity, 0);
				windMeter.setValue(windSpeedToMeterSpeed(cloudVelocity));
				presentSelectionMenu.setValue(Code.PRESENT_G1);
			
				//TODO: the creation of entities should be handled by the game world. Game world would accept all the listeners
				// and each entity would have only a listener that notifies the game world with a code. Game world would use
				// that code to identify which listeners would have been registered in the world for that event.
				new StaticChimney.SimpleTall(world, ww * (1 / 4f), wh, 0.0f).setStateListener(stateListener);
				new StaticChimney.Simple(world, ww * (1 / 2f), wh, 0.0f).setStateListener(stateListener);
				new StaticChimney.Simple(world, ww * (3 / 4f), wh, 0.0f).setStateListener(stateListener);
			}

			@Override
			public int nextPresentType() {
				return Code.PRESENT_G1;
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

				cloudVelocity = -5f;
				cloud.setLinearVelocity(cloudVelocity, 0);
				windMeter.setValue(windSpeedToMeterSpeed(cloudVelocity));
				presentSelectionMenu.setValue(Code.PRESENT_G1);

				new StaticChimney.SimpleFat(world, ww * (1 / 4f), wgh(0.25f), 0.0f).setStateListener(stateListener);
				new StaticChimney.SimpleFat(world, ww * (1 / 2f), wgh(0.5f), 0.0f).setStateListener(stateListener);
			}

			@Override
			public int nextPresentType() {
				return Code.PRESENT_G1;
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

				cloudVelocity = 10;
				cloud.setLinearVelocity(cloudVelocity, 0);
				windMeter.setValue(windSpeedToMeterSpeed(cloudVelocity));
				presentSelectionMenu.setValue(Code.PRESENT_G1);

				new StaticChimney.SimpleTall(world, ww * (1 / 4f), wgh(0.25f), 0.0f).setStateListener(stateListener);
				new StaticChimney.SimpleTall(world, ww * (1 / 2f), wgh(0.5f), 0.0f).setStateListener(stateListener);
			}

			@Override
			public int nextPresentType() {
				if(currentLevelDeliveredPresents == 0) return Code.PRESENT_G1;
				
				return Code.PRESENT_G1000;
			}
		});


		cloud = new DynamicClould(world, 10.f, 10.0f, 0.0f);
		cloud.setLinearVelocity(cloudVelocity, 0);

		float w = 5f, h = 3f;

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

		gameAreaLayer.addListener(new Pointer.Listener() {

			@Override
			public void onPointerStart(Event event) {

			}

			@Override
			public void onPointerEnd(Event event) {
				int presentType = currentLevel.nextPresentType();
				Present present = new Present(world, world.getPhysUnitPerScreenUnit() * event.x(), world.getPhysUnitPerScreenUnit() * event.y(), 0.0f);
				present.setStateListener(stateListener);
				Vec2 currentV = present.getLinearVelocity();
				present.setLinearVelocity(cloudVelocity, currentV.y);
				present.setType(presentType);
			}

			@Override
			public void onPointerDrag(Event event) {
			}
		});

		currentLevel = levels.remove(0);
		currentLevel.create();
		log().debug("New level: " + currentLevelNumber);

		activatePointer();

	}



	@Override
	public void paint(float alpha) {
		if(!showingNextLevelMenu) {
			world.paint(alpha);
		}
	}

	Float chimneyWidth[] = {3f, 4f, 5f};
	Float chimneyHeight[] = {5f, 6f, 7f};
	Float chimneyDistance[] = {1f, 3f, 5f};

	<T> T randomValue(T a[]) {
		return a[(int)(random() * a.length)];
	}

	int transitionDistance = 20;

	private void activatePointer() {
		gameAreaLayer.setInteractive(true);
	}

	private void deactivatePointer() {
		gameAreaLayer.setInteractive(false);
	}

	public void replayLevel() {
		activatePointer();
		currentLevel.destroy();
		currentLevel.create();
		currentLevelDeliveredPresents = 0;
		lifesCounter.setValue(MAX_LIFES - lifes);
		lifes = MAX_LIFES;
		log().debug("Replay level: " + currentLevelNumber);

		showingNextLevelMenu = false;
	}

	public void startNextLevel() {
		activatePointer();
		currentLevel = levels.remove(0);
		currentLevel.create();
		currentLevelNumber++;
		currentLevelDeliveredPresents = 0;
		lifesCounter.setValue(MAX_LIFES - lifes);
		lifes = MAX_LIFES;

		log().debug("New level: " + currentLevelNumber);

		showingNextLevelMenu = false;
	}

	boolean showingNextLevelMenu = false;
	NextLevelMenu nextLevelMenu;
	int[] nextLevelMenus = {NextLevelMenu.NEXT_1_STAR, NextLevelMenu.NEXT_2_STAR, NextLevelMenu.NEXT_3_STAR};

	@Override
	public void update(float delta) {
		if(!showingNextLevelMenu) {
			world.update(delta);

			if(lifes <= 0) {
				deactivatePointer();
				log().debug("Game over.");
				nextLevelMenu = new NextLevelMenu(world.getMenuLayer());
				nextLevelMenu.init(NextLevelMenu.GAME_OVER);
				showingNextLevelMenu = true;
			}

			if(currentLevel.isSuccess()) {
				currentLevel.destroy();
				if(levels.isEmpty()) {
					log().debug("Finish!!!");
				} else {
					deactivatePointer();
					nextLevelMenu = new NextLevelMenu(world.getMenuLayer());
					nextLevelMenu.init(nextLevelMenus[lifes - 1]);
					showingNextLevelMenu = true;

				}
			}
		
		}
	}

	public class NextLevelMenu {
		public static final int NEXT_3_STAR = 1;
		public static final int NEXT_2_STAR = 2;
		public static final int NEXT_1_STAR = 4;
		public static final int NEXT_0_STAR = 8;
		public static final int GAME_OVER = 16;

		private GroupLayer nextLevelLayer;


		public NextLevelMenu(GroupLayer menuLayer) {
			nextLevelLayer = graphics().createGroupLayer();
			nextLevelLayer.setTranslation(ww/2f, wh/2f);
			menuLayer.add(nextLevelLayer);
		}

		private boolean is(int state, int flag) {
			return (state & flag) != 0; 
		}


		public void init(int state) {
			float iconW = 2f;
			float iconH = 2f;
			float textW = 5.5f;
			float textH = 3.5f;
			float menuW = 9f;
			float menuH = 8f;
			float iconDistance = 1.5f;

			ImageView menu = is(state, NEXT_1_STAR) ? new ImageView("images/menu_1star.png") : is(state, NEXT_2_STAR) ? new ImageView("images/menu_2star.png") : is(state, NEXT_3_STAR) ? new ImageView("images/menu_3star.png") : is(state, GAME_OVER) ? new ImageView("images/menu_gameover.png") : null;
			menu.setOrigin(menu.getWidth() / 2f, menu.getHeight() / 2f);
			menu.setScale(menuW / menu.getWidth(), menuH / menu.getHeight());
			nextLevelLayer.add(menu.getLayer());

			if(is(state, NEXT_3_STAR | NEXT_2_STAR | NEXT_1_STAR)) {
				ImageView next = new ImageView("images/next.png");
				next.setOrigin(next.getWidth() / 2f, next.getHeight() / 2f);
				next.setScale(iconW / next.getWidth(), iconW / next.getHeight());
				next.setTranslation(menuW/2f, 0f);
				nextLevelLayer.add(next.getLayer());

				next.getLayer().addListener(new Pointer.Listener() {

					@Override
					public void onPointerStart(Event event) {

					}

					@Override
					public void onPointerEnd(Event event) {
						nextLevelLayer.destroy();
						startNextLevel();
					}

					@Override
					public void onPointerDrag(Event event) {
					}
				});
			}

			if(is(state, GAME_OVER | NEXT_1_STAR | NEXT_2_STAR | NEXT_3_STAR)) {
				ImageView replay = new ImageView("images/replay.png");
				replay.setOrigin(replay.getWidth() / 2f, replay.getHeight() / 2f);
				replay.setScale(iconW / replay.getWidth(), iconH / replay.getHeight());
				replay.setTranslation(-menuW/2f, 0f);
				nextLevelLayer.add(replay.getLayer());

				replay.getLayer().addListener(new Pointer.Listener() {

					@Override
					public void onPointerStart(Event event) {

					}

					@Override
					public void onPointerEnd(Event event) {
						nextLevelLayer.destroy();
						replayLevel();
					}

					@Override
					public void onPointerDrag(Event event) {
					}
				});
			}

			ImageView text = is(state, NEXT_1_STAR | NEXT_2_STAR | NEXT_3_STAR) ? new ImageView("images/text_welldone.png") : is(state, GAME_OVER) ? new ImageView("images/text_gameover.png") : null;
			text.setOrigin(text.getWidth() / 2f, text.getHeight() / 2f);
			text.setScale(textW / text.getWidth(), textH / text.getHeight());
			text.setTranslation(0f, -menuH/2f * (1/6f));
			nextLevelLayer.add(text.getLayer());

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
