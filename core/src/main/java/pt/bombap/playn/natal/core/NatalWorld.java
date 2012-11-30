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

public class NatalWorld extends GameWorld {
		
	public static int worldWidth = 32;
	public static int worldHeight = 24;
	//public static float physUnitPerScreenUnit = 1 / 26.666667f;
	public static float physUnitPerScreenUnit = 1 / 20.0f;

	private GroupLayer backgroundLayer;

	private GroupLayer staticLayerBack;
	private GroupLayer dynamicLayer;
	private GroupLayer staticLayerFront;
	private GroupLayer controlLayer;
	private GroupLayer menuLayer;

	public NatalWorld(int width) {
		super(width);
		init();
	}

	public NatalWorld() {
		super(physUnitPerScreenUnit, worldWidth, worldHeight);
		init();
	}

	public void init() {
		backgroundLayer = graphics().createGroupLayer();
		worldLayer.add(backgroundLayer);
		staticLayerBack = graphics().createGroupLayer();
		worldLayer.add(staticLayerBack);
		dynamicLayer = graphics().createGroupLayer();
		worldLayer.add(dynamicLayer);
		staticLayerFront = graphics().createGroupLayer();
		worldLayer.add(staticLayerFront);
		controlLayer = graphics().createGroupLayer();
		worldLayer.add(controlLayer);
		menuLayer = graphics().createGroupLayer();
		worldLayer.add(menuLayer);

		// create the ground
		Body ground = world.createBody(new BodyDef());
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsEdge(new Vec2(0, worldHeight), new Vec2(worldWidth, worldHeight));
		ground.createFixture(groundShape, 0.0f);

		// create the walls
		Body wallLeft = world.createBody(new BodyDef());
		PolygonShape wallLeftShape = new PolygonShape();
		wallLeftShape.setAsEdge(new Vec2(0, 0), new Vec2(0, worldHeight));
		wallLeft.createFixture(wallLeftShape, 0.0f);
		Body wallRight = world.createBody(new BodyDef());
		PolygonShape wallRightShape = new PolygonShape();
		wallRightShape.setAsEdge(new Vec2(worldWidth, 0), new Vec2(worldWidth, worldHeight));
		wallRight.createFixture(wallRightShape, 0.0f);

	}
	
	public GroupLayer getBackgroundLayer() {
		return backgroundLayer;
	}

	public GroupLayer getStaticLayerBack() {
		return staticLayerBack;
	}

	public GroupLayer getDynamicLayer() {
		return dynamicLayer;
	}

	public GroupLayer getStaticLayerFront() {
		return staticLayerFront;
	}
	
	public GroupLayer getControlLayer() {
		return controlLayer;
	}
	
	public GroupLayer getMenuLayer() {
		return menuLayer;
	}


}
