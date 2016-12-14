package jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class CCM_OutputTrans {
	public static void main(String[] args){
		ArrayList<String> fileIdList = createFileIdList(args[0]);
		HashMap<Integer,ArrayList<CodeClone>> fileToCloneMap = createFileToCloneMap(args[0]);
		ArrayList<CloneSet> cloneSetList = createCloneSetList(fileToCloneMap);
		System.out.println(cloneSetList.size());
		String[] prepHierarchy = getPrepHierarchy(args[0]);
		findCloneLocation(fileIdList, prepHierarchy, fileToCloneMap);
		OutputFile(fileIdList,cloneSetList);
	}
	
	public static ArrayList<String> createFileIdList(String pass){
		BufferedReader br = getBufferedReader(pass);
		ArrayList<String> fileIdList = new ArrayList<String>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("source_files {")){
					canRead = true;
				}else if(str.equals("}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[\\s]+",0);
					fileIdList.add(str_split[1]);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileIdList;
	}
	
	public static HashMap<Integer,ArrayList<CodeClone>> createFileToCloneMap(String pass){
		BufferedReader br = getBufferedReader(pass);
		HashMap<Integer,ArrayList<CodeClone>> fileToCloneMap = new HashMap<Integer,ArrayList<CodeClone>>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("clone_pairs {")){
					canRead = true;
				}else if(str.equals("}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[.,\\-\t]+",0);
					CodeClone c1 = new CodeClone()
					.setCloneSetId(Integer.parseInt(str_split[0]))
					.setFileId(Integer.parseInt(str_split[1]))
					.setToken(Integer.parseInt(str_split[2]),Integer.parseInt(str_split[3]));
					CodeClone c2 = new CodeClone()
					.setCloneSetId(Integer.parseInt(str_split[0]))
					.setFileId(Integer.parseInt(str_split[4]))
					.setToken(Integer.parseInt(str_split[5]), Integer.parseInt(str_split[6]));
					int index1 = -1;
					if(fileToCloneMap.containsKey(c1.getFileId())){
						index1=getCodeCloneIndex(fileToCloneMap.get(c1.getFileId()),c1);
					}else{
						fileToCloneMap.put(c1.getFileId(), new ArrayList<CodeClone>());
					}
					int index2 = -1;
					if(fileToCloneMap.containsKey(c2.getFileId())){
						index2=getCodeCloneIndex(fileToCloneMap.get(c2.getFileId()),c2);
					}else{
						fileToCloneMap.put(c2.getFileId(), new ArrayList<CodeClone>());
					}
					if(index1==-1&&index2==-1){
						fileToCloneMap.get(c1.getFileId()).add(c1);
						fileToCloneMap.get(c2.getFileId()).add(c2);
						CloneSet cs = new CloneSet();
						c1.setParent(cs);
						c2.setParent(cs);
						cs.getCloneList().add(c1);
						cs.getCloneList().add(c2);
					}else if(index1!=-1&&index2==-1){
						fileToCloneMap.get(c2.getFileId()).add(c2);
						CloneSet cs = fileToCloneMap.get(c1.getFileId()).get(index1).getParent();
						c2.setParent(cs);
						cs.getCloneList().add(c2);
					}else if(index1==-1&&index2!=-1){
						fileToCloneMap.get(c1.getFileId()).add(c1);
						CloneSet cs = fileToCloneMap.get(c2.getFileId()).get(index2).getParent();
						c1.setParent(cs);
						cs.getCloneList().add(c1);
					}
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileToCloneMap;
	}
	
	public static ArrayList<CloneSet> createCloneSetList(HashMap<Integer,ArrayList<CodeClone>>map){
		ArrayList<CloneSet> cloneSetList = new ArrayList<CloneSet>();
		int cloneSetId=0;
		for(ArrayList<CodeClone> list:map.values()){
			for(CodeClone c : list){
				if(c.getParent().getCloneSetId()==-1){
					cloneSetList.add(c.getParent().setCloneSetId(cloneSetId++));
				}
			}
		}
		return cloneSetList;
	}
	
	
	public static int getCodeCloneIndex(ArrayList<CodeClone>list,CodeClone clone){
		for(int i=0;i<list.size();i++){
			CodeClone c = list.get(i);
			if(c.getTS()==clone.getTS()&&c.getTE()==clone.getTE()){
				return i;
			}
		}
		return -1;
	}
	
	public static String[] getPrepHierarchy(String pass){
		BufferedReader br = getBufferedReader(pass);
		String[] prepHierarchy = new String[2];
		try{
			String str = br.readLine();
			while(str!=null){
				String[] str_split = str.split("[\\s]+",0);
				if(str_split[0].equals("option:")){
					if(str_split[1].equals("-preprocessed_file_postfix")){
						prepHierarchy[0] = str_split[2];
					}else if(str_split[1].equals("-n")){
						prepHierarchy[1] = str_split[2];
						if(!prepHierarchy[0].isEmpty())
						return prepHierarchy;
					}
				}
				str = br.readLine();
			}
					
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return prepHierarchy;
	}
	
	public static void findCloneLocation(ArrayList<String> fileIdList,String[] prepHierarchy,HashMap<Integer,ArrayList<CodeClone>> map){
		for(int fileId:map.keySet()){
			StringBuffer sb = new StringBuffer();
			sb.append(prepHierarchy[1]);
			sb.append("\\.ccfxprepdir");
			sb.append(fileIdList.get(fileId-1).substring(prepHierarchy[1].length(), fileIdList.get(fileId-1).length()));
			sb.append(prepHierarchy[0]);
			String prepLocation =  sb.toString();
			BufferedReader br = getBufferedReader(prepLocation);
			try {
				String str = br.readLine();
				ArrayList<Integer[]> locList = new ArrayList<Integer[]>();
				while(str!=null){
					String[] str_split = str.split("[.\\s]+",0);
					Integer[] loc = new Integer[]{
							Integer.parseInt(str_split[0],16),Integer.parseInt(str_split[1],16)};
					locList.add(loc);
					str = br.readLine();
				}
				for(CodeClone c:map.get(fileId)){
					c.setLine(locList.get(c.getTS())[0],locList.get(c.getTE())[0])
					.setColumn(locList.get(c.getTS())[1], locList.get(c.getTE())[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public  static void OutputFile(ArrayList<String> fileList,ArrayList<CloneSet> cloneSetList){
		PrintWriter pw = getPrintWriter(new File("c.txt"));
		pw.println("#begin{file description}");
			for(int i=0;i<fileList.size();i++){
				pw.println("0."+i+"\t0\t0\t"+fileList.get(i));
			}
		pw.println("#end{file description}");
		pw.println("#begin{clone}");
		for(CloneSet cs : cloneSetList){
			pw.println("#begin{set}");
			for(CodeClone c:cs.getCloneList()){
				/*fileId LS,CS,TS LE,CE,TE, LNR*/
				pw.println(c.getCloneSetId()+"\t0."+(c.getFileId()-1)+"\t"+c.getLS()+","+c.getCS()+","+c.getTS()
						+"\t"+c.getLE()+","+c.getCE()+","+c.getTE()+"\t0");
			}
			pw.println("#end{set}");
		}
		pw.println("#end{clone}");
		pw.close();
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
	
	public static PrintWriter getPrintWriter(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,true)));
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	} 
}
