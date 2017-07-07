package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Merge {
	
	private static String finalText;
	
	public static void genMergedFile(File[] files) throws IOException {
		
		File dir = new File(".");
		File merged = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "res" + File.separator + "merged" + File.separator + "MergedFile.txt");
		
		if(!merged.exists()){
			merged.createNewFile();
		}
		
		for (File file : files) {
		    if (file.isFile()) {
				finalText = "";
				
				File original = new File(dir.getCanonicalPath() + File.separator + 
						"src" + File.separator + "res" + File.separator + "files" + File.separator + file.getName());
				
				System.out.println("MERGE:");
				readFile(original);
				writeFile(merged, finalText);
				
				System.out.println("DONE!" + System.lineSeparator() + System.lineSeparator());
		    }
		}
	}

	private static void writeFile(File finalFile, String document) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(finalFile, "UTF-8");
		writer.println(document);
		writer.close();		
	}
	
	private static void readFile(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			finalText += line + System.lineSeparator();
		}
		br.close();
	}
}
