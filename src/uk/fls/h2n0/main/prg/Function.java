package uk.fls.h2n0.main.prg;

import java.util.HashMap;

import uk.fls.h2n0.main.prg.values.Variable;
import uk.fls.h2n0.main.prg.values.Variable.Type;

public class Function {

	
	private int sl,el;
	private String[] args;
	private HashMap<String,Variable> locals;
	
	
	
	public Function(int sl,int el, String... args){
		this.sl = sl;
		this.el = el;
		this.args = args;
	}
	
	public void call(String...cargs){
		if(this.args.length != cargs.length)throw new RuntimeException("Expected " + this.args.length + " arguments but got " + cargs.length);
		this.locals = new HashMap<String, Variable>();
		for(int i = 0; i < this.args.length; i++){
			String s = cargs[i];
			String[] parts = s.split(" ");
			
			
			String stype = this.args[i].split(" ")[0];
			String rest = parts[0];
			String name = this.args[i].split(" ")[1];
			
			stype = stype.trim();
			name = name.trim();
			
			Type t = Type.INT;
			
			if(stype.equals("bool")){
				t = Type.BOOL;
			}else if(stype.equals("string")){
				t = Type.STRING;
			}
			Variable v = new Variable(t);
			
			if(t == Type.INT){
				v.setInt(Interpriter.instance.compute(s));
			}else if(t == Type.BOOL){
			
			}else if(t == Type.STRING){
				v.setString(rest);
			}
			this.locals.put(name, v);
		}
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
	
	public void terminate(){
		this.locals = null;
	}
	
	public HashMap<String,Variable> getVars(){
		return this.locals;
	}
}
