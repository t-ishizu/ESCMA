package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.util.ArrayList;

public class Sprout {
	private int id=-1; //NOT original clone id , This is sequence id
	private ArrayList<Seed> seedList;
	private ArrayList<Integer> splitIdList;
	private ArrayList<Integer> spliteSproutIdList;

	public Sprout(){
		seedList = new ArrayList<Seed>();
		splitIdList = new ArrayList<Integer>();
		spliteSproutIdList = new ArrayList<Integer>();
	}


	/* getter */
	public int getId(){return id;}
	public ArrayList<Seed> getSeedList(){return seedList;}
	public ArrayList<Integer> getSplitList(){return splitIdList;}
	public ArrayList<Integer> getSplitSproutList(){return spliteSproutIdList;}
	
	/* setter */
	public Sprout setId(int id){this.id = id;return this;}
	public Sprout addSeedList(Seed s){seedList.add(s);return this;}
	public Sprout addSplit(int splitId){splitIdList.add(splitId);return this;}
	public Sprout addSplitSprout(int splitSproutId){spliteSproutIdList.add(splitSproutId);return this;}

}
