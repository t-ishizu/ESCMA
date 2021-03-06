package jp.ac.osaka.u.ist.t_ishizu.ChameleonHunter;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static jp.ac.osaka.u.ist.t_ishizu.ChameleonHunter.ChameleonHunter.*;
public class UpperStream {
	public static String ResultFile = "c.txt";
	public static String CobolSeedFile = "d.txt";

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
				if(str.equals("source_files {")){
					canRead = true;
				}else if(str.equals("}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[\t]+",0);
					fileList.add(str_split[1]);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileList;
	}
	
	public static HashMap<Integer,ArrayList<Seed>> createSeedMap(){
		BufferedReader br = getBufferedReader(CobolSeedFile);
		HashMap<Integer,ArrayList<Seed>> SeedMap = new HashMap<Integer,ArrayList<Seed>>();
		try{
			String str = br.readLine();
			boolean find = false;
			while(str!=null){
				if(str.equals("clone_pairs {")){
					find = true;
				}else if(str.equals("}")){
					find = false;
				}else if(find){
					String[] str_split = str.split("[.,\\-\t]+",0);
					Seed s1 = new Seed()
					.setId(Integer.parseInt(str_split[0]))
					.setFileId(Integer.parseInt(str_split[1]))
					.setTS(Integer.parseInt(str_split[2])).setTE(Integer.parseInt(str_split[3]));
					Seed s2 = new Seed()
					.setId(Integer.parseInt(str_split[0]))
					.setFileId(Integer.parseInt(str_split[4]))
					.setTS(Integer.parseInt(str_split[5])).setTE(Integer.parseInt(str_split[6]));
					int indexOfs1 = -1;
					if(SeedMap.containsKey(s1.getFileId())){
						indexOfs1=getCodeCloneIndex(SeedMap.get(s1.getFileId()),s1);
					}else{
						SeedMap.put(s1.getFileId(), new ArrayList<Seed>());
					}
					int indexOfs2 = -1;
					if(SeedMap.containsKey(s2.getFileId())){
						indexOfs2=getCodeCloneIndex(SeedMap.get(s2.getFileId()),s2);
					}else{
						SeedMap.put(s2.getFileId(), new ArrayList<Seed>());
					}
					if(indexOfs1==-1&&indexOfs2==-1){
						SeedMap.get(s1.getFileId()).add(s1);
						SeedMap.get(s2.getFileId()).add(s2);
						Sprout sp = new Sprout();
						s1.setSprout(sp);
						s2.setSprout(sp);
						sp.addSeedList(s1);
						sp.addSeedList(s2);
					}else if(indexOfs1!=-1&&indexOfs2==-1){
						SeedMap.get(s2.getFileId()).add(s2);
						Sprout sp = SeedMap.get(s1.getFileId()).get(indexOfs1).getSprout();
						s2.setSprout(sp);
						sp.addSeedList(s2);
					}else if(indexOfs1==-1&&indexOfs2!=-1){
						SeedMap.get(s1.getFileId()).add(s1);
						Sprout sp = SeedMap.get(s2.getFileId()).get(indexOfs2).getSprout();
						s1.setSprout(sp);
						sp.addSeedList(s1);
					}
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return SeedMap;
	}
	
	public static int getCodeCloneIndex(ArrayList<Seed>list,Seed s){
		for(int i=0;i<list.size();i++){
			Seed seed = list.get(i);
			if(s.getTS()==seed.getTS()&&s.getTE()==seed.getTE()){
				return i;
			}
		}
		return -1;
	}
	
	public static ArrayList<Sprout> createSproutList(){
		ArrayList<Sprout> sproutList = new ArrayList<Sprout>();
		int sproutId=0;
		for(ArrayList<Seed> list:SeedMap.values()){
			for(Seed s : list){
				if(s.getSprout().getId()==-1){
					sproutList.add(s.getSprout().setId(sproutId++));
				}
			}
		}
		return sproutList;
	}
	
	public static String[] createCCFXDFileArray(){
		BufferedReader br = getBufferedReader(CobolSeedFile);
		String[] CCFXDFileArray = new String[2];
		try{
			String str = br.readLine();
			while(str!=null){
				String[] str_split = str.split("[\\s]+",0);
				if(str_split[0].equals("option:")){
					if(str_split[1].equals("-preprocessed_file_postfix")){
						CCFXDFileArray[0] = str_split[2];
					}else if(str_split[1].equals("-n")){
						CCFXDFileArray[1] = str_split[2];
						if(!CCFXDFileArray[0].isEmpty())
						return CCFXDFileArray;
					}
				}
				str = br.readLine();
			}
					
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return CCFXDFileArray;
	}
	
	public static HashMap<Integer,ArrayList<Token>> createTokenMap(){
		HashMap<Integer,ArrayList<Token>> tokenMap = new HashMap<Integer, ArrayList<Token>>(); 
		for(int fileId=1;fileId<=fileList.size();fileId++){
			StringBuffer sb = new StringBuffer();
			sb.append(CCFXDFileArray[1]);	
			sb.append("\\.ccfxprepdir");
			sb.append(fileList.get(fileId-1).substring(CCFXDFileArray[1].length(), fileList.get(fileId-1).length()));
			sb.append(CCFXDFileArray[0]);
			String path =  sb.toString();
			BufferedReader br = getBufferedReader(path);
			ArrayList<Token> tokenList = new ArrayList<Token>();
			try {
				String str = br.readLine();
				ArrayList<Integer[]> coorList = new ArrayList<Integer[]>();
				int id = 0;
				while(str!=null){
					String[] str_split = str.split("[.\\s\\t]+",0);
					Integer[] tokenArr = new Integer[]{
							Integer.parseInt(str_split[0],16),Integer.parseInt(str_split[1],16)};
					coorList.add(tokenArr);
					str = br.readLine();
					Token token = new Token().setFileId(fileId)
											 .setId(id++)
							                 .setLine(Integer.parseInt(str_split[0],16))
							                 .setColumn(Integer.parseInt(str_split[1],16))
							                 .setToken(str_split[4]);
					tokenList.add(token);
				}
				if(SeedMap.containsKey(fileId))
				for(Seed s:SeedMap.get(fileId)){
					s.setLS(coorList.get(s.getTS())[0]).setLE(coorList.get(s.getTE())[0])
					 .setCS(coorList.get(s.getTS())[1]).setCE(coorList.get(s.getTE())[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			tokenMap.put(fileId,tokenList);
		}
		return tokenMap;
	}
	
	private UpperStream(){}
}
