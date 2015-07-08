package tools.analizers;


public class ExceptionRootCauseAnalizer extends ExceptionAnalizer implements IAnalizer {
	
	public ExceptionRootCauseAnalizer(final String pattern, final String mask, final int depth) {
		super(pattern, mask, depth);
	}
	
	@Override
	protected void processLine(final int num, final String line) {
		if (line.startsWith("Caused") || line.startsWith("Nested exception")) {
			final String firstLine = getFirstLine();
			clearLineBuffor();
			addLine(firstLine);
			addLine(line);
		} else {
			super.processLine(num, line);
		}
	}

}
