package tools.analizers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAnalizer implements IAnalizer {
	
	private boolean acceptNextLine = false;
	protected enum State {IDLE, SEARCHING}
	protected State state = State.IDLE;
	protected List<String> lines = new ArrayList<String>();
	protected Map<String, Data> result = new HashMap<String, Data>();

	protected abstract void processIdle(final int lineNumber, final String line);
	
	protected void processSearching(final int lineNumber, final String line) {
		if (isNextLineValid(line)) {
			acceptNextLine = true;
		}
		
		processLine(lineNumber, line);
		
		if (acceptNextLine && !isNextLineValid(line)) {
			acceptNextLine = false;
		}
	}

	protected void processLine(final int lineNumber, final String line) {
		if (isCurrentLineValid(line)) {
			addLine(line);
		}
		else {
			final int lastValidLineIndex = lineNumber - 1;
			saveResult(lastValidLineIndex, lines);
			clearLineBuffor();

			state = State.IDLE;
			processIdle(lineNumber, line);
		}
	}
	
	protected String getFirstLine() {
		return lines.get(0);
	}

	protected void saveResult(final int lineNumber, final List<String> lines) {
		String message = "";
		for (String line : lines) {
			message += line + "\n";
		}
		
		if (!result.containsKey(message)) {
			result.put(message, new Data(lineNumber, 1));
		}
		else {
			final Data data = result.get(message);
			data.setCount(data.getCount() + 1);
			data.setLines(data.getLines() + lineNumber);
		}
	}

	@Override
	public void analize(final int lineNumber, final String line) {
		switch (state) {
		case IDLE:
			processIdle(lineNumber, line);
			break;
		case SEARCHING:
			processSearching(lineNumber, line);
			break;
		}
	}
	
	public void close(final int lineNumber) {
		if (!lines.isEmpty()) {
			saveResult(lineNumber, lines);
		}
	}
	
	protected boolean isNextLineValid(final String line) {
		return false;
	}

	protected boolean isCurrentLineValid(final String line) {
		return acceptNextLine;
	}
	
	protected boolean isLineBufforEmpty() {
		return lines.isEmpty();
	}

	protected void clearLineBuffor() {
		lines.clear();
	}

	protected void addLine(final String line) {
		lines.add(line);
	}

	@Override
	public Map<String, Data> getResult() {
		return result;
	}

	public static class Data {
		private int lines;
		private int count;
		
		public Data(final int lines, final int count) {
			this.lines = lines;
			this.count = count;
		}
		
		public int getLines() {
			return lines;
		}

		public void setLines(int lines) {
			this.lines = lines;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + count;
			result = prime * result + lines;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Data other = (Data) obj;
			if (count != other.count)
				return false;
			if (lines != other.lines)
				return false;
			return true;
		}
	}
}
