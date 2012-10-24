package pt.bombap.playn.natal.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import pt.bombap.playn.natal.core.Natal;

public class NatalActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("pt/bombap/playn/natal/resources");
    PlayN.run(new Natal());
  }
}
