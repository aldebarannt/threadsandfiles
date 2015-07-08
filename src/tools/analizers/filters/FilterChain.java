package tools.analizers.filters;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements IFilterChain {

	private List<IFilter> filters = new ArrayList<IFilter>();
	
	@Override
	public List<String> filter(
		final List<String> lines)
	{
		final List<String> filteredLines = new ArrayList<String>(lines);
		
		filters.forEach(filter -> filter.filter(filteredLines));
		
		return filteredLines;
	}
	
	public void add(final IFilter filter) {
		filters.add(filter);
	}
	
}
