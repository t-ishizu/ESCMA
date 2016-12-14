package jp.ac.osaka.u.ist.t_ishizu.ESCMA_Viewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

public class ForcusSourceCode {
	public static String dirPath;
	
	public static void forcus(String fileName){
		File dir = new File(dirPath+"\\ESCMAver2\\");
		if(!dir.exists()){
			dir.mkdirs();
		}
		BufferedReader br = getBufferedReader(dirPath+"\\"+fileName);
		PrintWriter pr = getPrintWriter(dirPath+"\\ESCMAver2\\"+fileName);
		try{
			String str = br.readLine();
			boolean find = false;
			while(str != null){
				if(str.matches(".*"+"(?i)procedure.*(?i)division*"+".*")||str.matches(".*"+"(?i)procedure.*(?i)division\\.")){
					find = true;
				}
				if(find){
					int index = str.indexOf("*");
					if(index<0){
						if(!StringUtils.isBlank(str))
						pr.println(str);
					}else{
						if(!StringUtils.isBlank(str.substring(0,index)))
							pr.println(str.substring(0, index));
					}
					/*
					if(!str.matches("\\*.*"))
					pr.println(str);
					*/
				}
				str = br.readLine();
			}
			br.close();
			pr.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public static BufferedReader getBufferedReader(String file){
		File input = new File(file);
		if(!input.exists()){
			System.out.println("there are no file. : " + file);
			System.exit(0);
		}
		try {
			return new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PrintWriter getPrintWriter(String file){
		File output = new File(file);
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	} 
	
	public static void setDirPath(String dirpath){
		dirPath = dirpath;
	}
}
