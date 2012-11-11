package pt.bombap.playn.natal.core;

import java.util.ArrayList;
import java.util.List;

public class AndFilter<E> implements Filter<E> {
	private List<Filter<E>> filters;
	
	public AndFilter(Filter<E> filterA, Filter<E> filterB) {
		filters = new ArrayList<Filter<E>>(2);
		filters.add(filterA);
		filters.add(filterB);
	}
	
	public AndFilter(List<Filter<E>> filters) {
		this.filters = filters;
	}
	
	@Override
	public boolean isOk(E e) {
		for(Filter<E> f: filters) {
			if(!f.isOk(e)) {
				return false;
			}
		}
		
		return true;
	}
	

}
