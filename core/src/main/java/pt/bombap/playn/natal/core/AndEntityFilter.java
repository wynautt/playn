package pt.bombap.playn.natal.core;

import java.util.ArrayList;
import java.util.List;

public class AndEntityFilter implements EntityFilter {
	private List<EntityFilter> filters;
	
	public AndEntityFilter(EntityFilter filterA, EntityFilter filterB) {
		filters = new ArrayList<EntityFilter>(2);
		filters.add(filterA);
		filters.add(filterB);
	}
	
	public AndEntityFilter(List<EntityFilter> filters) {
		this.filters = filters;
	}
	
	@Override
	public boolean isOk(Entity entity) {
		for(EntityFilter f: filters) {
			if(!f.isOk(entity)) {
				return false;
			}
		}
		
		return true;
	}
	

}
