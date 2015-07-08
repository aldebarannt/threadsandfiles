package tools.analizers;

import java.util.Map;

import tools.analizers.AbstractAnalizer.Data;

public interface IAnalizer {

	public void analize(final int num, final String line);
	public void close(final int num);
	public Map<String, Data> getResult();
	
}
