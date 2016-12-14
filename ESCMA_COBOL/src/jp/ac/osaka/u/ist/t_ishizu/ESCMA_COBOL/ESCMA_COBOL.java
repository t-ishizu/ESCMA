package jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL;

import static jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.CloneLevel.*;
import static jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.Method.*;
import static jp.ac.osaka.u.ist.t_ishizu.ESCMA_COBOL.TokenKind.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.moeaframework.Executor;

public class ESCMA_COBOL {
	public static int LOC = 0;
	public static int SLOC = 0;
	public static int CLOC =0;
	//public static int FCLOC = 0;
	public static int OLOC = 0;
	//public static int FOLOC =0;
	public static int numOfClones = 0;
	//public static int FnumOfClones = 0;
	public static int numOfCloneSets = 0;
	//public static int FnumOfCloneSets = 0;
	public static int FunctionLevelR = 0;
	public static int GreedyLevelR = 0;
	public static int HCLevelR = 0;
	public static int SALevelR = 0;
	public static int GALevelR =0;
	static long totalTime = 0;
	static long start;
	static long end;
	static long FunctionTime;
	static long GreedyTime;
	static long HCTime;
	static long SATime;
	static long GATime;
	public static int level = 0;
	public static int iterateIndex=0;
	//public static boolean timer = false;
	public static int THRESHOLD_RNR = 50;

	public static void main(String[] args){
		/*
		if(args.length==0){
			System.out.println("ESCMA version \""+2+"." + 1+ "\"");
			System.out.println("Usage:java jar ESCMA.jar CCFinder�̏o�͌��ʃt�@�C���̃p�X");
			System.exit(0);
		}
		*/
		//System.out.println("ESCMA version \""+2+"." + 1+ "\"");
		//System.out.println(args[0]);
		PrintWriter pw = getPrintWriter(new File("result.txt"));
		ArrayList<String> fileIdList = createFileIdList(args[0]);
		ArrayList<CloneSet> cloneSetList = createCloneSetList(args[0]);
		HashMap<Integer,ArrayList<CodeClone>>fileToCloneMap = createFileToCloneMap(cloneSetList);
		level = Greedy;
		createPackedCloneList(fileIdList, fileToCloneMap,cloneSetList);
		numOfClones = countNumClone(cloneSetList);
		numOfCloneSets = countNumCloneSet(cloneSetList);
		//System.out.println("RNR=50�ł�");
		//System.out.println("*�t�B���^�����O�O�̊e���g���N�X");
		//System.out.println("�t�@�C���̌�\t�R�[�h�N���[���̌�\t�N���[���Z�b�g�̌�\tLOC\tSLOC\tCLOC\t�I�[�o�[���b�v���Ă���s��");
		System.out.print(fileIdList.size()+"\t"+numOfClones+"\t"+numOfCloneSets+"\t"+LOC+"\t"+SLOC+"\t"+CLOC+"\t"+OLOC);
		System.out.print("\t");
		pw.print(fileIdList.size()+"\t"+numOfClones+"\t"+numOfCloneSets+"\t"+LOC+"\t"+SLOC+"\t"+CLOC+"\t"+OLOC);
		pw.print("\t");
		clearSCCList(cloneSetList);
		//System.out.println();

		/*function level*/
		level = functionLevel;
		cloneSetList = createCloneSetList(args[0]);
		fileToCloneMap = createFileToCloneMap(cloneSetList);
		startTimer();
		createPackedCloneList(fileIdList,fileToCloneMap,cloneSetList);
		//FnumOfClones = countNumClone(cloneSetList);
		//FnumOfCloneSets = countNumCloneSet(cloneSetList);
		//System.out.println("*�t�B���^�����O��̍s���Ɋւ��郁�g���N�X");
		//System.out.println("CLOC\t�I�[�o�[���b�v���Ă���s��");
		//System.out.print(FCLOC+"\t"+FOLOC);
		System.out.print("\t");
		//System.out.println();
		//System.out.println("*�t�B���^�����O��̃N���[���̌�:BASIC");
		//System.out.println("�R�[�h�N���[���̌�\t�N���[���Z�b�g�̌�");
		//System.out.println(FnumOfClones+"\t"+FnumOfCloneSets);
		openCloneSet(fileIdList,cloneSetList);
		clearSCCList(cloneSetList);
		//System.out.println("BASIC");
		System.out.print(FunctionLevelR);
		System.out.print("\t");
		pw.print(FunctionLevelR);
		pw.print("\t");
		FunctionTime = endTimer();
		//System.out.println("time(ms)");
		System.out.print(FunctionTime);
		System.out.print("\t");
		pw.print(FunctionTime);
		pw.print("\t");
		//System.out.println();

		/*heuristic level*/
		level++;
		cloneSetList = createCloneSetList(args[0]);
		fileToCloneMap = createFileToCloneMap(cloneSetList);
		startTimer();
		createPackedCloneList(fileIdList,fileToCloneMap,cloneSetList);
		//FnumOfCloneSets = FnumOfClones = 0;
		//FnumOfClones = countNumClone(cloneSetList);
		//FnumOfCloneSets = countNumCloneSet(cloneSetList);
		//System.out.println("*�t�B���^�����O��̊e�N���[���̌�:HEURISTIC");
		//System.out.println("�R�[�h�N���[���̌�\t�N���[���Z�b�g�̌�");
		//System.out.println(FnumOfClones+"\t"+FnumOfCloneSets);
		openCloneSet(fileIdList,cloneSetList);
		//System.out.println("Greedy\tHC\tSA\tGA");
		System.out.print(GreedyLevelR+"\t");
		pw.print(GreedyLevelR+"\t");
		clearSCCList(cloneSetList);
		GreedyTime = endTimer();

		level++;
		cloneSetList = createCloneSetList(args[0]);
		fileToCloneMap = createFileToCloneMap(cloneSetList);
		startTimer();
		createPackedCloneList(fileIdList,fileToCloneMap,cloneSetList);
		openCloneSet(fileIdList,cloneSetList);
		HCTime = endTimer();
		System.out.print(HCLevelR+"\t");
		pw.print(HCLevelR+"\t");

		level++;
		cloneSetList = createCloneSetList(args[0]);
		fileToCloneMap = createFileToCloneMap(cloneSetList);
		startTimer();
		createPackedCloneList(fileIdList,fileToCloneMap,cloneSetList);
		openCloneSet(fileIdList,cloneSetList);
		SATime = endTimer();
		System.out.print(SALevelR+"\t");
		pw.print(SALevelR+"\t");

		level++;
		cloneSetList = createCloneSetList(args[0]);
		fileToCloneMap = createFileToCloneMap(cloneSetList);
		startTimer();
		createPackedCloneList(fileIdList,fileToCloneMap,cloneSetList);
		openCloneSet(fileIdList,cloneSetList);
		GATime = endTimer();
		System.out.print(GALevelR);
		System.out.print("\t");
		pw.print(GALevelR);
		pw.print("\t");
		//System.out.println("time(ms)");
		System.out.println(GreedyTime + "\t" + HCTime +"\t" + SATime + "\t" + GATime);
		pw.println(GreedyTime + "\t" + HCTime +"\t" + SATime + "\t" + GATime);
		pw.close();
	}

