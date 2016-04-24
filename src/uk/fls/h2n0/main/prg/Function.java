package uk.fls.h2n0.main.prg;

import java.util.HashMap;

import uk.fls.h2n0.main.prg.values.Variable;

public class Function {

	
	private int sl,el;
	private String[] args;
	public HashMap<String,Variable> locals;
	
	
	
	public Function(int sl,int el, String... args){
		this.sl = sl;
		this.el = el;
		this.args = args;
		//System.out.println(sl + " : " + el);
	}
	
	public void call(String...cargs){
		if(this.args.length != cargs.length)throw new RuntimeException("Expected " + this.args.length + " arguments but got " + cargs.length);
		//System.out.println(cargs[0]);
	}
	
	public boolean isLineIn(int ln){
		return ln >= sl && ln <= el;
	}
	
	public int getSL(){
		return this.sl;
	}
	
	public int getEL(){
		return this.el;
	}
}
