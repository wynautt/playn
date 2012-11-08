package pt.bombap.playn.natal.core;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public interface PhysicsEntity {

  public Body getBody();

  public interface HasContactListener {
    public void contact(PhysicsEntity other, Fixture myFixture, Fixture otherFixture, Contact contact);
  }
}
