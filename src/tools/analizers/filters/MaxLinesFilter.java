package tools.analizers.filters;

import java.util.ArrayList;
import java.util.List;

public class MaxLinesFilter implements IFilter {

	private int maxLines;
	
	public MaxLinesFilter(final int maxLines) {
		this.maxLines = maxLines;
	}
	
	@Override
	public void filter(final List<String> lines) {
		final List<String> filteredLines = new ArrayList<String>();
		
		for (int i=0; i <= maxLines && i < lines.size(); i++) {
			filteredLines.add(lines.get(i));
		};
		
		lines.clear();
		lines.addAll(filteredLines);
	}

}
