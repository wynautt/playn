package pt.bombap.playn.natal.core;

public interface Function2D<R, E> {
	public R apply(R partial, E e);
}
