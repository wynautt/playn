package pt.bombap.playn.natal.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import pt.bombap.playn.natal.core.Natal;

public class NatalJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("pt/bombap/playn/natal/resources");
    PlayN.run(new Natal());
  }
}
