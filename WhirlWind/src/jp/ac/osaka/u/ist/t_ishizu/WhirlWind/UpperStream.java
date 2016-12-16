package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * 
 * @author t-ishizu
 *
 */
public class UpperStream {
	public static String CobolSeedFile;
	public static final BufferedReader getBufferedReader(String fileName){
		File file = new File(fileName);
		if(!file.exists()){
			System.err.println("!Error:There are no file " + file.getPath());
			System.exit(0);
		}
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final BufferedReader getBufferedReader(File file){
		if(!file.exists()){
			System.err.println("!Error:There are no file " + file.getPath());
			System.exit(0);
		}
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private UpperStream(){}
}
