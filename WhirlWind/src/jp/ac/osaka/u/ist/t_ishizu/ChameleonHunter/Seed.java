package jp.ac.osaka.u.ist.t_ishizu.ChameleonHunter;

public class Seed {
	private int id=-1;//original clone id, but splited seed is identified by minus number less than -2
	private int fileId=-1,LS,LE,CS,CE,TS,TE;
	private Sprout sprout;
	//private CloneSet cloneSet;
	private Token initialToken;
	private Token finalToken;
	private boolean valid = false;
	private int weight;

	/* getter */
	public int getId(){return id;}
	public int getFileId(){ return fileId;}
	public int getLS(){ return LS;}
	public int getLE(){ return LE;}
	public int getCS(){ return CS;}
	public int getCE(){ return CE;}
	public int getTS(){ return TS;}
	public int getTE(){ return TE;}
	public Sprout getSprout(){ return sprout;}
	//public CloneSet getCloneSet(){return cloneSet;}
	public Token getInitial(){return initialToken;}
	public Token getFinal(){return finalToken;}
	public boolean getValid(){return valid;}
	public int getWeight(){return weight;}

	/* setter */
	public Seed setId(int id){this.id = id; return this;}
	public Seed setFileId(int FileId){this.fileId = FileId; return this;}
	public Seed setLS(int LS){this.LS = LS; return this;}
	public Seed setLE(int LE){this.LE = LE; return this;}
	public Seed setCS(int CS){this.CS = CS; return this;}
	public Seed setCE(int CE){this.CE = CE; return this;}
	public Seed setTS(int TS){this.TS = TS; return this;}
	public Seed setTE(int TE){this.TE = TE; return this;}
	public Seed setSprout(Sprout sp){this.sprout = sp; return this;}
	//public Seed setCloneSet(CloneSet cs){this.cloneSet = cs; return this;}
	public Seed setInitial(Token t){this.initialToken = t; return this;}
	public Seed setFinal(Token t){this.finalToken = t; return this;}

	public Seed isValid(){this.valid=true;return this;}
	public Seed isInvalid(){this.valid=false;return this;}

	public Seed calcWeight(){
		TS = initialToken.getLine();TE = finalToken.getLine();
		weight = TE - TS + 1;
		return this;
	}

	public String messageSeed(){
		return "valid: "+valid+" id: "+id+" fileId: "+fileId+" TS: "+TS+" TE: "+TE+" sproutId: "+sprout.getId();
	}
}
