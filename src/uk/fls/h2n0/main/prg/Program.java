package uk.fls.h2n0.main.prg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.io.FileIO;
import uk.fls.h2n0.main.prg.values.Variable;
import uk.fls.h2n0.main.prg.values.Variable.Type;

public class Program {

	public boolean running = false;
	private String file;
	private String[] fileLines;
	private int currentLine;
	public int[] reg;
	public HashMap<String,Variable> vars;
	public HashMap<String,Function> func;
	private Function currentFunc;
	private int calledFromLine = -1;
	
	public Program(String pos){
		this.currentFunc = null;
		parse(pos + ".prg");
	}
	
	public void terminate(){
		this.running = false;
	}
	
	public void start(){
		running = true;
		nextLine();
	}
	
	public void nextLine(){
		if(!running)return;
		while(true){
			if(this.currentLine >= this.fileLines.length){
				break;
			}
			
			//System.out.println(this.currentLine+1);
			String line = this.fileLines[this.currentLine];
			if(line.isEmpty()){
				this.currentLine++;
				continue;
			}
			
			if(this.currentFunc == null){
				if(isLineInFunction(this.currentLine)){
					this.currentLine++;
					continue;
				}
			}else{
				if(currentLine >= this.currentFunc.getEL()){
					this.currentFunc = null;
					this.currentLine = this.calledFromLine;
					this.currentLine ++;
					this.calledFromLine = -1;
					continue;
				}
			}
			
			if(!line.endsWith(";"))throw new RuntimeException("Excpected a ';' at the end of line " + (this.currentLine+1));
			
			line = line.substring(0,line.length()-1);
			String[] secs = line.trim().split(" ");
			
			if(!line.contains("(")){
				if(line.contains("num")){//Deceleration of variables
					for(int i = 0; i < secs.length; i++){
						String p = secs[i];
						p = p.trim();
						if(p.contains("num")){
							getValue(secs[i + 1],"num",fillWithVars(buildVar(secs,i + 3)));
							break;
						}
					}
				}else if(line.contains("bool")){
					for(int i = 0; i < secs.length; i++){
						String p = secs[i];
						p = p.trim();
						if(p.contains("bool")){
							getValue(secs[i + 1],"bool",buildVar(secs,i + 3));
							break;
						}
					}
				}else if(line.contains("string")){
					for(int i = 0; i < secs.length; i++){
						String p = secs[i];
						p = p.trim();
						if(p.contains("string")){
							getValue(secs[i + 1],"string",buildVar(secs,i + 3));
							break;
						}
					}
				}else if(line.contains("=")){//Updating variables
					for(int i = 0; i < secs.length; i++){
						String p = secs[i];
						p = p.trim();
						if(p.contains("=")){
							updateValue(secs[i - 1],secs[i],buildVar(secs,i + 1));
							break;
						}
					}
				}else if(line.contains("?")){
					for(int i = 0; i < this.vars.size(); i++){
						String key = ""+this.vars.keySet().toArray()[i];
						System.out.println(key +  " : " + this.vars.get(key).getPrintable());
					}
				}else if(line.contains("function")){//Function deceleration
					int current = this.currentLine;
					while(!this.fileLines[current].contains("}")){
						current++;
						if(current >= this.fileLines.length){
							terminate();
							return;
						}
					}
					int el = current;
					this.currentLine = el;
				}else if(line.contains("out")){
					String out = fillWithVars(buildVar(secs,1));
					if(out.contains("\"")){
						print(out);
					}else{
						print(""+Interpriter.instance.compute(out));
					}
				}
			}else{
				String part = line.split(" ")[0];
				String[] args = part.substring(part.indexOf("(")+1,part.indexOf(")")).split(",");
				if(args.length == 1){
					if(args[0].isEmpty()){
						callFunction(this.getFunctionOnCurrentLine());
					}else{
						callFunction(this.getFunctionOnCurrentLine(), args);
					}
				}else{
					callFunction(this.getFunctionOnCurrentLine(), args);
				}
			}
	
			this.currentLine ++;
		}
		this.terminate();
	}
	
