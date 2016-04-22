package uk.fls.h2n0.main.prg;

import java.util.HashMap;

import fls.engine.main.io.FileIO;

public class Program {

	private boolean started;
	private int currentLine;
	
	private HashMap<String, Variable> vars;
	private String[] lines;
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
	}
	
	private void processLine(String s){
		System.out.println(s);
		if(!s.endsWith(";"))throw new RuntimeException("Excpected ';' at the end of line: "+(this.currentLine+1));
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
}
