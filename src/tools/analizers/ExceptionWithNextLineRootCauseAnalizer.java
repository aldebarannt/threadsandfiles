package tools.analizers;


public class ExceptionWithNextLineRootCauseAnalizer extends ExceptionAnalizer implements IAnalizer {
	
	public ExceptionWithNextLineRootCauseAnalizer(final String pattern, final String mask, final int depth) {
		super(pattern, mask, depth);
	}
	
	protected void processSearching(final int num, final String line) {
		if (line.startsWith("at")) {
			if (depth <= 0 || lines.size() < depth) {
				lines.add(line);
			}
		}
		else {
			if (lines.size() == 1) {
				lines.add(line);
			} else {
				state = State.IDLE;
				saveResult(num, lines);
				lines.clear();
				processIdle(num, line);
			}
		}
	}

}
