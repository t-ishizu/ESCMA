package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

public class Token {
	private int id,line,column,fileId;
	private String token;
	
	/* getter */
	public int getId(){return id;}
	public int getFileId(){return fileId;}
	public int getLine(){return line;}
	public int getColumn(){return column;}
	public String getToken(){return token;}
	
	/*setter*/
	public Token setId(int id){this.id = id;return this;}
	public Token setFileId(int fileId){this.fileId=fileId;return this;}
	public Token setLine(int line){this.line=line;return this;}
	public Token setColumn(int column){this.column=column;return this;}
	public Token setToken(String token){this.token=token;return this;}
}