	public static BufferedReader getBufferedReader(String file){
		File input = new File(file);
		if(!input.exists()){
			System.out.println("there are no file. : " + file);
			System.exit(0);
		}
		try {
			return new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> createFileIdList(String pass){
		BufferedReader br = getBufferedReader(pass);
		ArrayList<String> fileIdList = new ArrayList<String>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			while(str!=null){
				if(str.equals("#begin{file description}")){
					canRead = true;
				}else if(str.equals("#end{file description}")){
					canRead = false;
				}else if(canRead){
					String[] str_split = str.split("[\t]+",0);
					fileIdList.add(str_split[3]);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileIdList;
	}

	public static ArrayList<CloneSet> createCloneSetList(String pass){
		BufferedReader br = getBufferedReader(pass);
		ArrayList<CloneSet> cloneSetList = new ArrayList<CloneSet>();
		try{
			String str = br.readLine();
			boolean canRead = false;
			CloneSet cs = null;
			while(str!=null){
				if(str.equals("#begin{set}")){
					canRead = true;
					cs = new CloneSet().setCloneSetId(cloneSetList.size());
				}else if(str.equals("#end{set}")){
					canRead = false;
					//cs.setRNR();
					cloneSetList.add(cs);
				}else if(canRead){
					String[] str_split = str.split("[.,\t]+",0);
					CodeClone c = new CodeClone()
					.setFileId(Integer.parseInt(str_split[1]))
					.setLine(Integer.parseInt(str_split[2]),Integer.parseInt(str_split[5]))
					.setColumn(Integer.parseInt(str_split[3]),Integer.parseInt(str_split[6]))
					.setToken(Integer.parseInt(str_split[4]),Integer.parseInt(str_split[7]))
					//.setLNR(Integer.parseInt(str_split[8]))
					.setParent(cs);
					cs.getCloneList().add(c);
				}
				str=br.readLine();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		isDispersive(cloneSetList);
		return cloneSetList;
	}

	public static void isDispersive(ArrayList<CloneSet> cloneSetList){
		for(CloneSet cs : cloneSetList){
			int fileId = cs.getCloneList().get(0).getFileId();
			for(CodeClone c : cs.getCloneList()){
				if(fileId!=c.getFileId()){
					cs.dispersive = true;
				}
			}
		}
	}

	public static HashMap<Integer,ArrayList<CodeClone>> createFileToCloneMap(ArrayList<CloneSet> cloneSetList){
		HashMap<Integer,ArrayList<CodeClone>> fileToCloneMap = new HashMap<Integer,ArrayList<CodeClone>>();
		for(CloneSet cs : cloneSetList){
			for(CodeClone c : cs.getCloneList()){
				if(!fileToCloneMap.containsKey(c.getFileId())){
					fileToCloneMap.put(c.getFileId(),new ArrayList<CodeClone>());
				}
				fileToCloneMap.get(c.getFileId()).add(c);
			}
		}
		return fileToCloneMap;
	}

	public static  void createPackedCloneList(ArrayList<String> fileList,HashMap<Integer,ArrayList<CodeClone>>cloneMap,ArrayList<CloneSet> cloneSetList){
		LOC =0;
		SLOC = 0;
		//FCLOC = 0;
		//FOLOC = 0;
		overlapSetMap.clear();
		//int fileNum = 0;
		for(int fileId = 0;fileId<fileList.size();fileId++){
			//fileNum++;
			//System.out.println(fileNum);
			HashMap<Integer,ArrayList<Tokens>> tokenMap = createTokenMap(fileList.get(fileId));
			if(tokenMap.size()>0){
				Object[] sorted_key = tokenMap.keySet().toArray();
				Arrays.sort(sorted_key);
				SLOC+=getSLOC(tokenMap.get(sorted_key[0]).get(0));
				if(cloneMap.containsKey(fileId)){

					for(CodeClone c : cloneMap.get(fileId)){
						//if(c.getParent().getRNR()>=RNR){
						ArrayList<Tokens> codeList = new ArrayList<Tokens>();
							Tokens currentToken = null;

							int comment_line = -1;
							boolean comment_out = false;
							for(Tokens t:tokenMap.get(c.getLS())){
								if(t.column==c.getCS()){
									currentToken = t;
									break;
								}
							}
							while(true){
								if(currentToken.isComment()){
									comment_line = currentToken.line;
								}else if(currentToken.isLComment()){
									comment_out = true;
								}else if(currentToken.isRComment()){
									comment_out = false;
								}else if(currentToken.line==comment_line||comment_out){

								}else if((currentToken.line<c.getLE()) || ((currentToken.line==c.getLE()) && (currentToken.column < c.getCE())) ){
									codeList.add(currentToken);
									currentToken.setClone();
								}else{
									break;
								}
								if(currentToken.getNextToken()!=null)
									currentToken = currentToken.getNextToken();
								else break;
							}
							//CloneSet parent,ArrayList<CloneSet> cloneSetList,int fileId,boolean dipersive
							if(CloneIsPackedByLevel(codeList, level,c.getParent(),cloneSetList,c.getFileId(),c.getParent().dispersive)){
								SourceCodeClone scc = (SourceCodeClone) new SourceCodeClone()
								.setTokensList(codeList)
								.setCloneSetId(c.getCloneSetId())
								.setFileId(fileId)
								.setParent(c.getParent())
								.setLine(codeList.get(0).line, codeList.get(codeList.size()-1).line)
								.setColumn(codeList.get(0).column, codeList.get(codeList.size()-1).column)
								.setToken(codeList.get(0).tokenId, codeList.get(codeList.size()-1).tokenId);
								scc.getParent().setSourceCodeClone(scc);
								//System.out.println(scc.getNumberOfLines());
							}
						//}
					}
				}
					CLOC += getCLOC(tokenMap.get(sorted_key[0]).get(0));
					OLOC += getOLOC(tokenMap.get(sorted_key[0]).get(0));
				/*
				else{
					FCLOC += getCLOC(tokenMap.get(sorted_key[0]).get(0));
					FOLOC += getOLOC(tokenMap.get(sorted_key[0]).get(0));
				}
				*/
			}
		}

	}

	public static HashMap<Integer,ArrayList<Tokens>> createTokenMap(String pass){
		HashMap<Integer,ArrayList<Tokens>> tokenMap = new HashMap<Integer,ArrayList<Tokens>>();
		BufferedReader br = getBufferedReader(pass);
		try{
			String str = br.readLine();
			int line = 1;
			Tokens preToken=null;
			int tokenId=0;
			int impId = 0;
			while(str!=null){
				LOC++;
				int column = 1;
				ArrayList<Tokens> tokenList = new ArrayList<Tokens>();
				String[] code_split = str.split("((?<=\\.)|(?=\\.))|((?<=\\s)|(?=\\s))|((?<=\\{)|(?=\\{))|((?<=\\})|(?=\\}))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\))|((?<=;)|(?=;))|((?<=,)|(?=,)))|((?<=//)|(?=//))|((?<=/\\*)|(?=/\\*))|((?<=\\*/)|(?=\\*/))|((?<=:)|(?=:))",0);
				for(int i=0;i<code_split.length;i++){
					if(code_split[i].trim().length()==0){
						column++;
					}else if(!code_split[i].isEmpty()){
						tokenList.add(new Tokens(code_split[i],line,column,tokenId++,impId++));
						if(code_split[i].equals(".")){
							impId = 0;
						}
						if(preToken!=null){
							preToken.setNextToken(tokenList.get(tokenList.size()-1));
						}
						preToken = tokenList.get(tokenList.size()-1);
						column += code_split[i].length();
					}
				}
				if(tokenList.size()>0){
					tokenMap.put(line, tokenList);
				}
				str = br.readLine();
				line ++;
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return tokenMap;
	}

	public static void hasParagraph(ArrayList<Tokens> codeList){
		for(int i=0;i<codeList.size()-1;i++){
			if(!Arrays.asList(reservedWords).contains(codeList.get(i).token)&&codeList.get(i+1).token.equals(".")){
				if(codeList.get(i).getImpId()==0){
					codeList.get(i).paragraph = true;
					codeList.get(i+1).paragraph = true;
					//codeList.get(i+2).paragraph = true;
				}
			}
		}
		for(int i=0;i<codeList.size()-2;i++){
			if(!Arrays.asList(reservedWords).contains(codeList.get(i).token)&&codeList.get(i+1).token.matches("(?i)section")&&codeList.get(i+2).token.equals(".")){
				if(codeList.get(i).getImpId()==0){
					codeList.get(i).paragraph = true;
					codeList.get(i+1).paragraph = true;
				}
			}
		}
	}

	public static boolean CloneIsPackedByLevel(ArrayList<Tokens> tokenList,int level,CloneSet parent,ArrayList<CloneSet> cloneSetList,int fileId,boolean dispersive){
		if(level>=Greedy)
			return hasSentence(tokenList,parent,cloneSetList,fileId,dispersive);
		if(level==functionLevel){
			return hasFunction(tokenList,dispersive);
		}
		return false;
	}

	public static int acrossTheParagraph(int left,ArrayList<Tokens> codeList){
		if(codeList.size()-left>1){
			boolean status = codeList.get(left).paragraph;
			boolean nextStatus = codeList.get(left+1).paragraph;
			if(!status&&nextStatus){
				return left;
			}
			for(int i=left+1;i<codeList.size()-1;i++){
				status = codeList.get(i).paragraph;
				nextStatus = codeList.get(i+1).paragraph;
				if(!status&&nextStatus){
					return i;
				}
			}
		}
		return -1;
	}

	public static boolean leapToTheParagraph(ArrayList<Tokens> codeList){
		if(codeList.size()>0){
			for(Tokens token : codeList){
				if(token.token.matches("(?i)perform")||token.token.matches("(?i)call")){
					return false;
				}
			}
		}
		return false;
	}

	public static boolean hasSentence(ArrayList<Tokens> codeList,CloneSet parent,ArrayList<CloneSet> cloneSetList,int fileId,boolean dipersive){
		hasParagraph(codeList);
		boolean findSplitClone = false;
		int left = 0;
		int right = acrossTheParagraph(left, codeList);
		int loop = 0;
		while(right!=-1){
			findSplitClone = true;
			ArrayList<Tokens> splitCodeList = new ArrayList<Tokens>();
			for(int i=0;i<=right-left;i++){
				splitCodeList.add(codeList.get(left+i));
			}
			if(hasFunction(splitCodeList,dipersive)){
				CloneSet cs;
				if(parent.splitCloneIdList.size()<=loop){
					cs = new CloneSet();
					cs.setCloneSetId(cloneSetList.size());
					parent.splitCloneIdList.add(cs.getCloneSetId());
					cloneSetList.add(cs);
				}else{
					cs = cloneSetList.get(parent.splitCloneIdList.get(loop));
				}
				CodeClone c = new CodeClone()
				.setCloneSetId(cs.getCloneSetId())
				.setFileId(fileId)
				.setParent(cs)
				.setLine(codeList.get(left).line, codeList.get(right).line)
				.setColumn(codeList.get(left).column, codeList.get(right).column)
				.setToken(codeList.get(left).tokenId, codeList.get(right).tokenId);
				c.getParent().getCloneList().add(c);
				SourceCodeClone scc = (SourceCodeClone) new SourceCodeClone()
				.setTokensList(splitCodeList)
				.setCloneSetId(cs.getCloneSetId())
				.setFileId(fileId)
				.setParent(cs)
				.setLine(codeList.get(left).line, codeList.get(right).line)
				.setColumn(codeList.get(left).column, codeList.get(right).column)
				.setToken(codeList.get(left).tokenId, codeList.get(right).tokenId);
				scc.getParent().setSourceCodeClone(scc);

				loop++;
			}
			left = right + 1;
			right = acrossTheParagraph(left, codeList);
		}
		if(findSplitClone){
			//System.out.println("find");
			right = codeList.size()-1;
			//���܂�̕������R�[�h�N���[���ł���D
			ArrayList<Tokens> splitCodeList = new ArrayList<Tokens>();
			for(int i=0;i<=right-left;i++){
				splitCodeList.add(codeList.get(left+i));
			}
			if(hasFunction(splitCodeList,dipersive)){
				findSplitClone = true;
				CloneSet cs;
				if(parent.splitCloneIdList.size()<=loop){
					cs = new CloneSet();
					cs.setCloneSetId(cloneSetList.size());
					parent.splitCloneIdList.add(cs.getCloneSetId());
					cloneSetList.add(cs);
				}else{
					cs = cloneSetList.get(parent.splitCloneIdList.get(loop));
				}
				CodeClone c = new CodeClone()
				.setCloneSetId(cs.getCloneSetId())
				.setFileId(fileId)
				.setParent(cs)
				.setLine(codeList.get(left).line, codeList.get(right).line)
				.setColumn(codeList.get(left).column, codeList.get(right).column)
				.setToken(codeList.get(left).tokenId, codeList.get(right).tokenId);
				c.getParent().getCloneList().add(c);
				SourceCodeClone scc = (SourceCodeClone) new SourceCodeClone()
				.setTokensList(splitCodeList)
				.setCloneSetId(cs.getCloneSetId())
				.setFileId(fileId)
				.setParent(cs)
				.setLine(codeList.get(left).line, codeList.get(right).line)
				.setColumn(codeList.get(left).column, codeList.get(right).column)
				.setToken(codeList.get(left).tokenId, codeList.get(right).tokenId);
				scc.getParent().setSourceCodeClone(scc);

				loop++;
			}
			return false;
		}else{
			if(acrossTheParagraph(0,codeList)<0){
				if(!dipersive || (dipersive&&!leapToTheParagraph(codeList))){
					return true;
				}
			}
			return false;
		}
	}

	public static boolean hasFunction(ArrayList<Tokens> codeList,boolean dispersive){
		hasParagraph(codeList);
		if(acrossTheParagraph(0,codeList)<0){
			if(!dispersive || (dispersive&&!leapToTheParagraph(codeList))){
				return true;
			}
		}

		return false;
	}

	public static int getSLOC(Tokens firstToken){
		int SLOC =0;
		Tokens CurrentToken = firstToken;
		int currentLine = -1;
		boolean comment_out = false;
		while(true){
			if(CurrentToken.isComment()){
				currentLine = CurrentToken.line;
			}else if(CurrentToken.isLComment()){
				comment_out = true;
			}else if(CurrentToken.isRComment()){
				comment_out = false;
			}else if(CurrentToken.line==currentLine||comment_out){

			}else{
				currentLine = CurrentToken.line;
				SLOC++;
			}
			if(CurrentToken.getNextToken()!=null)
				CurrentToken = CurrentToken.getNextToken();
			else break;

		}
		return SLOC;
	}

	public static int getCLOC(Tokens firstToken){
		int CLOC = 0;
		Tokens CurrentToken = firstToken;
		int currentLine = -1;
		while(true){
			if(currentLine!=CurrentToken.line&&CurrentToken.isClone()){
				currentLine = CurrentToken.line;
				CLOC++;
			}
			if(CurrentToken.getNextToken()!=null)
				CurrentToken = CurrentToken.getNextToken();
			else break;
		}
		return CLOC;
	}

	public static int getOLOC(Tokens firstToken){
		int OLOC = 0;
		Tokens CurrentToken = firstToken;
		int currentLine = -1;
		while(true){
			if(currentLine!=CurrentToken.line&&CurrentToken.isClone()){
				currentLine = CurrentToken.line;
				if(CurrentToken.getCount()>2) OLOC++;
			}
			if(CurrentToken.getNextToken()!=null)
				CurrentToken = CurrentToken.getNextToken();
			else break;
		}
		return OLOC;
	}

	public static void openCloneSet(ArrayList<String>fileIdList,ArrayList<CloneSet> cloneSetList){
		switch(level){
		case Greedy:
			GreedyLevelR = calcSourceLevelReducGreedy(cloneSetList);
			convert("c_greedy.txt",fileIdList,cloneSetList);
			break;
		case functionLevel:
			//FunctionLevelR =  calcFunctionLevelReduc(cloneSetList);
			FunctionLevelR = calcSourceLevelReducGreedy(cloneSetList);
			convert("c_function.txt",fileIdList,cloneSetList);
			break;
		case HC:
			extractNeighborCloneSet(cloneSetList);
			for(CloneSet cs:cloneSetList){
				cs.calcReduc();
				if(cs.getReduc()<0){
					cs.getSCloneList().clear();
				}
			}
			HillClimbing();
			HCLevelR = calcReduc(cloneSetList);
			convert("c_hc.txt",fileIdList,cloneSetList);
			clearSCCList(cloneSetList);
			break;
		case SA:
			extractNeighborCloneSet(cloneSetList);
			for(CloneSet cs:cloneSetList){
				cs.calcReduc();
				if(cs.getReduc()<0){
					cs.getSCloneList().clear();
				}
			}
			SimulatedAnnealing(100, (float)0.5);
			SALevelR = calcReduc(cloneSetList);
			convert("c_sa.txt",fileIdList,cloneSetList);
			clearSCCList(cloneSetList);
			break;
		case GA:
			extractNeighborCloneSet(cloneSetList);
			for(CloneSet cs:cloneSetList){
				cs.calcReduc();
				if(cs.getReduc()<0){
					cs.getSCloneList().clear();
				}
			}
			GeneticAlgorithmMain(100);
			GALevelR = calcReduc(cloneSetList);
			convert("c_ga.txt",fileIdList,cloneSetList);
			clearSCCList(cloneSetList);
			break;
		}
	}

	public static int calcFunctionLevelReduc(ArrayList<CloneSet> cloneSetList){
		int Reduc = 0;
		for(CloneSet cs : cloneSetList){
			int sum = 0;
			for(SourceCodeClone scs:cs.getSCloneList()){
				//System.out.println(scs.getNumberOfLines());
				sum+=scs.getNumberOfLines();
			}
			int num = cs.getSCloneList().size();
			if(num>1){
				Reduc+=(num-1)*(sum/num)-num;

			}
		}
		return Reduc;
	}

	public static int calcSourceLevelReducGreedy(ArrayList<CloneSet> cloneSetList){
		extractNeighborCloneSet(cloneSetList);
		int Reduc=0;
		for(CloneSet cs:cloneSetList){
			cs.calcReduc();
		}
		Collections.sort(cloneSetList,new TokenSizeComparator());
		for(int i=0;i<cloneSetList.size();i++){
			CloneSet cs = cloneSetList.get(i);
			if(!cs.contains(cs)&&!cs.isPainted()&&cs.getReduc()>0){
				cs.setPainted();
				cs.allPacked();
				Reduc+=cs.getReduc();
				for(CloneSet cs2:cs.getNeighborCloneSet()){
					cs2.setPainted();
				}
			}
		}
		return Reduc;
	}

	public static void extractNeighborCloneSet(ArrayList<CloneSet> cloneSetList){
		HashMap<Integer,ArrayList<SourceCodeClone>> fileToSCloneMap = createFileToSCloneMap(cloneSetList);
		identifyOverlap(cloneSetList);
		for(int fileId:fileToSCloneMap.keySet()){
			ArrayList<SourceCodeClone> sccList = fileToSCloneMap.get(fileId);
			for(int i=0;i<sccList.size()-1;i++){
				SourceCodeClone scc1 = sccList.get(i);
				for(int j=i+1;j<sccList.size();j++){
					SourceCodeClone scc2 = sccList.get(j);
					if(isOverlap(scc1,scc2)){
						scc1.getNeighbors().add(scc2);
						scc2.getNeighbors().add(scc1);
						if(!scc1.getParent().contains(scc2.getParent())&&!scc2.getParent().contains(scc1.getParent())){
							scc1.getParent().getNeighborCloneSet().add(scc2.getParent());
							scc2.getParent().getNeighborCloneSet().add(scc1.getParent());
							registerOs(scc1.getParent(),scc2.getParent());
						}
					}
				}
			}
		}
	}

	public static HashMap<Integer,ArrayList<SourceCodeClone>> createFileToSCloneMap(ArrayList<CloneSet>cloneSetList){
		HashMap<Integer,ArrayList<SourceCodeClone>> fileToSCloneMap = new HashMap<Integer,ArrayList<SourceCodeClone>>();
		for(CloneSet cs:cloneSetList){
			for(SourceCodeClone scc:cs.getSCloneList()){
				if(!fileToSCloneMap.containsKey(scc.getFileId())){
					fileToSCloneMap.put(scc.getFileId(),new ArrayList<SourceCodeClone>());
				}
				fileToSCloneMap.get(scc.getFileId()).add(scc);
			}
		}
		return fileToSCloneMap;
	}

	public static boolean isOverlap(SourceCodeClone scc1,SourceCodeClone scc2){
		if((scc1.getTE()-scc2.getTS())*(scc2.getTE()-scc1.getTS())>=0){
			return true;
		}else{
			return false;
		}
	}

	public static void clearSCCList(ArrayList<CloneSet> cloneSetList){
		for(CloneSet cs :cloneSetList){
			if(cs.getSCloneList().size()>0){
				for(SourceCodeClone scc : cs.getSCloneList()){
					scc.getNeighbors().clear();
				}
				cs.getNeighborCloneSet().clear();
				cs.getSCloneList().clear();
				cs.open();
				cs.osIndex = -1;
				cs.overlapSetID = -1;
				osId = 0;
			}
		}
	}

	public static int countNumClone(ArrayList<CloneSet> cloneSetList){
		int sum = 0;
		for(CloneSet cs :cloneSetList){
			if(cs.getSCloneList().size()>0){
				sum += cs.getSCloneList().size();
			}
		}
		return sum;
	}

	public static int countNumCloneSet(ArrayList<CloneSet> cloneSetList){
		int sum = 0;
		for(CloneSet cs :cloneSetList){
			if(cs.getSCloneList().size()>0){
				sum ++;
			}
		}
		return sum;
	}

	private static int osId=0;
	public static HashMap<Integer, OverlapSet> overlapSetMap=new HashMap<Integer,OverlapSet>();

	private static void registerOs(CloneSet cs1,CloneSet cs2){
		if(cs1.getCloneSetId()!=cs2.getCloneSetId()){
			if(cs1.overlapSetID==-1&&cs2.overlapSetID==-1){
				OverlapSet os = new OverlapSet(osId);
				os.getCloneSet(cs1);
				os.getCloneSet(cs2);
				overlapSetMap.put(osId++, os);
			}else if(cs1.overlapSetID==-1){
				overlapSetMap.get(cs2.overlapSetID).getCloneSet(cs1);
			}else if(cs2.overlapSetID==-1){
				overlapSetMap.get(cs1.overlapSetID).getCloneSet(cs2);
			}else if(cs1.overlapSetID!=cs2.overlapSetID){
				OverlapSet os1 = overlapSetMap.get(cs1.overlapSetID);
				OverlapSet os2 = overlapSetMap.get(cs2.overlapSetID);
				int removeKey = cs2.overlapSetID;
				for(CloneSet cs : os2.setList){
					cs.overlapSetID=os1.overlapSetID;
				}
				os1.setList.addAll(os2.setList);
				overlapSetMap.remove(removeKey);
			}
		}
	}

	public static void HillClimbing(){
		/*
		for(HashMap.Entry<Integer, OverlapSet> e : overlapSetMap.entrySet()) {
			OverlapSet os = e.getValue();
			for(int i=0;i<os.setList.size();){
				CloneSet cs1 = os.setList.get(i);
				boolean find = false;
				for(int j=0;j<cs1.getNeighborCloneSet().size();j++){
					CloneSet cs2 = cs1.getNeighborCloneSet().get(j);
					if(cs1.getCloneSetId() == cs2.getCloneSetId()){
						find = true;
						cs1.getNeighborCloneSet().remove(j);
						break;
					}
				//os.setList.remove(j);
				}
				if(find){
					for(int k=0;k<cs1.getNeighborCloneSet().size();k++){
						CloneSet cs3 = cs1.getNeighborCloneSet().get(k);
						for(int j=0;j<cs3.getNeighborCloneSet().size();){
							if(cs1.getCloneSetId() == cs3.getNeighborCloneSet().get(j).getCloneSetId()){
								cs3.getNeighborCloneSet().remove(j);
							}else{
								j++;
							}
						}
					}
					os.setList.remove(i);

				}else{
					i++;
				}
			}
		}
		*/
		/*************************/
		for(HashMap.Entry<Integer, OverlapSet> e : overlapSetMap.entrySet()) {
			OverlapSet os = e.getValue();
			os.indexReset();
			Collections.sort(os.setList,new TokenSizeComparator());
			int n = os.setList.size();
			os.bestMatrix = new int[n][n];
			os.bestVector = new int[n];
			initialized(os.bestMatrix,os.bestVector,os);
			int bestEval = evalVec(os.bestVector,os);
			climbing(os);
			for(int i=0;i<n;i++){
				if(os.bestVector[i]==1){
					os.setList.get(i).allPacked();
				}
			}
		}
	}

	public static int calcReduc(ArrayList<CloneSet> cloneSetList){
		int Reduc = 0;
		for(CloneSet cs : cloneSetList){
			if(cs.isPacked()){
				Reduc += cs.getReduc();
				//cs.open();
			}else if(cs.overlapSetID==-1&&cs.getSCloneList().size()>0){
				Reduc += cs.getReduc();
			}
		}
		return Reduc;
	}

	public static void climbing(OverlapSet os){
		int n = os.setList.size();
		while(true){
			int[][] nextMatrix = new int[n][n];
			int[][] currentMatrix =	os.bestMatrix.clone();
			int nextEval = -1;
			for(int k=0;k<n;k++){
				ArrayList<int[][]> neighborList = neighbors(currentMatrix,k);
				for(int[][] xM : neighborList){
				//int[][] xM = neighbors(currentMatrix,k);
					int[] xV = new int[n];
					for(int i=0;i<n;i++){
						xV[i] = xM[i][i];
					}
					int eval = evalVec(xV,os);
					if(nextEval<eval){
						nextEval = eval;
						for(int j=0;j<n;j++){
							System.arraycopy(xM[j], 0, nextMatrix[j], 0, n);
							//nextMatrix = xM;
						}
					}
				}
			}
			if(os.bestEval>=nextEval){
				return;
			}else{
				os.bestEval = nextEval;
				for(int j=0;j<n;j++){
					System.arraycopy(nextMatrix[j], 0, os.bestMatrix[j], 0, n);
				}
				//os.bestMatrix=nextMatrix.clone();
				for(int i=0;i<n;i++){
					os.bestVector[i]=os.bestMatrix[i][i];
				}
			}
		}
	}

	private static int MAX_ITER=2000;
	private static float ALPHA = (float)0.5;

	public static void SimulatedAnnealing(int iter,float alpha){
		//System.out.println("start simulated annealing");

		if(iter>0){
			MAX_ITER=iter;
		}
		if(alpha<=1.0&&alpha>0){
			ALPHA = alpha;
		}
		for(HashMap.Entry<Integer, OverlapSet> e : overlapSetMap.entrySet()) {
			OverlapSet os = e.getValue();
			/*���g�Əd������N���[���Z�b�g��r������*/
			/*
			for(int i=0;i<os.setList.size();){
				CloneSet cs1 = os.setList.get(i);
				boolean find = false;
				for(int j=0;j<cs1.getNeighborCloneSet().size();j++){
					CloneSet cs2 = cs1.getNeighborCloneSet().get(j);
					if(cs1.getCloneSetId() == cs2.getCloneSetId()){
						find = true;
						cs1.getNeighborCloneSet().remove(j);
						break;
					}
				}
				if(find){
					for(int k=0;k<cs1.getNeighborCloneSet().size();k++){
						CloneSet cs3 = cs1.getNeighborCloneSet().get(k);
					//for(CloneSet cs3 : cs1.getNeighborCloneSet()){
						for(int j=0;j<cs3.getNeighborCloneSet().size();){
							if(cs1.getCloneSetId() == cs3.getNeighborCloneSet().get(j).getCloneSetId()){
								cs3.getNeighborCloneSet().remove(j);
							}else{
								j++;
							}
						}
					}
					os.setList.remove(i);
				}else{
					i++;
				}
			}
			*/
			/*************************/
			Collections.sort(os.setList,new TokenSizeComparator());
			int n = os.setList.size();
			os.bestMatrix = new int[n][n];
			os.bestVector = new int[n];
			initialized(os.bestMatrix,os.bestVector,os);
			os.bestEval = evalVec(os.bestVector,os);
			annealing(os);
			for(int i=0;i<n;i++){
				if(os.bestVector[i]==1){
					os.setList.get(i).allPacked();
				}
			}
		}
	}

	private static void annealing(OverlapSet os){
		int n = os.setList.size();
		if(n<2) return;
		int[][] matrix = new int[n][n];//os.bestMatrix.clone();
		for(int j=0;j<n;j++){
			System.arraycopy(os.bestMatrix[j], 0, matrix[j], 0, n);
			//nextMatrix = xM;
		}
		int[] vector = os.bestVector.clone();
		int e = os.bestEval;
		for(int iter=0;iter<MAX_ITER;iter++){
			int ran1 = (int) (Math.random()*(n-1));
			ArrayList<int[][]> neighborList = neighbors(matrix,ran1);
			int ran2 = (int) (Math.random()*(neighborList.size()-1));
			int[][] nextMatrix = neighborList.get(ran2);
			int[] nextVec = new int[n];
			for(int i=0;i<n;i++){
				nextVec[i] = nextMatrix[i][i];
			}
			int nextEval = evalVec(nextVec,os);
			if(nextEval>os.bestEval){
				//os.bestMatrix = nextMatrix;
				for(int j=0;j<n;j++){
					System.arraycopy(nextMatrix[j], 0, os.bestMatrix[j], 0, n);
				}
				os.bestVector = nextVec;
				os.bestEval = nextEval;
				/*GOAL_E�Ƃ̔�r���L�q����Ȃ炱��*/
			}
			if(Math.random()<=probability(e,nextEval,temperature(iter/MAX_ITER))){
				//matrix = nextMatrix.clone();
				for(int j=0;j<n;j++){
					System.arraycopy(nextMatrix[j], 0, matrix[j], 0, n);
					//nextMatrix = xM;
				}
				vector = nextVec.clone();
				e = nextEval;
			}
		}
	}

	private static float probability(int e1,int e2,float t){
		if(e1>=e2){
			return 1;
		}else{
			return (float) Math.pow(Math.E,(e1-e2)/t);
		}
	}

	private static float temperature(float r){
		return (float) Math.pow(ALPHA,r);
	}

	public static void GeneticAlgorithmMain(int Iter){
		InputStream input = GeneticAlgorithm.class.getResourceAsStream(
				"interMediate");
		if(input == null){
			System.err.println("Unable input stream");
			System.exit(-1);
		}
		for(HashMap.Entry<Integer, OverlapSet> e : overlapSetMap.entrySet()) {
			/*
			OverlapSet os = e.getValue();
			for(int i=0;i<os.setList.size();){
				CloneSet cs1 = os.setList.get(i);
				boolean find = false;
				for(int j=0;j<cs1.getNeighborCloneSet().size();j++){
					CloneSet cs2 = cs1.getNeighborCloneSet().get(j);
					if(cs1.getCloneSetId() == cs2.getCloneSetId()){
						find = true;
						cs1.getNeighborCloneSet().remove(j);
						break;
					}
				}
				if(find){
					for(int k=0;k<cs1.getNeighborCloneSet().size();k++){
						CloneSet cs3 = cs1.getNeighborCloneSet().get(k);
					//for(CloneSet cs3 : cs1.getNeighborCloneSet()){
						for(int j=0;j<cs3.getNeighborCloneSet().size();){
							if(cs1.getCloneSetId() == cs3.getNeighborCloneSet().get(j).getCloneSetId()){
								cs3.getNeighborCloneSet().remove(j);
							}else{
								j++;
							}
						}
					}
					os.setList.remove(i);
				}else{
					i++;
				}
			}
			*/
			//e.getValue().indexReset();
			iterateIndex = e.getKey();
			new Executor()
			.withProblemClass(GeneticAlgorithm.class,input)
			.withAlgorithm("NSGAII")
			.withMaxEvaluations(Iter)
			.withProperty("populationSize",300)
			.withProperty("sbx.rate", 1.0)
			.withProperty("pm.rate", 1.0)
			//.distributeOnAllCores()
			.run();
		}
	}

	static void startTimer(){
		start = System.currentTimeMillis();

	}

	static long endTimer(){
		long end = System.currentTimeMillis();
		return end - start;
	}

	static void identifyOverlap(ArrayList<CloneSet> cloneSetList){
		for(CloneSet cs : cloneSetList){
			boolean find = false;
			for(int i=0;i<cs.getSCloneList().size()-1;i++){
				for(int j=i+1;j<cs.getSCloneList().size();j++){
					SourceCodeClone scc1 = cs.getSCloneList().get(i);
					SourceCodeClone scc2 = cs.getSCloneList().get(j);
					if(scc1.getFileId()==scc2.getFileId()&&isOverlap(scc1,scc2)){
						find = true;
					}
				}
			}
			if(find){
				cs.getSCloneList().clear();
			}
		}
	}

	public static void convert(String file,ArrayList<String> fileList,ArrayList<CloneSet> cloneSetList){
		PrintWriter pw = getPrintWriter(new File(file));
		pw.println("#begin{file description}");
			for(int i=0;i<fileList.size();i++){
				pw.println("0."+(i+1)+"\t0\t0\t"+fileList.get(i));
			}
		pw.println("#end{file description}");
		pw.println("#begin{clone}");
		for(CloneSet cs : cloneSetList){
			if(cs.isPacked()&&cs.getReduc()>0){
				pw.println("#begin{set}");
				for(SourceCodeClone c:cs.getSCloneList()){
					/*fileId LS,CS,TS LE,CE,TE, LNR*/
					pw.println("0."+(c.getFileId())+"\t"+c.getLS()+","+c.getCS()+","+c.getTS()
							+"\t"+c.getLE()+","+c.getCE()+","+c.getTE()+"\t0");
				}
				pw.println("#end{set}");
			}
		}
		pw.println("#end{clone}");
		pw.close();
	}

	public static PrintWriter getPrintWriter(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	}

}

