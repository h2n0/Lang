package uk.fls.h2n0.main;

import uk.fls.h2n0.main.prg.Program;

public class Lang{
	
	public Lang(String a){
		String name = a;
		Program prg;
		prg = new Program(name);
		System.out.println("Running " + name + "\n");
		prg.start();
		System.out.println("\nFinished running " + name);
	}
	
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("Usage: <filename>");
		}else{
			new Lang(args[0]);
		}
	}

}
