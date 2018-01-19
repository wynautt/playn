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

import playn.core.PlayN;
import playn.core.ResourceCallback;

public class StaticChimney extends NatalEntity implements PhysicsEntity.HasContactListener {
	public static String TYPE = "Chimney";
	private NatalWorld world;
	private SpriteView base;
	
	public static class Simple extends StaticChimney {
		public Simple(GameWorld gameWorld, float px, float py, float pangle) {
			super(gameWorld, px, py, pangle);
		}
		
		@Override
		public float getWidth() {
			return 5.75f / 2f;
		}
		
		@Override
		public float getHeight() {
			return 5.15f / 2f;
		}
		
		@Override
		public View createView() {
			SpriteView view = (SpriteView) super.createView();
			view.changeSprite("simple");
			return view;
		}
	}
	
	public static class SimpleTall extends Simple {
		public SimpleTall(GameWorld gameWorld, float px, float py, float pangle) {
			super(gameWorld, px, py, pangle);
			((SpriteView)getView()).changeSprite("simple_tall");
		}
		
		@Override
		public float getWidth() {
			return 5.35f / 2f;
		}
		
		@Override
		public float getHeight() {
			return 7.95f / 2f;
		}
		
		@Override
		public View createView() {
			SpriteView view = (SpriteView) super.createView();
			view.changeSprite("simple_tall");
			return view;
		}
	}
	
	public static class SimpleFat extends Simple {
		public SimpleFat(GameWorld gameWorld, float px, float py, float pangle) {
			super(gameWorld, px, py, pangle);
			((SpriteView)getView()).changeSprite("simple_fat");
		}
		
		@Override
		public float getWidth() {
			return 5.35f / 2f;
		}
		
		@Override
		public float getHeight() {
			return 4.35f / 2f;
		}
		
		@Override
		public View createView() {
			SpriteView view = (SpriteView) super.createView();
			view.changeSprite("simple_fat");
			return view;
		}
	}
	

	public StaticChimney(final GameWorld gameWorld, float px, float py, float pangle) {
		super(gameWorld, px, py, pangle);
		world = (NatalWorld) gameWorld;
		base = new SpriteView("sprites/chimneys_base.json");
		base.setRandomSprite();
		
		final float bw = getWidth() * 1.1f;
		final float bh = getHeight() / 5f;
		
		base.addCallback(new ResourceCallback<View>() {
			@Override
			public void done(View view) {
				// since the image is loaded, we can use its width and height
				view.setOrigin(view.getWidth() / 2f, view.getHeight() / 2f);
				view.setScale(bw / view.getWidth(), bh / view.getHeight());
				view.setTranslation(x, y);
				
				((NatalWorld)gameWorld).getStaticLayerBack().add(base.getLayer());
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error loading view: " + err.getMessage());
			}
		});
		
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

		float w = getWidth() * (7/10f) / 2f;
		float h = getHeight() * (14.5f/20f);
		
		Vec2 tlv = new Vec2(-w, -h);
		Vec2 blv = new Vec2(-w, 0f);
		Vec2 trv = new Vec2(w, -h);
		Vec2 brv = new Vec2(w, 0f);
		Vec2 collisionSurfaceL = new Vec2(-w/2f, -1);
		Vec2 collisionSurfaceR = new Vec2(w/2f, -1);

		float tw = getWidth() * (8/10f) /2f;
		float th = getHeight() * (9/10f) ;
		
		Vec2 toptlv = new Vec2(-tw, -th);
		Vec2 topblv = new Vec2(-tw, -h);
		Vec2 toptrv = new Vec2(tw, -th);
		Vec2 topbrv = new Vec2(tw, -h);
		
		
		polygonShape.setAsEdge(tlv, blv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "left";
		body.createFixture(fixtureDef);

		polygonShape.setAsEdge(blv, brv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "bottom1";
		body.createFixture(fixtureDef);
		
		polygonShape.setAsEdge(collisionSurfaceL, collisionSurfaceR);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "bottom";
		body.createFixture(fixtureDef);

		polygonShape.setAsEdge(trv, brv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "right";
		body.createFixture(fixtureDef);
		
		polygonShape.setAsEdge(toptlv, topblv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "topleft";
		body.createFixture(fixtureDef);

		polygonShape.setAsEdge(toptrv, topbrv);
		fixtureDef.shape = polygonShape;
		fixtureDef.userData = "topright";
		body.createFixture(fixtureDef);


		body.setTransform(new Vec2(x, y), angle);
		return body;
	}

	@Override
	public View createView() {
		//return new ImageView("sprites/chimney1.jpg");
		return new SpriteView("sprites/chimneys.json");
	}

	@Override
	protected float[] getViewOrigin(View view) {
		return new float[] {view.getWidth() / 2.0f, view.getHeight()};
	}
	
	@Override
	protected void destroy(GameWorld gameWorld) {
		super.destroy(gameWorld);
		((NatalWorld)gameWorld).getStaticLayerBack().remove(base.getLayer());
	}

	@Override
	public void contact(PhysicsEntity other, Fixture myFixture,	Fixture otherFixture, Contact contact) {
		if(other instanceof Present) {
			if("bottom".equals(myFixture.getUserData())) {
				log().debug("Collision between chimney and present!");
				//world.getWorld().destroyBody(other.getBody());
				//world.removeEntity((Entity) other);
				//TODO: think about joining creation and adding to the world and destruction and removing from the world
				stateListener.notify((Present) other, Code.PRESENT_DELIVERED);
				((Present) other).destroy(world);
				destroy(world);
			}
		}
	}

}
