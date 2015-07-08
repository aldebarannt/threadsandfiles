package tools.analizers;


public class ExceptionAnalizer extends RegexAnalizer implements IAnalizer {
	
	protected int depth;
	
	public ExceptionAnalizer(final String pattern, final String mask, final int depth) {
		super(pattern, mask);
		this.depth = depth;
	}
	
	@Override
	protected void addLine(final String line) {
		if (depth <= 0 || lines.size() < depth) {
			super.addLine(line);
		}
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

}
