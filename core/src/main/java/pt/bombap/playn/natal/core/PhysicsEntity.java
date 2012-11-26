package pt.bombap.playn.natal.core;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public interface PhysicsEntity {

  public Body getBody();

  public interface HasContactListener {
	  
    /**
     * Listener called every time a collision occurs involving this object.
     * @param other The object this object is colliding with.
     * @param myFixture This object's fixture.
     * @param otherFixture The fixture of the object this object is colliding with.
     * @param contact The contact produced by this collision.
     */
    public void contact(PhysicsEntity other, Fixture myFixture, Fixture otherFixture, Contact contact);
  }
}
