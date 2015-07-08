package tools.analizers.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RootCauseFilter implements IFilter {

	@Override
	public void filter(final List<String> lines) {
		final List<String> filteredLines = new ArrayList<String>();
		final List<String> tempLines = new ArrayList<String>(lines);
		
		Collections.reverse(tempLines);
		
		for (String line : tempLines) {
			filteredLines.add(line);
			if (line.startsWith("Caused") || line.startsWith("Nested exception")) {
				break;
			}
		};
		
		Collections.reverse(filteredLines);
		
		lines.clear();
		lines.addAll(filteredLines);
	}

}
