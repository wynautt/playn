package pt.bombap.playn.natal.core;

public interface IGameWorld {
	public void preStepUpdate(float delta);
	public void postStepUpdate(float delta);
	public void worldPaint(float delta);
}
