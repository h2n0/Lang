package uk.fls.h2n0.main.prg.values;

public class Variable {
	
	public enum Type{
		INT,
		BOOL,
		STRING;
	}
	
	private final Type type;
	
	private float iVal;
	private String sVal;
	
	public Variable(Type ty){
		this.type = ty;
	}
	
	public int getIValue(){
		if(!isNum())throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		return (int)this.iVal;
	}
	
	public float getFValue(){
		if(!isNum())throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		return this.iVal;
	}
	
	public String getSValue(){
		if(!isString())throw new RuntimeException("Trying to access a string variable of a int... Not possible");
		return this.sVal;
	}
	
	public void setFloat(float a){
		if(!isNum())throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		this.iVal = a;
	}
	
	public void setInt(int a){
		if(!isNum())throw new RuntimeException("Trying to access an int variable of a string... Not possible");
		this.iVal = a;
	}
	
	public void setString(String s){
		if(!isString())throw new RuntimeException("Trying to access a string variable of a int... Not possible");
		this.sVal = s;
	}
	
	public String getValue(){
		if(this.type == Type.INT){
			return ""+this.iVal;
		}else{
			return this.sVal;
		}
	}
	
	public boolean isNum(){
		if(this.type == Type.INT || this.type == Type.BOOL)return true;
		else return false;
	}
	
	public boolean isString(){
		if(this.type == Type.STRING)return true;
		else return false;
	}
	
	public boolean isBool(){
		if(this.type == Type.BOOL)return true;
		else return false;
	}
	
	public String getPrintable(){
		if(this.type == Type.STRING){
			return this.sVal;
		}else{
			return ""+this.iVal;
		}
	}
}
