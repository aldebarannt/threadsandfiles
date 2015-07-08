package tools.analizers.filters;

import java.util.List;

public class LastLineFilter implements IFilter {

	@Override
	public void filter(List<String> lines) {
		if (lines.size() > 0) {
			final String tmp = lines.get(lines.size()-1);
			lines.clear();
			lines.add(tmp);
		}
	}

}