	private void parse(String pos){
		this.file = FileIO.instance.loadFile(pos);
		this.vars = new HashMap<String, Variable>();
		this.func = new HashMap<String, Function>();
		String[] lines = this.file.split("\n");
		this.fileLines = lines;
		parseForFunctions();
		
		
		Variable pi = new Variable(Type.INT);
		pi.setInt(3);
		this.vars.put("pi", pi);
	}
	
	private void parseForFunctions(){
		
		int start = -1;
		int end = -1;
		String fName = "undefined";
		String[] args = new String[0];
		boolean noArgs = false;
		boolean next = false;
		for(int i = 0; i < this.fileLines.length; i++){
			String line = this.fileLines[i];

			if(line.isEmpty())continue;
			
			if(line.contains("function")){//Found a function
				start = i;
				fName = line.split(" ")[1].substring(0, line.split(" ")[1].indexOf("("));
				args = line.split(" ")[1].substring(line.split(" ")[1].indexOf("("), line.split(" ")[1].indexOf(")")-1).split(",");
				
				if(args.length == 1){
					if(args[0].isEmpty()){
						noArgs = true;
					}
				}
				next = true;
			}
			
<<<<<<< HEAD
			if(next){
				while(true){
					if(i == this.fileLines.length-2){
						throw new RuntimeException("Missplaced } in function " + fName);
					}
					line = this.fileLines[++i];
					if(line.contains("}") && start != -1){
						end = i;
						//System.out.println("Created function: " + fName + " : " + start + " : " + end);
						if(noArgs){
							this.func.put(fName, new Function(start,end));
						}else{
							this.func.put(fName, new Function(start,end,args));
						}
						noArgs = false;
						next = false;
						end = -1;
						start = -1;
						break;
					}
				}
			}
		}
	}
	
