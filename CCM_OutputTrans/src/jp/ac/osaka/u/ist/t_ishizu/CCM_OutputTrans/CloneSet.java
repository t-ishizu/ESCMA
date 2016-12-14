package jp.ac.osaka.u.ist.t_ishizu.CCM_OutputTrans;


import java.util.ArrayList;

public class CloneSet {
	private ArrayList<CodeClone> cloneList = new ArrayList<CodeClone>();
	private boolean packed=false;
	private int cloneSetId=-1;
	public ArrayList<CodeClone> getCloneList(){
		return cloneList;
	}
	public void setClone(CodeClone c){
		cloneList.add(c);
	}
	
	public void allPainted(){
		for(CodeClone c:cloneList){
			c.getPainted();
		}
	}
	
	public void allPacked(){
		for(CodeClone c:cloneList){
			c.getPacked();
		}
		packed = true;
	}
	
	public boolean isPacked(){
		return packed;
	}
	
	public CloneSet setCloneSetId(int id){
		cloneSetId = id;
		return this;
	}
	
	public int getCloneSetId(){
		return cloneSetId;
	}
}

