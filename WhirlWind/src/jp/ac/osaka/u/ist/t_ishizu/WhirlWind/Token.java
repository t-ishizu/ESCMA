package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;
import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.TokenType.*;
public class Token {
	private int id,line,column,fileId;
	private String token;
	private String type;
	
	/* getter */
	public int getId(){return id;}
	public int getFileId(){return fileId;}
	public int getLine(){return line;}
	public int getColumn(){return column;}
	public String getToken(){return token;}
	public String getType(){return type;}
	
	/*setter*/
	public Token setId(int id){this.id = id;return this;}
	public Token setFileId(int fileId){this.fileId=fileId;return this;}
	public Token setLine(int line){this.line=line;return this;}
	public Token setColumn(int column){this.column=column;return this;}
	public Token setToken(String token){this.token=token;setType();return this;}
	
	public void setType(){
		if(token.startsWith("r_")){
			type = reserved;
		}else if(token.startsWith("word|")){
			type = word;
		}else if(token.startsWith("suffix:")){
			type = suffix;
		}else if(token.startsWith("comma")){
			type = comma;
		}else if(token.startsWith("c_")){
			type = conditional;
		}else if(token.startsWith("LP")){
			type = left_parenthesis;
		}else if(token.startsWith("RP")){
			type = right_parenthesis;
		}else if(token.startsWith("l_")){
			type = literal;
		}else if(token.startsWith("op_")){
			type = operator;
		}else if(token.startsWith("eof")){
			type = eof;
		}else if(token.endsWith("division_block")){
			type = division_block;
		}else if(token.endsWith("block")){
			type = block;
		}else{
			System.out.println("@Type Unknown:"+fileId+" : "+id+" : " + token + " : Line "+line );
			type = unknown;
		}
	}
}
