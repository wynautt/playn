package pt.bombap.playn.natal.core;

import static playn.core.PlayN.*;
import static pt.bombap.playn.natal.core.MathUtils.*;

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

public class Natal implements Game {
	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;

	// main world
	private NatalWorld world = null;


	@Override
	public void init() {
		// create and add background image layer
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);

		screenWidth = graphics().width();
		screenHeight = graphics().height();


		log().debug("" + screenWidth);
		log().debug("" + screenHeight);

		bgLayer.setSize(screenWidth, screenHeight);

		graphics().rootLayer().add(bgLayer);

		world = new NatalWorld(GameWorld.normalizeWidth(24));

		world.setDt(1.0f/40.0f);

		world.addEntity(new Chimney(world, 10.0f, 12.0f, 0.0f));
		world.addEntity(new Chimney(world, 15.0f, 12.0f, 0.0f));
		world.addEntity(new Chimney(world, 20.0f, 12.0f, 0.0f));

		world.addEntity(new Present(world, 10.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 11.5f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 12.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 13.0f, 0.0f, 0.0f));
		world.addEntity(new Present(world, 14.0f, 0.0f, 0.0f));

		world.addEntity(new RandomCloud(world, 0.0f, 0.0f, 0.0f));

		world.addEntity(new DynamicClould(world, 10.f, 10.0f, 0.0f));

		pointer().setListener(new Pointer.Adapter(){

			@Override
			public void onPointerEnd(Event event) {
				super.onPointerEnd(event);
				Present present = new Present(world, world.getPhysUnitPerScreenUnit() * event.x(), world.getPhysUnitPerScreenUnit() * event.y(), 0.0f);
				world.addEntity(present);
				Vec2 currentV = present.getBody().getLinearVelocity();
				present.getBody().setLinearVelocity(new Vec2(10, currentV.y));

			}
		});

	}

	EntityFilter chimeysFilter = new EntityFilter() {

		@Override
		public boolean isOk(Entity entity) {
			return entity instanceof Chimney;
		}
	};

	EntityFilter presentsFilter = new EntityFilter() {

		@Override
		public boolean isOk(Entity entity) {
			return entity instanceof Present;
		}
	};


	@Override
	public void paint(float alpha) {
		world.paint(alpha);
	}

	@Override
	public void update(float delta) {
		world.update(delta);
	}



	@Override
	public int updateRate() {
		//return 50; // 20fps
		return 25; // 40fps
		//return 16; //60fps
	}
}
