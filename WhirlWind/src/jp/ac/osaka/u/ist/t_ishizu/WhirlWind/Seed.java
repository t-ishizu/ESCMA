package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

public class Seed {
	private int id;
	private int fileId,LS,LE,CS,CE,TS,TE;
	private Sprout sprout;
	
	/* getter */
	public int getId(){return id;};
	public int getFileId(){ return fileId;};
	public int getLS(){ return LS;};
	public int getLE(){ return LE;};
	public int getCS(){ return CS;};
	public int getCE(){ return CE;};
	public int getTS(){ return TS;};
	public int getTE(){ return TE;};
	public Sprout getSprout(){ return sprout;};
	
	/* setter */
	public Seed setId(int id){this.id = id; return this;};
	public Seed setFileId(int FileId){this.fileId = FileId; return this;};
	public Seed setLS(int LS){this.LS = LS; return this;};
	public Seed setLE(int LE){this.LE = LE; return this;};
	public Seed setCS(int CS){this.CS = CS; return this;};
	public Seed setCE(int CE){this.CE = CE; return this;};
	public Seed setTS(int TS){this.TS = TS; return this;};
	public Seed setTE(int TE){this.TE = TE; return this;};
	public Seed setSprout(Sprout sp){this.sprout = sp; return this;};
}
