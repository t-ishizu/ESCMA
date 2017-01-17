package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.io.File;

public class MyOption {
	public static final int BASIC = 0;
	public static final int GREEDY = 1;
	public static int slimmingMode = GREEDY;//BASIC;
	/**
	 * slimmingMode
	 * 0 BASIC
	 * 1 HEURISTIC GREEDY
	 * 2 HEURISTIC
	 */

	private static String seedFile = "b.txt";

	public static void setSeedFile(File seedfile){seedFile=seedfile.getPath();}

	public static String getSeedFile(){return seedFile;}
}
