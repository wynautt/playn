package pt.bombap.playn.natal.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;
import playn.core.GroupLayer;
import playn.core.PlayN;
import react.UnitSlot;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Interface;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public class Menu {

	private Interface iface;
	private GroupLayer layer;


	public void init() {
		layer = graphics().createGroupLayer();
		graphics().rootLayer().add(layer);

		// create our UI manager and configure it to process pointer events
		iface = new Interface();

		// create our demo interface
		Root root = iface.createRoot(AxisLayout.vertical().gap(15), SimpleStyles.newSheet());
		root.setSize(graphics().width(), graphics().height());
		root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF).inset(5)));
		layer.add(root.layer);

		Group buttons;
		root.add(new Label("PlayN Demos:"),
				buttons = new Group(AxisLayout.vertical().offStretch()),
				new Label("ESC/BACK key or two-finger tap returns to menu from demo"),
				new Label("(renderer: " + graphics().getClass().getName() + ")"));

		int key = 1;
		Button button = new Button(key++ + " - " + "btn test");
		buttons.add(button);
		button.clicked().connect(new UnitSlot() {
			@Override
			public void onEmit() {
				PlayN.log().debug("btn clicked");
			}
		});
	}


	public void shutdown() {
		if (iface != null) {
			pointer().setListener(null);
			iface = null;
		}
		layer.destroy();
		layer = null;
	}

	public void update(float delta) {
		if (iface != null) {
			iface.update(delta);
		}
	}

	public void paint(float alpha) {
		if (iface != null) {
			iface.paint(alpha);
		}
	}
	
}
