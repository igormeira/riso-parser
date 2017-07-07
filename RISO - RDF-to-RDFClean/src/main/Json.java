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

public class Json {
	
	private static String finalText;
	private static List<String> listaElements;
	private static List<String> lista;
	private File finalFile;
	
	public void getJson (File original, String finalFile) throws IOException {
		File dir = new File(".");
		finalText = "[" + System.lineSeparator();
		
		this.finalFile = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "json" + File.separator + finalFile);
		
		if(!this.finalFile.exists()){
			this.finalFile.createNewFile();
		}
		
		System.out.println(original.getName());
		readFile(original);
		prepareJson(original);
		writeFile(this.finalFile, finalText);
	}
	
	private void prepareJson(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		Term term = new Term();
		boolean aux = true;
		
		while ((line = br.readLine()) != null) {	
			if (line.contains("<rdf:Description")) {
				
				if (aux == true) aux = false;
				else finalText += "," + System.lineSeparator() + System.lineSeparator();
				
				term = new Term();
				term.setType(getType(line));
				term.setDescription(getDescription(line));
				finalRDF(term);
			
			} else if (line.contains("<rel:") && (!line.contains("<rel:hasText>"))) {
				listaElements = new ArrayList<String>();
								
				String relation = addRelation(line);
				while (line.contains(relation)) {
					String element = addElement(line);
					listaElements.add(element);
					line = br.readLine();
				}
				finalRDF(relation, listaElements);
			
			} if (line.contains("</rdf:Description>")) {
				finalText += System.lineSeparator() + "}";
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
		
		result = "\"" + result + "\"";
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
		
		result = "\"" + result + "\"";
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
		
		result = "\"" + result + "\"";
		return result.replace("_", " ");
	}
	
	public static void finalRDF(String relation, List<String> lista) {
		String elmts = "";
		String rel = "," + System.lineSeparator();
		rel += "\"" + relation + "\"" + ": [";
		for (int i = 0; i < lista.size() - 1; i++) {
			elmts += "\"" + lista.get(i) + "\"" + ", ";
		}
		elmts += "\"" + lista.get(lista.size()-1) + "\"" + "]";
		
		finalText += rel + elmts;
	}
	
	public static void finalRDF(Term term) {
		
		finalText += "{" + System.lineSeparator() + "\"Term\"" + ":" + term.getType();
		if (!term.getDescription().equals("")) finalText += concatDescription(term);
	}	
	
	private static String getDescription(String line) {
		String result = "";
		if (line.contains("/n/")) {
			int start = line.indexOf("/n/");
			result = "\"" + line.substring(start+3, line.length()-2) + "\"";
			
			return result.replace("_", " ");
		} else {
			return result;
		}
	}	

	private static String concatDescription(Term term) {
		String str = "," + System.lineSeparator();
		str += "\"Description\":";
		str += term.getDescription();
		return str;
	}

	private static void readFile(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
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
	}	
	
	private static void writeFile(File finalFile, String document) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(finalFile, "UTF-8");
		writer.println(document);
		writer.close();		
	}

}
