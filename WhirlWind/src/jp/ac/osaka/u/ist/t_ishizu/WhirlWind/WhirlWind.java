package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
/**
 * 
 * @author t-ishizu
 * 
 */

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.UpperStream.*;

import java.util.ArrayList;
public class WhirlWind {
	/**
	 * @author t-ishizu
	 * 
	 */
	public static ArrayList<String> fileList; 
	public WhirlWind(){
		
	}
	public void run(){
		fileList = createFileList();
	}
}
