package pt.bombap.playn.natal.core;

import static playn.core.PlayN.graphics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import playn.core.CanvasImage;
import playn.core.DebugDrawBox2D;
import playn.core.GroupLayer;

public abstract class GameWorld implements ContactListener {
	private static boolean showDebugDraw = false;

	protected GroupLayer worldLayer;
	
	protected float physUnitPerScreenUnit = 1 / 26.666667f;

	// size of world
	protected int worldWidth = 24;
	protected int worldHeight = 18;

	protected float dt = 1.0f / 60.0f;
	protected int positionIterations = 10;
	protected int velocityIterations = 10;

	// box2d object containing physics world
	protected World world;


	private DebugDrawBox2D debugDraw;


	//worldLayer is a scaled layer
	public GameWorld(float physUnitPerScreenUnit, int width, int height) {

		this.physUnitPerScreenUnit = physUnitPerScreenUnit;
		this.worldWidth = width;
		this.worldHeight = height;
		
		worldLayer = graphics().createGroupLayer();
		worldLayer.setScale(1f / physUnitPerScreenUnit);
		graphics().rootLayer().add(worldLayer);

		// create the physics world
		world = new World(new Vec2(0.0f, 10.0f), true);
		world.setWarmStarting(true);
		world.setAutoClearForces(true);
		world.setContactListener(this);


		if (showDebugDraw) {
			CanvasImage image = graphics().createImage((int) (worldWidth / physUnitPerScreenUnit),
					(int) (worldHeight / physUnitPerScreenUnit));
			graphics().rootLayer().add(graphics().createImageLayer(image));
			debugDraw = new DebugDrawBox2D();
			debugDraw.setCanvas(image);
			debugDraw.setFlipY(false);
			debugDraw.setStrokeAlpha(150);
			debugDraw.setFillAlpha(75);
			debugDraw.setStrokeWidth(2.0f);
			debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
			debugDraw.setCamera(0, 0, 1f / physUnitPerScreenUnit);
			world.setDebugDraw(debugDraw);
		}

	}

	public void update(float delta) {
		preStepUpdate(delta);

		// the step delta is fixed so box2d isn't affected by framerate
		world.step(dt, velocityIterations, positionIterations);
		postStepUpdate();
	}

	public void paint(float delta) {
		if (showDebugDraw) {
			debugDraw.getCanvas().clear();
			world.drawDebugData();
		}
		worldPaint(delta);
	}


	protected abstract void postStepUpdate();
	protected abstract void preStepUpdate(float delta);
	protected abstract void worldPaint(float delta);


	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	public static void setShowDebugDraw(boolean showDebugDraw) {
		GameWorld.showDebugDraw = showDebugDraw;
	}

	public void setPhysUnitPerScreenUnit(float physUnitPerScreenUnit) {
		this.physUnitPerScreenUnit = physUnitPerScreenUnit;
	}

	public void setWorldWidth(int worldWidth) {
		this.worldWidth = worldWidth;
	}

	public void setWorldHeight(int worldHeight) {
		this.worldHeight = worldHeight;
	}

	public void setDt(float dt) {
		this.dt = dt;
	}

	public void setPositionIterations(int positionIterations) {
		this.positionIterations = positionIterations;
	}

	public void setVelocityIterations(int velocityIterations) {
		this.velocityIterations = velocityIterations;
	}

	public GroupLayer getWorldLayer() {
		return worldLayer;
	}

	public float getPhysUnitPerScreenUnit() {
		return physUnitPerScreenUnit;
	}

	public World getWorld() {
		return world;
	}



}
