package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
/**
 *
 * @author t-ishizu
 *
 */

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.TokenType.*;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.UpperStream.*;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.Option.*;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.LowerStream.*;
import java.util.ArrayList;
import java.util.Collections;
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
	public static int splitedSeedId = -2;
	public static ArrayList<CloneSet> cloneSetList;
	public static HashMap<Integer,OverlapSet> overlapMap;

	public WhirlWind(){

	}

	public void initialize(){
		/* create ArrayLists by UpperStream class */
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
		overlapMap.clear();
	}
	
	public void run(){
		initialize();
		confirmGermination();
		Boxing();
		switch(slimmingMode){
		case BASIC:
			break;
		case GREEDY:
			greedy();
			break;
		default :
			break;
		}
		resultMessage("c.txt");
		updateSeedFile("d.txt");
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

/**
 * Boxing()
 * This method create Clone Set List to estimate Slimming.
 *
 */

	public void Boxing(){
		System.out.println("@whirWind.Boxing()");
		cloneSetList = createCloneSetList();
		identifyOverlap();
		overlapMap = createOverlapMapBetweenCloneSet();
		
	}
	
	public ArrayList<CloneSet> createCloneSetList(){
		ArrayList<CloneSet>cloneSetList = new ArrayList<CloneSet>();
		for(int i=0;i<sproutList.size();i++){
			boolean valid = true;
			for(Seed seed:sproutList.get(i).getSeedList()){
				valid = valid && seed.getValid();
				seed.setInitial(tokenMap.get(seed.getFileId()).get(seed.getTS()))
			    .setFinal(tokenMap.get(seed.getFileId()).get(seed.getTE()));
			}
			if(valid && sproutList.size()>1 && !isOverlapWithinSameCloneSet(sproutList.get(i))){
				sproutList.get(i).isValid();
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
		for(int fileId:SeedMap.keySet()){
			for(int i=0;i<SeedMap.get(fileId).size()-1;i++){
				Seed seed1 = SeedMap.get(fileId).get(i);
				if(seed1.getSprout().getValid()){
					for(int j=i+1;j<SeedMap.get(fileId).size();j++){
						Seed seed2 = SeedMap.get(fileId).get(j);
						if(isOverlap(seed1,seed2)&&seed2.getSprout().getValid()){
							seed1.getCloneSet().addOverlapCloneSetList(seed2.getCloneSet());
						}
					}
				}
			}
		}
		System.out.println("@Overlap : ");
		for(CloneSet cs:cloneSetList){
			System.out.println("id: "+cs.getId()+" "+cs.getOverlapCloneSetList());
		}
	}

	public HashMap<Integer,OverlapSet> createOverlapMapBetweenCloneSet(){
		System.out.println("@WhirlWind.createOverlapMapBetweenCloneSet()");
		HashMap<Integer,OverlapSet> overlapMap = new HashMap<Integer,OverlapSet>();
		int id = 0;
		for(int i=0;i<cloneSetList.size()-1;i++){
			CloneSet cloneSet1 = cloneSetList.get(i);
			for(int j=i+1;j<cloneSetList.size();j++){
				CloneSet cloneSet2 = cloneSetList.get(j);
				if(cloneSet1.getOverlapCloneSetList().contains(cloneSet2.getId())){
					if(cloneSet1.getOverlapSetId()==-1 && cloneSet2.getOverlapSetId()==-1){
						overlapMap.put(id,new OverlapSet().setId(id)
						.addCloneSet(cloneSet1).addCloneSet(cloneSet2));
						id++;
					}else if(cloneSet1.getOverlapSetId() == -1){
						overlapMap.get(cloneSet2.getOverlapSetId()).addCloneSet(cloneSet1);
					}else if(cloneSet2.getOverlapSetId() == -1){
						overlapMap.get(cloneSet1.getOverlapSetId()).addCloneSet(cloneSet2);
					}else if(cloneSet1.getOverlapSetId()!=cloneSet2.getOverlapSetId()){
						OverlapSet overlapSet1 = overlapMap.get(cloneSet1.getOverlapSetId());
						OverlapSet overlapSet2 = overlapMap.get(cloneSet2.getOverlapSetId());
						for(int cloneSetId : overlapSet2.getCloneSetList()){
							overlapSet1.addCloneSet(cloneSetList.get(cloneSetId));
						}
						overlapMap.remove(cloneSet2.getOverlapSetId());
					}
				}
			}
		}
		for(OverlapSet overlapSet : overlapMap.values())
		System.out.println(overlapSet.getMessage());
		return overlapMap;
	}
	
	public void greedy(){
		Collections.sort(cloneSetList,new WeightComparator());
		for(int i=0;i<cloneSetList.size();i++){
			CloneSet cloneSet = cloneSetList.get(i);
			if(!cloneSet.isHot()){
				cloneSet.setHot().setSlimming();
				for(int index : cloneSet.getOverlapCloneSetList()){
					cloneSetList.get(index).setHot();
				}
			}
		}
	}
}
