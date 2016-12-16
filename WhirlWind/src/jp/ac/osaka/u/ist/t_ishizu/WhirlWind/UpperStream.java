package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
	
	public static final ArrayList<String> createFileList(){
		if(CobolSeedFile==null){
			System.err.println("!Error:Cobol Source Code isn't defined.");
			System.exit(0);
		}
		BufferedReader br = getBufferedReader(CobolSeedFile);
		ArrayList<String> fileList = new ArrayList<String>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("#begin{file description}")){
					canRead = true;
				}else if(str.equals("#end{file description}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[\t]+",0);
					fileList.add(str_split[3]);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileList;
	}
	private UpperStream(){}
}
