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
	public static int worldWidth = 24;
	public static int worldHeight = 18;
	public static float physUnitPerScreenUnit = 1 / 26.666667f;
	
	public GroupLayer staticLayerBack;
	public GroupLayer dynamicLayer;
	public GroupLayer staticLayerFront;

	
	public NatalWorld() {
		super(physUnitPerScreenUnit, worldWidth, worldHeight);
		
		staticLayerBack = graphics().createGroupLayer();
		worldLayer.add(staticLayerBack);
		dynamicLayer = graphics().createGroupLayer();
		worldLayer.add(dynamicLayer);
		staticLayerFront = graphics().createGroupLayer();
		worldLayer.add(staticLayerFront);

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

	@Override
	protected void postStepUpdate() {
	}

	@Override
	protected void preStepUpdate(float delta) {
	}

	@Override
	protected void worldPaint(float delta) {
	}
}
