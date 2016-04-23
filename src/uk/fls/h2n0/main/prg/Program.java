package uk.fls.h2n0.main.prg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import fls.engine.main.io.FileIO;
import uk.fls.h2n0.main.prg.values.Variable;
import uk.fls.h2n0.main.prg.values.Variable.Type;

public class Program {

	private boolean started;
	private int currentLine;
	
	private HashMap<String, Variable> vars;
	private String[] lines;
	private int humanLine;
	public Program(String loc){
		this.vars = new HashMap<String, Variable>();
		this.currentLine = 0;
		this.lines = FileIO.instance.loadFile(loc).split("\n");
	}
	
	public void nextLine(){
		if(!this.started)return;
		if(this.currentLine == this.lines.length){
			this.stop();
			return;
		}
		String line = this.lines[this.currentLine].trim();
		if(!line.isEmpty()){
			processLine(line);
		}
		this.currentLine++;
		this.humanLine = this.currentLine + 1;
	}
	
	private void processLine(String line){
		if(!line.endsWith(";"))error("Excpected ';' at the end of line: " + this.humanLine);
		//After syntax errors, for now
		
		line = line.substring(0, line.length() - 1);
		lookForVars(line);
		line = fillWithVars(line);
	}
	
	public void stop(){
		if(!started)return;
		this.started = false;
	}
	
	public void start(){
		if(this.started)return;
		this.started = true;
		this.currentLine = 0;
		while(!hasFinished())nextLine();
	}
	
	public boolean hasFinished(){
		return this.currentLine == this.lines.length && !this.started;
	}
	
	private String lookForVars(String line){
		
		if(line.contains(":")){// Doing something with vars.
			String[] parts = line.trim().split("");
			String name = "";
			int cPos = -1;
			for(int i = 0; i < parts.length; i++){// Looking for
				if(parts[i].contains(":")){
					cPos = i;
					name = line.substring(0,i-1);
				}
			}
			name = name.trim();// Final name of var.
			
			if(this.vars.containsKey(name))return line;
			if(name.contains(" "))error("Unexcpected ' ' when declaring variable in line: " + this.humanLine);
			if(Character.isDigit(name.charAt(0)))error("Unexpected number on line: " + this.humanLine);
			
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
					}
				}else if(type.equals("bool")){
					
				}else if(type.equals("string")){
					Variable v = new Variable(Type.STRING);
					v.setString(rest);
					if(!this.vars.containsKey(name)){
						this.vars.put(name, v);
					}
				}
			}
			
			
		}else{// Functions and other things
			
		}
		
		return line;
	}
	
	private String fillWithVars(String line){
		if(this.vars.size() == 0)return line;
		String[] parts = line.split(" ");
		List<String> fin = new ArrayList<String>();
		for(int i = 0; i < parts.length; i++){
			String c = parts[i];
			if(this.vars.containsKey(c)){
				fin.add(this.vars.get(c).getValue());
			}else{
				fin.add(c);
			}
		}
		
		String alt = "";
		for(int i = 0; i < fin.size(); i++){
			alt += fin.get(i) + " ";
		}
		alt = alt.trim();
		System.out.println(alt);
		return alt;
	}
	
	private void error(String r){
		throw new RuntimeException(r);
	}
}
