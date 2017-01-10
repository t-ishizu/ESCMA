package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
/**
 *
 * @author t-ishizu
 *
 */

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.TokenType.*;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.UpperStream.*;

import java.util.ArrayList;
import java.util.HashMap;
public class WhirlWind {
	/**
	 * @author t-ishizu
	 *
	 */
	public static final int BASIC = 0;
	public static final int GREEDY = 1;
	public static int slimmingMode = BASIC;
	/**
	 * slimmingMode
	 * 0 BASIC
	 * 1 HEURISTIC GREEDY
	 * 2 HEURISTIC
	 */

	public static ArrayList<String> fileList;
	public static ArrayList<Sprout> sproutList;
	public static HashMap<Integer, ArrayList<Seed>> SeedMap;
	public static String[] CCFXDFileArray;
	public static HashMap<Integer, ArrayList<Token>> tokenMap;


	public WhirlWind(){
		initialize();
	}

	public void initialize(){
		/* create ArrayList by UpperStream class */
		System.out.println("@WhirlWind.initialize()");
		fileList = createFileList();
		SeedMap = createSeedMap();
		sproutList = createSproutList();
		CCFXDFileArray = createCCFXDFileArray();
		tokenMap = createTokenMap();
	}

	public void terminate(){
		System.out.println("@WhirlWind.terminate()");
		fileList.clear();
		sproutList.clear();
		SeedMap.clear();
		CCFXDFileArray=null;
		tokenMap.clear();
	}

	public void run(){
		confirmGermination();
		terminate();
	}

	public void confirmGermination(){
		System.out.print("@WhirlWind.confirmGermination() ");
		if(slimmingMode==BASIC)System.out.println("@BASIC MODE:");
		else System.out.println("@HEURISTIC MODE:");
		for(int fileId : SeedMap.keySet()){
			for(Seed seed:SeedMap.get(fileId)){
				seed.setInitial(tokenMap.get(fileId).get(seed.getTS()))
				    .setFinal(tokenMap.get(fileId).get(seed.getTE()));
				if(slimmingMode == BASIC){

					if(isFunction(seed)){
						System.out.print("@Func Germination : ");
						for(int t = seed.getTS();t<=seed.getTE();t++){
							System.out.print(tokenMap.get(fileId).get(t).getToken()+"\t");
						}
						System.out.println();
					}
				}else{
					if(isParagraph(seed)){

					}
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

	public Seed deletePrefix(Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		if(seed.getTS()==0) return seed;
		if(seed.getTS()!=0){
			if(tokenList.get(seed.getTS()-1).getType()==suffix){
				return seed;
			}
		}
		/*TS!=0 && token(TS-1).Type!=suffix*/
		int findIndex=seed.getTE();
		for(int index=seed.getTS();index<=seed.getTE();index++){

		}
		return seed;
	}

	public Seed splitSeed(Seed seed){

		return seed;
	}

	public Seed deleteSuffix(Seed seed){

		return seed;
	}
	public boolean isParagraph(Seed seed){
		/*begin*/
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		if(seed.getTS()!=0){
			if(!tokenList.get(seed.getTS()-1).getToken().equals("suffix:period")){
				return false;
			}
		}
		/*mid*/
		if(acrossFunction(seed.getTS(),seed)>0){
			return false;
		}
		/*end*/
		if(tokenList.get(seed.getTE()).getToken().equals("suffix:period")){
			return true;
		}
		return false;
	}


}
