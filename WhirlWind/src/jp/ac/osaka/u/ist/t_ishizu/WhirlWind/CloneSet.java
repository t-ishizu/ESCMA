package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.util.ArrayList;

public class CloneSet {
	private int id;
	private ArrayList<Seed> seedList;
	private int weight;
	private int overlapSetIndex;
	private int overlapSetId;
	private boolean dispersive;
	private ArrayList<Integer> overlapCloneSetList;
	private boolean slimming=false;
	private boolean hot=false;
	private int size;

	public CloneSet(){
		overlapSetId = -1;
		seedList = new ArrayList<Seed>();
		overlapCloneSetList = new ArrayList<Integer>();
	}

	public CloneSet addSeed(Seed seed){
		seedList.add(seed);
		seed.setCloneSet(this);
		return this;
	}

	/* setter */
	public CloneSet setId(int id){this.id=id;return this;}
	public CloneSet setWeight(int weight){this.weight=weight;return this;}
	public CloneSet setOverlapSetIndex(int overlapSetIndex){this.overlapSetIndex=overlapSetIndex;return this;}
	public CloneSet setOverlapSetId(int overlapSetId){this.overlapSetId=overlapSetId;return this;}
	public CloneSet setDispersive(boolean dispersive){this.dispersive=dispersive;return this;}
	public CloneSet setSlimming(){this.slimming=true;return this;}
	public CloneSet setFatting(){this.slimming=false;return this;}
	public CloneSet setHot(){this.hot=true;return this;}
	public CloneSet setSize(){this.size=size;return this;}
	public CloneSet addSeedList(ArrayList<Seed> list){
		this.seedList.addAll(list);
		for(Seed seed:list){
			seed.setCloneSet(this);
		}
		return this;
	}
	public CloneSet addOverlapCloneSetList(CloneSet cs){
		if(!overlapCloneSetList.contains(cs.getId())){
			overlapCloneSetList.add(cs.getId());
			cs.addOverlapCloneSetList(this);
		}
		return this;
	}

	public CloneSet calcWeight(){
		if(seedList!=null){
			int w = 0;
			for(Seed seed : seedList){
				w += seed.calcWeight().getWeight();
			}
			weight = w;
			size=weight/seedList.size();
		}
		return this;
	}

	public CloneSet isDispersive(){
		dispersive = false;
		int iniFileId = -1;
		if(seedList!=null){
			if(seedList.size()>0)iniFileId = seedList.get(0).getFileId();
		}
		for(Seed seed : seedList){
			if(seed.getFileId()!=iniFileId){
				dispersive = true;
			}
		}
		return this;
	}
	/* getter */
	public int getId(){return id;}
	public ArrayList<Seed> getSeedList(){return seedList;}
	public int getWeight(){return weight;}
	public int getOverlapSetIndex(){return overlapSetIndex;}
	public int getOverlapSetId(){return overlapSetId;}
	public boolean getDispersive(){return dispersive;}
	public boolean isSlimming(){return slimming;}
	public boolean isHot(){return hot;}
	public int getSize(){return size;}
	public ArrayList<Integer> getOverlapCloneSetList(){return overlapCloneSetList;}
}