	private void getValue(String var,String type, String line){
		String alteredLine = "";
		if(line.contains("+") || line.contains("-") || line.contains("*") || line.contains("/") || line.contains("^")){//Using ops look for previous functions and resolve
			List<String> secs = new ArrayList<String>();
			String res = "";
			String[] parts = line.split("");
			for(int i = 0; i < parts.length; i++){
				String c = parts[i];
				if(c.equals("") || c.equals(" "))continue;
				if(c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^")){
					secs.add(res);
					secs.add(c);
				}else{
					boolean value = false;
					for(int j = 0; j < this.vars.size(); j++){
						if(getVariabeWithName(c) != null){
							value = true;
							break;
						}
=======
			if(line.substring(cPos, cPos+1).equals("=")){// Dynamic assignment
				//System.out.println(line.substring(cPos + 2));
			}else{ // Strict assignment
				String type = "";
				int ePos = -1;
				for(int i = cPos; i < parts.length; i++){
					if(parts[i].contains("=")){
						ePos = i;
						type = line.substring(cPos,i-1);
					}
				}
				
				type = type.trim();// Final type of var
				
				String rest = line.substring(ePos).trim();
				if(type.equals("int")){
					Variable v = new Variable(Type.INT);
					v.setInt(Interpriter.instance.compute(rest));
					if(!this.vars.containsKey(name)){
						this.vars.put(name, v);
>>>>>>> 29f78ae9013218a54de9d12dd10142b87ab2a6a5
					}
					if(value){
						if(!(getVariabeWithName(c).isNum() || getVariabeWithName(c).isBool()))secs.add(""+getVariabeWithName(c).getSValue());
						else secs.add(""+getVariabeWithName(c).getIValue());
					}else{
						secs.add(c);
					}
				}
			}
			for(int i = 0; i < secs.size(); i++){
				String pc = secs.get(i);
				pc = pc.trim();
				if(pc.isEmpty() || pc.equals(" "))continue;
				alteredLine += pc + " ";
			}
			alteredLine = alteredLine.trim();
		}
		int newLine = -1;
		if(type == "bool"){
			newLine = line.contains("true")?1:0;
		}else if(type == "num"){
			newLine = alteredLine!=""?Interpriter.instance.compute(alteredLine):Interpriter.instance.compute(line);
		}else if(type == "string"){
			
		}
		
		Type t = Type.INT;
		if(type.equals("bool"))t = Type.BOOL;
		if(type.equals("string"))t = Type.STRING;
		Variable nvar = new Variable(t);
		if(t != Type.STRING)setVarToInt(nvar, newLine);
		else setVarToString(nvar, line);
		this.vars.put(var, nvar);
	}
	
	private String buildVar(String[] secs, int s){
		String res = "";
		for(int i = s; i < secs.length; i++){
			res += secs[i] + " ";
		}
		return res.trim();
	}
	
	private void updateValue(String var, String op, String rest){
		rest = fillWithVars(rest);
		Variable cVar = this.getVariabeWithName(var);
		
		if(cVar == null)throw new RuntimeException("Trying to modify a variable before it is declared on line: "+(this.currentLine+1));
		
		if(cVar.isNum()){
			if(op.equals("=")){
				int add = Interpriter.instance.compute(rest);
				setVarToInt(cVar, add);
			}else{
				if(op.contains("+")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setVarToInt(cVar, (current + add));
				}else if(op.contains("-")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setVarToInt(cVar, (current - add));
				}else if(op.contains("/")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setVarToInt(cVar, (current / add));
				}else if(op.contains("*")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setVarToInt(cVar, (current * add));
				}else if(op.contains("^")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setVarToInt(cVar, (current ^ add));
				}
			}
		}else{
			if(op.equals("=")){
				int add = Interpriter.instance.compute(rest);
				setVarToInt(cVar,add % 2);
			}else{
				if(op.contains("!")){
					int current = cVar.getIValue();
					int nw =  current==1?0:1;
					setVarToInt(cVar,nw);
				}
			}
		}
	}
	
	private Variable getVariabeWithName(String var){
		if(this.currentFunc != null){
			if(this.currentFunc.locals.containsValue(var)){
				return this.currentFunc.locals.get(var);
			}
		}
		return this.vars.get(var);
	}
	
	private void setVarToInt(Variable var, int val){
		if(var.isNum() || var.isBool()){
			var.setInt(val);
		}else{
			setVarToString(var, ""+val);
		}
	}
	
	private void setVarToString(Variable var, String val){
		var.setString(val);
	}
	
	private String fillWithVars(String s){
		String res = "";
		String[] secs = s.split(" ");
		for(int i = 0; i < secs.length; i++){
			String c = secs[i];
			if(getVariabeWithName(c) != null){
				Variable var = getVariabeWithName(c);
				
				if(var.isNum()){
					res += var.getIValue() + " ";
				}else{
					res += var.getSValue();
				}
			}else{
				res += c + " ";
			}
		}
		return res;
	}
	
	private void print(String s){
		System.out.println(s);
	}
	
	private String getFunctionInLine(String line){
		String[] parts = line.split(" ");
		String name = parts[0].substring(0, parts[0].indexOf("("));
		return name;
	}
	
	private Function getFunctionByName(String name){
		if(hasFunction(name))return this.func.get(name);
		else return null;
	}
	
	private Function getFunctionOnCurrentLine(){
		return getFunctionByName(getFunctionInLine(this.fileLines[this.currentLine]));
	}
	
	private boolean hasFunction(String name){
		return this.func.get(name)!=null;
	}
	
	private boolean isLineInFunction(int ln){
		for(int i = 0; i < this.func.keySet().size(); i++){
			Function f = this.func.get(this.func.keySet().toArray()[i]);
			if(f.isLineIn(ln))return true;
		}
		return false;
	}
	
	private void callFunction(Function c, String...args){
		this.currentFunc = c;
		this.currentFunc.call(args);
		this.calledFromLine = this.currentLine+1;
		this.currentLine = c.getSL();
	}
}
