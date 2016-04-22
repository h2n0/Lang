package uk.fls.h2n0.main.prg;

public class Variable {
	
	public enum Type{
		INT,
		BOOL,
		STRING;
	}
	
	private final Type type;
	
	public Variable(Type ty){
		this.type = ty;
	}
}
