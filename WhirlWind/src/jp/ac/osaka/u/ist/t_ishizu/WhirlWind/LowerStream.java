package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import static jp.ac.osaka.u.ist.t_ishizu.WhirlWind.WhirlWind.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LowerStream {
	public static String outputFile = "ww.txt";

	public static PrintWriter getPrintWriterWithOverwrite(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,false)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static PrintWriter getPrintWriterWithOverwrite(String output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(new File(output),false)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static PrintWriter getPrintWriterWithoutOverwrite(File output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(output,true)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static PrintWriter getPrintWriterWithoutOverwrite(String output){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(new File(output),true)));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static void resultMessage(String output){
		PrintWriter pw = getPrintWriterWithOverwrite(output);
		for(CloneSet cloneSet:cloneSetList){
			if(cloneSet.isSlimming()){
				pw.println("CID\t:\t"+cloneSet.getId());
				pw.println("CHUNKS\t:\t1");
				pw.println("SIZE\t:\t"+cloneSet.getSize());
				for(int i=0;i<cloneSet.getSize();i++){
					pw.println("["+cloneSet.getId()+"]");
				}
				pw.println();
			}
		}
		pw.close();
		System.out.println("@LowerStream.resultMessage() c.txt");
	}

	public static void updateSeedFile(String output){
		PrintWriter pw = getPrintWriterWithOverwrite(output);
		pw.println("version: ccfx 10.2.7");
		pw.println("format: pair_diploid");
		pw.println("option: -b 50");
		pw.println("option: -s 2");
		pw.println("option: -u +");
		pw.println("option: -t 12");
		pw.println("option: -w f+g+w+");
		pw.println("option: -j +");
		pw.println("option: -k 60m");
		pw.println("option: -preprocessed_file_postfix\t"+CCFXDFileArray[0]);
		pw.println("option: -pp +");
		pw.println("option: -n\t"+CCFXDFileArray[1]);
		pw.println("preprocess_script: cobol");
		pw.println("source_files {");
		for(int fileId=1;fileId<=fileList.size();fileId++){
			pw.println(fileId + "\t" + fileList.get(fileId-1) + "\t" + tokenMap.get(fileId).size());
		}
		pw.println("}");
		pw.println("source_file_remarks {");
		pw.println("}");
		pw.println("clone_pairs {");
		for(ArrayList<Seed> seedList:SeedMap.values()){
			for(Seed seed1:seedList){
				if(seed1.getSprout().getValid()){
					if(seed1.getCloneSet().isSlimming()){
						for(Seed seed2:seed1.getCloneSet().getSeedList()){
							if(!isSameSeed(seed1,seed2)){
								pw.println(seed1.getCloneSet().getId()+"\t"+
								seed1.getFileId()+"."+seed1.getTS()+"-"+seed1.getTE()+"\t"+
								seed2.getFileId()+"."+seed2.getTS()+"-"+seed2.getTE());
							}
						}
					}
				}
			}
		}
		pw.println("}");
		pw.println("clone_set_remarks {");
		pw.println("}");
		pw.close();
		System.out.println("@LowerStream.updateSeedFile() d.txt");
	}

	public static boolean isSameSeed(Seed seed1,Seed seed2){
		if(seed1.getTS()==seed2.getTS()&&seed1.getTE()==seed2.getTE()){
			if(seed1.getFileId()==seed2.getFileId()){
				return true;
			}
		}
		return false;
	}
}
