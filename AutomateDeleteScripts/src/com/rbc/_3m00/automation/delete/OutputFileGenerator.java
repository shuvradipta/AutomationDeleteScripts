package com.rbc._3m00.automation.delete;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class OutputFileGenerator {
	
	private String CONTENT_NODE_START_TAG = "<content-node action=\"delete\" uniquename=\"@\">";
	
	private String CONTENT_NODE_END_TAG = "</content-node>";
	
	private int MAXIMUM_RECORD_COUNT = 10;
	
	private static String REPLACEMENT_CONTENT = "<!--Generated_Content-->";
	
	private Stack<String> portalPageUniqueNames = null;
	
	private ArrayList<String> output = null;
	
	public OutputFileGenerator(Stack<String> pageUniqueNames){
		this.portalPageUniqueNames = pageUniqueNames;
	}
	
	public ArrayList<String> getContent(){
		return this.output;
	}
	
	public boolean generateOutput() throws IOException{
		if(this.portalPageUniqueNames == null || this.portalPageUniqueNames.empty())
			return false;
		else{
			int fileCount = 1;
			File templateFile = new File("Delete-Pages-template.xml");
			while(!this.portalPageUniqueNames.empty()){
				StringBuffer sb = new StringBuffer();
				File outputFile = new File("Delete-Pages_"+fileCount+".xml");
				fileCount++;
				if(!outputFile.exists()){
					if(!outputFile.createNewFile())
						System.out.println(outputFile.getName() + " could not be created");
				}
				for (int i = 0; i < MAXIMUM_RECORD_COUNT && !this.portalPageUniqueNames.empty(); i++) {
					sb.append(CONTENT_NODE_START_TAG.replace("@", this.portalPageUniqueNames.pop()));
					sb.append("\n" + CONTENT_NODE_END_TAG+"\n");
				}
				BufferedReader reader = new BufferedReader(new FileReader(templateFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				String content = "";
				while( (content = reader.readLine()) != null){
					if(content.contains(REPLACEMENT_CONTENT)){
						content = content.replace(REPLACEMENT_CONTENT, sb.toString());
					}
					writer.write(content+"\n");
				}
				reader.close();
				writer.flush();
				writer.close();
				sb = null;
			}
			return true;
		}
	}

}
