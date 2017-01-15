package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.util.ArrayList;

public class OverlapSet {
	private int id;
	private ArrayList<Integer> cloneSetList;
	private int[][] bestMatrix;
	private int[] bestVector;
	private int bestEval;

	public OverlapSet(){
		id=-1;
		cloneSetList = new ArrayList<Integer>();
	}

	public OverlapSet addCloneSet(CloneSet cs){
		this.cloneSetList.add(cs.getId());
		cs.setOverlapSetId(this.id);
		return this;}
	/*setter*/
	public OverlapSet setId(int id){this.id = id;return this;}
	public OverlapSet setMatrix(int x,int y){bestMatrix = new int[x][y];return this;}
	public OverlapSet setVector(int x){bestVector = new int[x];return this;}
	public OverlapSet setEval(int e){this.bestEval=e;return this;}

	/*getter*/
	public int getId(){return id;}
	public ArrayList<Integer> getCloneSetList(){return cloneSetList;}
	public int[][] getMatrix(){return bestMatrix;}
	public int[] getVector(){return bestVector;}
	public int getEval(){return bestEval;}
	
	public String getMessage(){
		return "id: " + id + " " + cloneSetList;
	}
}
