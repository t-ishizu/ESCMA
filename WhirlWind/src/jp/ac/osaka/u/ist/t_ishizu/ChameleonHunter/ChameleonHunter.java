package jp.ac.osaka.u.ist.t_ishizu.ChameleonHunter;

import java.util.ArrayList;
import java.util.HashMap;

import static jp.ac.osaka.u.ist.t_ishizu.ChameleonHunter.UpperStream.*;
public class ChameleonHunter {
	public static ArrayList<String> fileList;
	public static ArrayList<Sprout> sproutList;
	public static HashMap<Integer,ArrayList<Seed>> SeedMap;
	public static String[] CCFXDFileArray;
	public static HashMap<Integer, ArrayList<Token>> tokenMap;
	
	public ChameleonHunter(){

	}

	public void initialize(){
		System.out.println("@ChameleonHunter.initialize()");
		fileList = createFileList();
		SeedMap = createSeedMap();
		sproutList = createSproutList();
		CCFXDFileArray = createCCFXDFileArray();
		tokenMap = createTokenMap();
		
	}

	public void terminate(){
		System.out.println("@ChameleonHunter.terminate()");
		fileList.clear();
		sproutList.clear();
		SeedMap.clear();
		CCFXDFileArray = null;
		tokenMap.clear();
		
	}

	public void run(){
		initialize();

		//should stop
	}
	
	public void stop(){
		//must run before stop()
		terminate();
	}

	public void createHTML(){
		
	}
}
