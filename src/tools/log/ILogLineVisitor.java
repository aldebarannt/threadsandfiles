package tools.log;

public interface ILogLineVisitor {

	void visit(final int num, final String line, final boolean eof);

//	void eof(final int num);
	
}
