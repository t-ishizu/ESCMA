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
	public static int slimmingMode = GREEDY;//BASIC;
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
	public static int splitedSeedId = -2;
	public static ArrayList<CloneSet> cloneSetList;

	public WhirlWind(){

	}

	public void initialize(){
		/* create ArrayList by UpperStream class */
		System.out.println("@WhirlWind.initialize()");
		fileList = createFileList();
		SeedMap = createSeedMap();
		sproutList = createSproutList();
		CCFXDFileArray = createCCFXDFileArray();
		tokenMap = createTokenMap();
		splitedSeedId = -2;
	}

	public void terminate(){
		System.out.println("@WhirlWind.terminate()");
		fileList.clear();
		sproutList.clear();
		SeedMap.clear();
		CCFXDFileArray=null;
		tokenMap.clear();
		splitedSeedId = -2;
		cloneSetList.clear();
	}

	public void run(){
		initialize();
		confirmGermination();
		Boxing();

		terminate();
	}

	public void confirmGermination(){
		System.out.print("@WhirlWind.confirmGermination() ");
		if(slimmingMode==BASIC)System.out.println("@BASIC MODE:");
		else System.out.println("@HEURISTIC MODE:");
		for(int fileId : SeedMap.keySet()){
			int size = SeedMap.get(fileId).size();
			for(int index=0;index<size;index++){
				Seed seed = SeedMap.get(fileId).get(index);
				seed.setInitial(tokenMap.get(fileId).get(seed.getTS()))
				    .setFinal(tokenMap.get(fileId).get(seed.getTE()));
				if(findSuffix(seed)){
					if(slimmingMode == BASIC){
						if(isFunction(seed)){
							System.out.print("@Func Germination : ");
							for(int t = seed.getTS();t<=seed.getTE();t++){
								System.out.print(tokenMap.get(fileId).get(t).getToken()+"\t");
							}
							System.out.println();
						}
					}else{
						seed = deletePrefix(seed);
						ArrayList<Seed> splitList = splitSeed(seed);
						System.out.println("***************");
						for(int j=0;j<splitList.size();j++){
							Seed s = splitList.get(j);
						//for(Seed s:splitSeed(seed)){
							if(isParagraph(s))s.isValid();
							else{
								s=deleteSuffix(s);
								if(s.getTS()!=s.getTE()) s.isValid();
								else s.isInvalid();
							}
							System.out.println("@Para Germination : " + s.messageSeed());
							for(int t = s.getTS();t<=s.getTE();t++){
								System.out.print(tokenMap.get(fileId).get(t).getToken()+"\t");
							}
							System.out.println();
							System.out.println();
						}
					}
				}
			}
		}
	}

	public boolean findSuffix(Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		for(int index = seed.getTS();index<=seed.getTE();index++){
			if(tokenList.get(index).getType()==suffix){
				return true;
			}
		}
		return false;
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
		/*with @WhirlWind.findSuffix(), this method must find suffix.*/
		int minIndex=seed.getTE();
		for(int index=seed.getTS();index<=seed.getTE();index++){
			if(tokenList.get(index).getType()==suffix){
				if(minIndex<index) minIndex=index+1;
			}
		}
		seed.setTS(minIndex+1);
		return seed;
	}



	public ArrayList<Seed> splitSeed(Seed seed){
		ArrayList<Seed> splitedSeedList = new ArrayList<Seed>();
		boolean splited = (seed.getSprout().getSplitList().size()>0) ;
		int left = seed.getTS();
		int right = acrossFunction(left,seed);
		int loop = 0;
		while(right>=0){
			Seed preSeed = new Seed();
			if(splited){
				preSeed.setId(seed.getSprout().getSplitList().get(loop))
				 .setFileId(seed.getFileId()).setTS(left).setTE(right)
				 .setSprout(sproutList.get(seed.getSprout().getSplitSproutList().get(loop)));
			}else{
				Sprout preSprout = new Sprout();
				preSeed.setId(splitedSeedId)
				.setFileId(seed.getFileId()).setTS(left).setTE(right).setSprout(preSprout);
				seed.getSprout().addSplit(splitedSeedId--);
				seed.getSprout().addSplitSprout(sproutList.size());
				sproutList.add(preSprout.setId(sproutList.size()));
			}
			preSeed.getSprout().addSeedList(preSeed);
			splitedSeedList.add(preSeed);
			SeedMap.get(seed.getFileId()).add(preSeed);
			left = right+1;
			right = acrossFunction(left,seed);
			loop++;
		}
		splitedSeedList.add(seed.setTS(left));
		return splitedSeedList;
	}

	public Seed deleteSuffix(Seed seed){
		ArrayList<Token> tokenList = tokenMap.get(seed.getFileId());
		int maxIndex=-1;
		for(int index = seed.getTE();index>=seed.getTS();index--){
			if(tokenList.get(index).getType()==suffix){
				if(maxIndex<index){
					maxIndex=index;
				}
			}
		}
		if(maxIndex!=-1) seed.setTE(maxIndex);
		else seed.setTE(seed.getTS());
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
		if(tokenList.get(seed.getTE()).getType()==suffix){
			return true;
		}
		return false;
	}

	public void Boxing(){
		cloneSetList = createCloneSetList();
		identifyOverlap();
	}
	public ArrayList<CloneSet> createCloneSetList(){
		System.out.println("@whirWind.createCloneSetList()");
		ArrayList<CloneSet>cloneSetList = new ArrayList<CloneSet>();
		for(int i=0;i<sproutList.size();i++){
			boolean valid = true;
			for(Seed seed:sproutList.get(i).getSeedList()){
				valid = valid && seed.getValid();
				seed.setInitial(tokenMap.get(seed.getFileId()).get(seed.getTS()))
			    .setFinal(tokenMap.get(seed.getFileId()).get(seed.getTE()));
			}
			if(valid && sproutList.size()>1 && !isOverlapWithinSameCloneSet(sproutList.get(i))){
				CloneSet cloneSet = new CloneSet()
				.addSeedList(sproutList.get(i).getSeedList())
				.calcWeight().isDispersive();
				cloneSetList.add(cloneSet.setId(cloneSetList.size()));

			}
		}
		return cloneSetList;
	}

	public boolean isOverlap(Seed seed1,Seed seed2){
		if(seed1.getFileId()!=seed2.getFileId()) return false;
		if((seed1.getTE()-seed2.getTS())*(seed2.getTE()-seed1.getTS())>=0){
			return true;
		}else return false;
	}

	public boolean isOverlapWithinSameCloneSet(Sprout sprout){
		for(int i=0;i<sprout.getSeedList().size()-1;i++){
			for(int j=i+1;j<sprout.getSeedList().size();j++){
				if(isOverlap(sprout.getSeedList().get(i),sprout.getSeedList().get(j))){
					return true;
				}
			}
		}
		return false;
	}

	public void identifyOverlap(){

	}

}
