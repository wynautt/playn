package pt.bombap.playn.natal.core;


/**
 * 
 * 
 *
 * @param <R> return type
 * @param <E> entity to which the function applies
 */
public interface Function<R, E> {
	public R apply(E e);
}
