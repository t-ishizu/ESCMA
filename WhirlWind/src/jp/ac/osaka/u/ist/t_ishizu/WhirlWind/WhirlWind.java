package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
/**
 * 
 * @author t-ishizu
 * 
 */

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.UpperStream.*;

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
	public void run(){
		/* create ArrayList by UpperStream class */ 
		fileList = createFileList();
		SeedMap = createSeedMap();
		sproutList = createSproutList();
		CCFXDFileArray = createCCFXDFileArray();
		tokenMap = createTokenMap();
	}
}
