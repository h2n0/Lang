package uk.fls.h2n0.main.prg;

import java.util.HashMap;

import fls.engine.main.io.FileIO;

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
	
	private String fillWithVars(String line){
		
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
			
			if(name.contains(" "))error("Unexcpected ' ' when declaring variable in line: " + this.humanLine);
			if(Character.isDigit(name.charAt(0)))error("Unexpected number on line: " + this.humanLine);
			
			if(line.substring(cPos, cPos+1).equals("=")){// Dynamic assignment
				
			}else{ // Strict assignment
				String type = "";
				for(int i = cPos; i < parts.length; i++){
					if(parts[i].contains("=")){
						type = line.substring(cPos,i-1);
					}
				}
				
				type = type.trim();// Final type of var
			}
			
			
		}else{// Functions and other things
			
		}
		
		return line;
	}
	
	private void error(String r){
		throw new RuntimeException(r);
	}
}
