package pt.bombap.playn.natal.core;

import static playn.core.PlayN.log;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class Chimney extends NatalDynamicEntity implements PhysicsEntity.HasContactListener {
	public static String TYPE = "Chimney";
	private NatalWorld world;

	
	public Chimney(GameWorld gameWorld, float width, float height, float px, float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
		world = (NatalWorld) gameWorld;
		autoDestroyWhenOutOfWorld = true;
		setLinearVelocity(0.0f, 0.0f);
	}

	
	@Override
	protected Body initPhysicsBody(World world, float x, float y, float angle) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KINEMATIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.0f;

		PolygonShape polygonShape = new PolygonShape();
		//		Vec2[] polygon = new Vec2[4];
		//		polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f);
		//		polygon[1] = new Vec2(-getWidth()/2f, getHeight()/2f);
		//		polygon[2] = new Vec2(getWidth()/2f, getHeight()/2f);
		//		polygon[3] = new Vec2(getWidth()/2f, -getHeight()/2f);
		//		polygonShape.set(polygon, polygon.length);

		Vec2 tlv = new Vec2(-getWidth()/2f, -getHeight()/2f);
		Vec2 blv = new Vec2(-getWidth()/2f, getHeight()/2f);
		Vec2 trv = new Vec2(getWidth()/2f, -getHeight()/2f);
		Vec2 brv = new Vec2(getWidth()/2f, getHeight()/2f);

		polygonShape.setAsEdge(tlv, blv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "left";
		body.createFixture(fixtureDef);

		polygonShape.setAsEdge(blv, brv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "bottom";
		body.createFixture(fixtureDef);

		polygonShape.setAsEdge(trv, brv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "right";
		body.createFixture(fixtureDef);


		body.setTransform(new Vec2(x, y), angle);
		return body;
	}


	/**
	 * Return the size of the offset where the block is slightly lower than where
	 * the image is drawn for a depth effect
	 */
	public float getTopOffset() {
		return 2.0f / 8f;
	}


	@Override
	public View createView() {
		//return new ImageView("sprites/chimney1.jpg");
		return new SpriteView("sprites/chimneys.json");
	}


	@Override
	public void contact(PhysicsEntity other, Fixture myFixture,	Fixture otherFixture, Contact contact) {
		if(other instanceof Present) {
			if("bottom".equals(myFixture.getUserData())) {
				log().debug("Collision between chimney and present!");
				//world.getWorld().destroyBody(other.getBody());
				//world.removeEntity((Entity) other);
				//TODO: think about joining creation and adding to the world and destruction and removing from the world
				((Present) other).destroy(world);
			}
		}
	}

	public float getVelocity() {
		return 0.002f;
	}
	
	
	@Override
	public SpriteView getView() {
		return (SpriteView)super.getView();
	}
	
	public void changeChimney(String id) {
		getView().changeSprite(id);
	}

//	@Override
//	public void update(float delta) {
//		super.update(delta);
//		x -= getVelocity() * delta;
//		setPos(x, y);
//	}

}
