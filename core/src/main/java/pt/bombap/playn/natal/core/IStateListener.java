package pt.bombap.playn.natal.core;

public interface IStateListener<T> {
	public void notify(T self, int code);
}
