package pt.bombap.playn.natal.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import pt.bombap.playn.natal.core.Natal;

public class NatalHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("natal/");
    PlayN.run(new Natal());
  }
}
