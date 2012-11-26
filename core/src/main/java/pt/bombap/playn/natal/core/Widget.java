package pt.bombap.playn.natal.core;

public abstract class Widget<T> extends Entity {
	protected T value;
	
	public Widget(GameWorld gameWorld, float px, float py, float pangle) {
		super(gameWorld, px, py, pangle);
	}

	public Widget(GameWorld gameWorld, float width, float height, float px,	float py, float pangle) {
		super(gameWorld, width, height, px, py, pangle);
	}
	
	public abstract void setValue(T value);
	public abstract T getValue();
	
	
}
