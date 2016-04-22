package uk.fls.h2n0.main.prg.values;

public class Variable {
	
	public enum Type{
		INT,
		BOOL,
		STRING;
	}
	
	private final Type type;
	
	private int iVal;
	private String sVal;
	
	public Variable(Type ty){
		this.type = ty;
	}
	
	public int getIValue(){
		if(this.type == Type.STRING)throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		return this.iVal;
	}
	
	public String getSValue(){
		if(this.type != Type.STRING)throw new RuntimeException("Trying to access a string variable of a int... Not possible");
		return this.sVal;
	}
	
	public void setInt(int a){
		if(this.type == Type.STRING)throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		this.iVal = a;
	}
	
	public void setString(String s){
		if(this.type != Type.STRING)throw new RuntimeException("Trying to access a string variable of a int... Not possible");
		this.sVal = s;
	}
	
	public String getValue(){
		if(this.type == Type.INT){
			return ""+this.iVal;
		}else{
			return this.sVal;
		}
	}
}
