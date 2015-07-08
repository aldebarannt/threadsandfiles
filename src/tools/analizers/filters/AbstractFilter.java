package tools.analizers.filters;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilter implements IFilter {

	@Override
	public void filter(final List<String> lines) {
		final List<String> filteredLines = new ArrayList<String>(lines);
		
		doFilter(filteredLines);
		
		lines.clear();
		lines.addAll(filteredLines);
	}

	protected abstract void doFilter(final List<String> filteredLines);

}
