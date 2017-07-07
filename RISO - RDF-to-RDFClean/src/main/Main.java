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

import Util.Json;
import Util.Merge;
import Util.Term;

public class Main {
	
	private static List<Term> lista;
	private static String finalText;
	private static Json json; 

	public static void main(String[] args) throws IOException {
		File dir = new File(".");
		File[] files = new File(dir.getCanonicalPath() + File.separator + "src"+ File.separator + "res" 
								+ File.separator + "files").listFiles();
		
		for (File file : files) {
		    if (file.isFile()) {
				json = new Json();
				
				finalText = "";
				
				File original = new File(dir.getCanonicalPath() + File.separator + 
						"src" + File.separator + "res" + File.separator + "files" + File.separator + file.getName());
				File finalFile = new File(dir.getCanonicalPath() + File.separator + 
						"src" + File.separator + "res" + File.separator + "cleaned" + File.separator + file.getName());
				
				if(!finalFile.exists()){
					finalFile.createNewFile();
				}
				
				Merge.genMergedFile(files);
				
				System.out.println("CLEAN:");
				System.out.println(file.getName());
				readFile(original);
				writeFile(finalFile, finalText);
				
				System.out.println("JSON:");
				json.getJson(original, finalFile.getName());
				
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
	 
		lista = new ArrayList<Term>();
		String line = null;
		Term term = new Term();
		while ((line = br.readLine()) != null) {
			if (line.contains("<rdf:Description")) {
				term = new Term();
				term.setType(getType(line));
				term.setDescription(getDescription(line));
				finalRDF(term);
			
			} else if (line.contains("<rel:") && (!line.contains("<rel:hasText>"))) {
				
				String relation = addRelation(line);
				String element = addElement(line);
				finalRDF(relation, element);
			
			} else if (line.contains("</rdf:Description>")) {
				finalText += "/>" + System.lineSeparator() + System.lineSeparator();
			}
		}
		br.close();
	}


	private static String addRelation(String line) {
		String[] aux = line.split(" ");
		int start = line.indexOf(":");
		int end = 20;
		if (line.contains(" rdf:resource=")) {
			end = line.indexOf(" rdf:resource=");
		}
		
		String result = line.substring(start+1, end);
		
		return result.replace("_", " ");
	}

	private static String addElement(String line) {
		int start = line.indexOf("/en/");
		
		String result = line.substring(start+4, line.length()-3);
		
		if (result.contains("/")) {
			result = result.substring(0, result.indexOf("/"));		
		} 
		
		return result.replace("_", " ");
	}
	
	private static String getDescription(String line) {
		String result = "";
		if (line.contains("/n/")) {
			int start = line.indexOf("/n/");
			result = line.substring(start+3, line.length()-2);
			
			return result.replace("_", " ");
		} else {
			return result;
		}
	}

	private static String getType(String line) {
		if (line.contains("/en/")) {
			return baseCase(line);
		
		} else if (line.contains("/documents\\")) {
			return docCase(line);
		
		} else if (line.contains("/r/")) {
			return rCase(line);		
		}
		
		return line;
	}

	
	private static String rCase(String line) {
		int start = line.indexOf("/r/");
		String result = line.substring(start+3, line.length()-2);
		
		if (result.contains("/")) {
			result = result.substring(0, result.indexOf("/"));			
		}
		
		if (result.contains("@CONTEXT")) {
			result = result.substring(0, result.indexOf("@CONTEXT"));
		}
		
		return result.replace("_", " ");
	}

	private static String docCase(String line) {
		int start = line.indexOf("/documents\\");
		String result = line.substring(start+11, line.length()-2);
		
		if (result.contains("\\") && result.contains(".txt")) {			
			result = result.substring(result.indexOf("\\")+1, result.indexOf("."));		
		} 
		
		if (result.contains("\\") && !result.contains(".txt")) {
			result = result.substring(result.indexOf("\\")+1);
		}
		
		if (result.contains("@CONTEXT")) {
			result = result.substring(0, result.indexOf("@CONTEXT"));
		}
		
		return result.replace("_", " ");
	}

	private static String baseCase(String line) {
		int start = line.indexOf("/en/");
		String result = line.substring(start+4, line.length()-2);
		
		if (result.contains("/")) {
			result = result.substring(0, result.indexOf("/"));
			
		} 
		if (result.contains("@CONTEXT")) {
			result = result.substring(0, result.indexOf("@CONTEXT"));
		}
		
		return result.replace("_", " ");
	}
	
	public static void finalRDF(String relation, String element) {
		String rel = "-" + relation + ": ";
		String elmt = element;
		
		finalText += rel + elmt + System.lineSeparator();
	}
	
	public static void finalRDF(Term term) {
		finalText += "<" + term.getType() + System.lineSeparator();
		if (!term.getDescription().equals("")) finalText += concatDescription(term);
	}	

	private static String concatDescription(Term term) {
		String str = "-Description: ";
		str += term.getDescription();
		str += System.lineSeparator();
		return str;
	}
}
