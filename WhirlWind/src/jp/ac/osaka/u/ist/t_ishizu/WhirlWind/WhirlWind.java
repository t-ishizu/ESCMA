package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
/**
 * 
 * @author t-ishizu
 * 
 */

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.UpperStream.*;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.TokenType.*;
import java.util.ArrayList;
import java.util.HashMap;
public class WhirlWind {
	/**
	 * @author t-ishizu
	 * 
	 */
	public static ArrayList<String> fileList; 
	public static ArrayList<Sprout> sproutList;
	public static HashMap<Integer, ArrayList<Seed>> SeedMap;
	public static String[] CCFXDFileArray;
	public static HashMap<Integer, ArrayList<Token>> tokenMap;
	
	public WhirlWind(){
		
	}
	
	public void initialize(){
		
	}
	
	public void terminate(){
		System.out.println("@WhirlWind.class terminate.method");
		fileList.clear();
		sproutList.clear();
		SeedMap.clear();
		CCFXDFileArray=null;
		tokenMap.clear();
	}
	
	public void run(){
		/* create ArrayList by UpperStream class */ 
		fileList = createFileList();
		SeedMap = createSeedMap();
		sproutList = createSproutList();
		CCFXDFileArray = createCCFXDFileArray();
		tokenMap = createTokenMap();
		confirmGermination();
		terminate();
	}
	
	public void confirmGermination(){
		System.out.println("@WhirlWind.class confirmGermination.method");
		for(int fileId : SeedMap.keySet()){
			for(Seed seed:SeedMap.get(fileId)){
				seed.setInitial(tokenMap.get(fileId).get(seed.getTS()))
				    .setFinal(tokenMap.get(fileId).get(seed.getTE()));
				if(isFunction(seed)){
					System.out.print("@WhirlWind.class Func Germination : ");
					for(int t = seed.getTS();t<=seed.getTE();t++){
						System.out.print(tokenMap.get(fileId).get(t).getToken()+"\t");
					}
					System.out.println();
				}
				if(isParagraph(seed)){
					
				}
			}
		}
	}
	
	public boolean isFunction(Seed seed){
		if(beginFunction(seed)&&endFunction(seed)){
			if(acrossFunction(seed.getTS(),seed)<0){
				return true;
			}
		}
		return false;
	}
	
	public boolean beginFunction(Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		if(tokenList.get(seed.getTS()).getType()==word&&
				tokenList.get(seed.getTS()+1).getToken().equals("suffix:period")){
			return true;
		}
		return false;
	}
	
	public int acrossFunction(int left,Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		if(seed.getTE()-left>=0){
			for(int current=left+1;current<=seed.getTE()-2;current++){
				if(tokenList.get(current).getToken().equals("suffix:period")&&
						tokenList.get(current+1).getType()==word&&
						tokenList.get(current+2).getToken().equals("suffix:period")){
					return current;
				}
			}
		}
		return -1;
	}
	
	public boolean endFunction(Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		if(tokenList.get(seed.getTE()).getToken().equals("suffix:period")){
			if(tokenList.size()-seed.getTE()>2){
				if(tokenList.get(seed.getTE()+1).getType()==word&&
				 tokenList.get(seed.getTE()+2).getToken().equals("suffix:period")){
					return true;
				}
			}else{return true;}
		}
		return false;
	}
	
	public boolean isParagraph(Seed seed){
		
		return false;
	}
}
