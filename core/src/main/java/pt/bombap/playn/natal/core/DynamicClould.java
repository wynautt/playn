package pt.bombap.playn.natal.core;

import static playn.core.PlayN.random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class DynamicClould extends NatalDynamicEntity {
	private NatalWorld world;
	
	private float travelledDistance = 0.0f;
	private float prevX = 0.0f;
	
	public DynamicClould(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
		world = (NatalWorld) gameWorld;
		setPos(prevX = getRandomX(), getRandomY());
		setLinearVelocity(0.0f, 0.0f);
	}

	private float getRandomX() {
		return random() * world.getWorldWidth();
	}

	private float getRandomY() {
		return random() * (world.getWorldHeight() / 4.0f);
	}

	@Override
	protected Body initPhysicsBody(World world, float x, float y, float angle) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KINEMATIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		PolygonShape polygonShape = new PolygonShape();
		Vec2[] polygon = new Vec2[4];
		polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f);
		polygon[1] = new Vec2(-getWidth()/2f, getHeight()/2f);
		polygon[2] = new Vec2(getWidth()/2f, getHeight()/2f);
		polygon[3] = new Vec2(getWidth()/2f, -getHeight()/2f);
		polygonShape.set(polygon, polygon.length);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.filter.groupIndex = -1;
		
		body.createFixture(fixtureDef);
		body.setTransform(new Vec2(x, y), angle);
		
		return body;
	}

	@Override
	public float getWidth() {
		return 12.0f;
	}

	@Override
	public float getHeight() {
		return 3.0f;
	}

	@Override
	protected View createView() {
		return new ImageView("sprites/Cloud1.png");
	}
	
	public float getTravelledDistance() {
		return travelledDistance;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
			
		
		travelledDistance += Math.abs(prevX - x);
			
		if(x < -getWidth() / 2f) {
			setPos(world.getWorldWidth() + getWidth() / 2f, getRandomY());
		} else if(x > world.getWorldWidth() + getWidth() / 2f) {
			setPos(-getWidth() / 2f, getRandomY());
		}
		
		prevX = x;
	}

}
