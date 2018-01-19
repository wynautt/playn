package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
import playn.core.PlayN;

/**
 * default axis orientation
 *   -----> x
 *  |
 *  |
 *  |
 *  |
 *  y
 * @author admin
 *
 */
public abstract class GameWorld implements IGameWorld, ContactListener {
	private static boolean showDebugDraw = false;

	// main layer that holds the world. note: this gets scaled to world space
	protected GroupLayer worldLayer;

	protected List<Entity> entities = new ArrayList<Entity>(10);
	protected List<Entity> dirtyEntities = new ArrayList<Entity>(5);
	
	protected List<IStateListener<Entity>> outOfWorldListeners = new ArrayList<IStateListener<Entity>>(10);

	protected HashMap<Body, PhysicsEntity> bodyEntityTable = new HashMap<Body, PhysicsEntity>();
	protected Stack<Contact> contacts = new Stack<Contact>();


	//default for 640x480
	//screenWidth = (1 / physUnitPerScreenUnit) * worldWidth 
	//screenHeight = (1 / physUnitPerScreenUnit) * worldHeight
	protected float physUnitPerScreenUnit = 1 / 26.666667f;

	// size of world
	protected int worldWidth = 24;
	protected int worldHeight = 18;

	protected float dt = 1.0f / 60.0f;
	protected int positionIterations = 10;
	protected int velocityIterations = 10;

	// box2d object containing physics world
	protected World world;

	private OutOfWorldEntityFilter outOfWorldEntityFilter = new OutOfWorldEntityFilter();

	private DebugDrawBox2D debugDraw;

	public class OutOfWorldEntityFilter implements Filter<Entity> {

		@Override
		public boolean isOk(Entity entity) {
			float x = entity.getView().getLayer().transform().tx();

			if(x > getWorldWidth() + entity.getWidth() / 2f) return true;
			if(x < -entity.getWidth() / 2f) return true;

			float y = entity.getView().getLayer().transform().ty();

			if(y > getWorldHeight() + entity.getHeight() / 2f) return true;
			if(y < -entity.getHeight() / 2f) return true;

			return false;
		}

	}

	public static int normalizeWidth(int width) {
		float ratio = (float)graphics().width() / graphics().height();

		while(((int)((width++ / ratio) * 100)) % 100 > 9);

		return width - 1;

	}

	public GameWorld(int width) {
		this((float)width / graphics().width(), width, (int)(graphics().height() * ((float)width / graphics().width())));

	}

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
	
	public static void showDebug(boolean show) {
		showDebugDraw = show;
	}

	public void update(float delta) {
		preStepUpdate(delta);

		// the step delta is fixed so box2d isn't affected by framerate
		world.step(dt, velocityIterations, positionIterations);
		postStepUpdate(delta);
		//log().debug("Number of entities: " + entities.size());
	}

	public void paint(float delta) {
		if (showDebugDraw) {
			debugDraw.getCanvas().clear();
			world.drawDebugData();
		}
		worldPaint(delta);
	}

	public void preStepUpdate(float delta) {
		for(Entity e: entities) {
			e.update(delta);
		}
	}

	public void postStepUpdate(float delta) {
		processContacts();
		for(Entity e: dirtyEntities) {
			removeEntity(e);
		}
		dirtyEntities.clear();
	}

	public void worldPaint(float alpha) {
		for(Entity e: entities) {
			e.paint(alpha);
		}
	}




	@Override
	public void beginContact(Contact contact) {
		contacts.push(contact);
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

	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		if (entity instanceof PhysicsEntity) {
			PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			bodyEntityTable.put(physicsEntity.getBody(), physicsEntity);
		}
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
		if (entity instanceof PhysicsEntity) {
			PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			bodyEntityTable.remove(physicsEntity.getBody());
		}
	}

	public void markEntityDirty(Entity entity) {
		dirtyEntities.add(entity);
	}

	/**
	 * 
	 * @param filter
	 * @return null if no entity is valid according to the filter. A list of entities otherwise.
	 */
	public List<Entity> getEntities(Filter<? super Entity> filter) {
		List<Entity> result = null;
		for(Entity e: entities) {
			if(filter.isOk(e)) {
				if(result == null) {
					result = new ArrayList<Entity>(1);
				}
				result.add(e);
			}
		}

		return result;
	}

	public <T> List<T> map(Function<T, ? super Entity> function) {
		List<T> result = new ArrayList<T>(entities.size());

		for(Entity e: entities) {
			result.add(function.apply(e));
		}

		return result;
	}

	public <T> List<T> map(Filter<? super Entity> filter, Function<T, ? super Entity> function) {
		List<T> result = new ArrayList<T>(0);

		for(Entity e: entities) {
			if(filter.isOk(e)) {
				result.add(function.apply(e));
			}
		}

		return result;
	}

	public <T> T apply(Function2D<T, ? super Entity> function) {
		T parcialResult = null;

		for(Entity e: entities) {
			parcialResult = function.apply(parcialResult, e); 
		}

		return parcialResult;
	}

	public <T> T apply(Filter<? super Entity> filter, Function2D<T, ? super Entity> function) {
		T parcialResult = null;

		for(Entity e: entities) {
			if(filter.isOk(e)) {
				parcialResult = function.apply(parcialResult, e);
			}
		}

		return parcialResult;
	}
	
	public void apply(Procedure<? super Entity> fn) {
		for(Entity e: entities) {
			fn.apply(e); 
		}
	}

	public void apply(Filter<? super Entity> filter, Procedure<? super Entity> fn) {
		for(Entity e: entities) {
			if(filter.isOk(e)) {
				fn.apply(e);
			}
		}
	}

	public void destroyEntities(Filter<? super Entity> filter) {
		List<Entity> toRemove = null;

		for(Entity e: entities) {
			if(filter.isOk(e)) {
				if(toRemove == null) {
					toRemove = new ArrayList<Entity>(1);
				}
				toRemove.add(e);				
			}
		}

		if(toRemove != null) {
			for(Entity e: toRemove) {
				log().debug("out: " + e);
				removeEntity(e);
				e.destroy(this);
			}
		}

	}

	public void destroyOutOfWorldEntities() {
		destroyEntities(outOfWorldEntityFilter);
	}

	public void destroyOutOfWorldEntities(Filter<Entity> filter) {
		destroyEntities(new AndFilter<Entity>(filter, outOfWorldEntityFilter));
	}
	
	public enum EntityState {
		OUT_OF_WORLD
	}
	
	public void addListener(EntityState state, IStateListener<Entity> listener) {
		switch (state) {
		case OUT_OF_WORLD:
			outOfWorldListeners.add(listener);
			break;

		default:
			break;
		}
	}


	// handle contacts out of physics loop
	public void processContacts() {
		while (!contacts.isEmpty()) {
			Contact contact = contacts.pop();

			// handle collision
			PhysicsEntity entityA = bodyEntityTable.get(contact.m_fixtureA.m_body);
			PhysicsEntity entityB = bodyEntityTable.get(contact.m_fixtureB.m_body);

			if (entityA != null && entityB != null) {
				if (entityA instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityA).contact(entityB, contact.getFixtureA(), contact.getFixtureB(), contact);
				}
				if (entityB instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityB).contact(entityA, contact.getFixtureB(), contact.getFixtureA(), contact);
				}
			}
		}
	}


}
