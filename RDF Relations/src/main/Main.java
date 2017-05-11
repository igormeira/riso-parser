package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	static List<String> lista;

	public static void main(String[] args) throws IOException {
		File dir = new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "files" + File.separator + "civil-war.txt");
		File finalFile = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "files" + File.separator + "relations.txt");
		
		System.out.println(fin.toString());
		String document = readFile(fin);
		writeFile(finalFile, document);
		System.out.println("DONE!");
	}
	
	private static void writeFile(File finalFile, String document) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(finalFile, "UTF-8");
		writer.println(document);
		writer.close();		
	}
	
	private static String readFile(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		lista = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.contains("<rel:") && !line.contains("hasText")) {
				String strAux = line.substring(line.indexOf(":")+1, line.indexOf("rdf")-1);
				
				if (!lista.contains(strAux)) {
					lista.add(strAux);
				}
			} 
		}
		br.close();
		return finalText();
	}
	
		public static String finalText() {
			String finalStr = "";	
			
			for (String str : lista) {
				finalStr += "<" + str + ">" + System.lineSeparator();						
			}
			finalStr += "<hasText>";
			return finalStr;		
		}	
		
}
