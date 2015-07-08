package tools.analizers.filters;

import java.util.List;

public interface IFilterChain {

	List<String> filter (final List<String> lines);
	
}
