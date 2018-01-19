package pt.bombap.playn.natal.core;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class Present extends NatalDynamicEntity {
	public static String TYPE = "Present";
	
	private int type;

	public Present(GameWorld gameWorld, float x, float y, float angle) {
		super(gameWorld, x, y, angle);
		autoDestroyWhenOutOfWorld = true;
		
	}
	
	public void setType(int type) {
		SpriteView v = (SpriteView) getView();
		v.changeSprite("" + type);
		
		switch (type) {
		case Code.PRESENT_G1000:
			//getBody().applyLinearImpulse(new Vec2(0, 10), getBody().getWorldCenter());
			getBody().setLinearVelocity(new Vec2(0, getBody().getLinearVelocity().y));
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void viewLoaded(View view, GameWorld gameWorld) {
		float[] origin = getViewOrigin(view);
		view.setOrigin(origin[0], origin[1]);
		view.setScale(getWidth() / view.getWidth(), getHeight() / view.getHeight());
		view.setTranslation(x, y);
		view.setRotation(angle);
	}

	@Override
	protected Body initPhysicsBody(World world, float x, float y, float angle) {
		FixtureDef fixtureDef = new FixtureDef();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = getRadius();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.4f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.filter.groupIndex = -1;
		circleShape.m_p.set(0, 0);
		body.createFixture(fixtureDef);
		body.setLinearDamping(0.2f);
		body.setTransform(new Vec2(x, y), angle);
		return body;
	}

	@Override
	public float getWidth() {
		return 2 * getRadius();
	}

	@Override
	public float getHeight() {
		return 2 * getRadius();
	}

	float getRadius() {
		return 1.0f / 2f;
		//return 0.5f;
	}

	@Override
	public View createView() {
		return new SpriteView("sprites/present_types.json");
	}

}
