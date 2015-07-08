package tools.analizers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexAnalizer extends AbstractAnalizer implements IAnalizer {
	
	private int startIndex;
	
	private Pattern pattern;
	private String mask;
	
	public RegexAnalizer(final String pattern) {
		this(pattern, "$0");
	}

	public RegexAnalizer(final String pattern, final String mask) {
		this.mask = mask;
		this.pattern = Pattern.compile(pattern);
	}

	protected void processIdle(final int lineNumber, final String line) {
		final Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			if (!isLineBufforEmpty()) {
				final int lastValidLineIndex = lineNumber - 1;
				saveResult(lastValidLineIndex, lines);
				clearLineBuffor();
			}
			
			addLine(fillMask(matcher));
			state = State.SEARCHING;
			startIndex = lineNumber;
		}
	}

	@Override
	protected void saveResult(final int lineNumber, final List<String> lines) {
		String message = "";
		for (String line : lines) {
			message += line + "\n";
		}
		
		final int lineDiv = lineNumber - startIndex + 1;
		
		if (!result.containsKey(message)) {
			result.put(message, new Data(lineDiv, 1));
		}
		else {
			final Data data = result.get(message);
			data.setCount(data.getCount() + 1);
			data.setLines(data.getLines() + lineDiv);
		}
	}

	private String fillMask(final Matcher matcher) {
		String result = new String(mask); 
		for (int i=0; i<=matcher.groupCount(); i++) {
			final String token = "$" + i;
			if (result.contains(token)) {
				result = result.replace(token, matcher.group(i));
			}
		}
		return result;
	}

}
