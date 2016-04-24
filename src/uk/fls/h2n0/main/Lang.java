package uk.fls.h2n0.main;

import uk.fls.h2n0.main.prg.Program;

public class Lang{
	
	public Lang(){
		String name = "test";
		Program prg;
		prg = new Program(name);
		System.out.println("Running " + name + "\n");
		prg.start();
		System.out.println("Finished running " + name);
	}
	
	public static void main(String[] args){
		new Lang();
	}

}
