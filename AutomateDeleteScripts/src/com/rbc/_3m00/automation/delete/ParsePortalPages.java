package com.rbc._3m00.automation.delete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Pattern;

public class ParsePortalPages {
	private static final String XML_END_TAG = "/>";
	public static Stack<String> uniqueNames = new Stack<String>();
	
	public static void main(String[] args) throws IOException {
		if(args == null || args[0] == null || args[0].trim().isEmpty()){
			System.out.println("Invalid arguments");
			return;
		}
		String parentPath = args[0];
				
		File parentFile = new File(parentPath);
		parseFiles(parentFile.listFiles());
		OutputFileGenerator generator = new OutputFileGenerator(uniqueNames);
		if(generator.generateOutput()){
			System.out.println("Output files creation successful.....");
		}else{
			System.out.println("There was some error generating output....");
		}
	}
	
	public static void parseFiles(File[] files){
		for (int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()){
				parseFiles(files[i].listFiles());
			}else{
				try {
					BufferedReader reader = new BufferedReader(new FileReader(files[i]));
					String readLine = "";
					while((readLine = reader.readLine()) != null){
						if(readLine.contains("resourceref")){
							readLine = readLine.substring(readLine.indexOf("resourceref"),readLine.indexOf(XML_END_TAG));
							readLine = readLine.replaceAll(Pattern.quote("\""), "");
							String currentPageUniqueName = readLine.replace("resourceref=", "");
							if(uniqueNames.search(currentPageUniqueName) == -1){
								uniqueNames.push(currentPageUniqueName);
							}
						}
					}
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
