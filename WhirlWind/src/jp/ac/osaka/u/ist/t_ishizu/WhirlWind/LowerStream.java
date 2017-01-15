package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LowerStream {
	public static String outputFile = "ww.txt";
	
	public static PrintWriter getPrintWriterWithOverwrite(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static PrintWriter getPrintWriterWithOverwrite(String output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(new File(output),false)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static PrintWriter getPrintWriterWithoutOverwrite(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,true)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	public static PrintWriter getPrintWriterWithoutOverwrite(String output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(new File(output),true)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	
}
