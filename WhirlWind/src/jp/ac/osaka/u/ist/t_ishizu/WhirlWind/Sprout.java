package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.util.ArrayList;

public class Sprout {
	private int id;
	private ArrayList<Seed> seedList = new ArrayList<Seed>();
	
	/* getter */
	public int getId(){return id;}
	public ArrayList<Seed> getSeedList(){return seedList;}
	
	/* setter */
	public Sprout setId(int id){this.id = id;return this;}
	public Sprout addSeedList(Seed s){seedList.add(s);return this;}
	
	
}
