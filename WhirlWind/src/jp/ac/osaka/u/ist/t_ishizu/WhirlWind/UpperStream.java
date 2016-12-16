package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.WhirlWind.*;
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
	
	public static final ArrayList<Sprout> createSproutList(){
		if(CobolSeedFile==null){
			System.err.println("!Error:Cobol Source Code isn't defined.");
			System.exit(0);
		}
		BufferedReader br = getBufferedReader(CobolSeedFile);
		ArrayList<Sprout> sproutList = new ArrayList<Sprout>();
		try{
			String str = br.readLine();
			boolean find = false;
			Sprout sp = null;
			while(str!=null){
				if(str.equals("#begin{set}")){
					find = true;
					sp = new Sprout().setId(sproutList.size());
				}else if(str.equals("#end{set}")){
					find = false;
					sproutList.add(sp);
				}else if(find){
					/* register seed information */
					String[] str_split = str.split("[.,\t]+",0);
					Seed s = new Seed()
					.setFileId(Integer.parseInt(str_split[1]))
					.setLS(Integer.parseInt(str_split[2])).setLE(Integer.parseInt(str_split[5]))
					.setCS(Integer.parseInt(str_split[3])).setCE(Integer.parseInt(str_split[6]))
					.setTS(Integer.parseInt(str_split[4])).setTE(Integer.parseInt(str_split[7]))
					.setSprout(sp);
					sp.addSeedList(s);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return sproutList;
	}
	
	
	public static final HashMap<Integer,ArrayList<Seed>> createSeedMap(){
		HashMap<Integer,ArrayList<Seed>> SeedMap = new HashMap<Integer,ArrayList<Seed>>();
		for(Sprout sp : sproutList){
			for(Seed s : sp.getSeedList()){
				if(!SeedMap.containsKey(s.getFileId())){
					SeedMap.put(s.getFileId(),new ArrayList<Seed>());
				}
				SeedMap.get(s.getFileId()).add(s);
			}
		}
		return SeedMap;
	}
	private UpperStream(){}
}
