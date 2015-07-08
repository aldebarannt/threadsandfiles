package tools.analizers;

import java.util.List;

import tools.analizers.filters.IFilterChain;

public class ExceptionFilteredAnalizer extends RegexAnalizer {

	private IFilterChain filterChain;
	
	public ExceptionFilteredAnalizer(final String pattern, final String mask, final IFilterChain filterChain) {
		super(pattern, mask);
		this.filterChain = filterChain;
	}
	
	@Override
	protected boolean isNextLineValid(final String line) {
		return super.isNextLineValid(line)
			|| line.startsWith("Nested exception");
	}

	@Override
	protected boolean isCurrentLineValid(final String line) {
		return super.isCurrentLineValid(line)
			|| line.startsWith("Nested exception")
			|| line.startsWith("Caused")
			|| line.startsWith("at")
			|| line.startsWith("...");
	}
	
	@Override
	protected void saveResult(final int lineNumber, final List<String> lines) {
		final List<String> filteredLines = filterChain.filter(lines); 
		super.saveResult(lineNumber, filteredLines);
	}

}
