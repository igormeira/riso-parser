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
	
	static List<Term> lista;

	public static void main(String[] args) throws IOException {
		File dir = new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "files" + File.separator + "textfile.txt");
		File finalFile = new File(dir.getCanonicalPath() + File.separator + 
				"src" + File.separator + "files" + File.separator + "finaltextfile.txt");
		
		System.out.println(fin.toString());
		String document = readFile(fin);
		//System.out.println(document);
		writeFile(finalFile, document);
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
	 
		lista = new ArrayList<Term>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.contains("label")) {
				Term term = populateListNewTerm(line);
				if(termExist(term)) {
					populateListOldTerm(term);
				}
				else {
					lista.add(term);
				}
			}
			//System.out.println(line);
		}
		br.close();
		return finalText();
	}

	private static void populateListOldTerm(Term term) {
		for (Term termLista : lista) {
			if (term.getType() != null && termLista.equals(term)) {
								
				
				if (term.getTerms().size() > 0) {
					for (String terms : term.getTerms()) {
						termLista.getTerms().add(terms);
					}
				}
				if (term.getIs_a().size() > 0) {
					for (String isa : term.getIs_a()) {
						termLista.getIs_a().add(isa);
					}
				}
				if (term.getPart_of().size() > 0) {
					for (String partof : term.getPart_of()) {
						termLista.getPart_of().add(partof);
					}
				}
				if (term.getHas_part().size() > 0) {
					for (String haspart : term.getHas_part()) {
						termLista.getHas_part().add(haspart);
					}
				}
				if (term.getHas_subtype().size() > 0) {
					for (String hassubtype : term.getHas_subtype()) {
						termLista.getHas_subtype().add(hassubtype);
					}
				}
				if (term.getHas_context().size() > 0) {
					for (String hascontext : term.getHas_context()) {
						termLista.getHas_context().add(hascontext);
					}
				}
			}
		}
	}

	private static boolean termExist(Term term) {
		for (Term termLista : lista) {
			if (termLista.equals(term)) {
				return true;
			}
		}
		return false;
	}

	private static Term populateListNewTerm(String line) {
		Term term = new Term();
		String[] elements = getElements(line);
		term = populateTerm(term, elements);
		return term;
	}

	private static Term populateTerm(Term term, String[] elements) {
		Term newTerm = term;
		String[] newElements = elements;
		
		newTerm.setType(newElements[0]);
		
		if (newElements[2].equals("is-a")) term.getIs_a().add(newElements[1]);
		else if (newElements[2].equals("part-of")) term.getPart_of().add(newElements[1]);
		else if (newElements[2].equals("has-part")) term.getHas_part().add(newElements[1]);
		else if (newElements[2].equals("instance-of")) term.getInstance_of().add(newElements[1]);
		else if (newElements[2].equals("has-subtype")) term.getHas_subtype().add(newElements[1]);
		else if (newElements[2].equals("has-context")) term.getHas_context().add(newElements[1]);
		
		return newTerm;
	}

	private static String[] getElements(String line) {
		String type;
		String label;
		String labelElement;
		
		String[] split = line.split(" ");
		type = split[0];
		labelElement = split[2];
		label = split[3].substring(8, split[3].length()-3);
		
		String[] elements = new String[3];
		elements[0] = type;
		elements[1] = labelElement;
		elements[2] = label;
		return elements;
	}
	
	public static String finalText() {
		String termAux = "";
		String finalStr = "[";		
		
		addInverseRelation();
		
		for (Term term : lista) {
			finalStr += System.lineSeparator() + "{" + System.lineSeparator();
			finalStr += "\"type\":"+ "\"" + term.getType() + "\"" + "," + System.lineSeparator();
			
			//finalStr += concatTerms(term);
			finalStr += concatIsA(term);
			finalStr += concatPartOf(term);
			finalStr += concatHasPart(term);
			finalStr += concatInstanceOf(term);
			finalStr += concatHasSubtype(term);
			finalStr += concatHasContext(term);
			
			if (term.getType() != null && term.getType().equals(lista.get(lista.size()-1).getType())) {
				finalStr += "}" + System.lineSeparator();
			}
			else {
				finalStr += "}," + System.lineSeparator();
			}			
		}
		finalStr += "]";
		return finalStr;		
	}
	
	private static void addInverseRelation() {
		for (Term majorTerm : lista) {
			//IsA relation
			for (String isa : majorTerm.getIs_a()) {
				for (Term term : lista) {
					if (term.getType().equals(isa)) {
						if (!term.getHas_subtype().contains(majorTerm.getType())) {
							term.getHas_subtype().add(majorTerm.getType());
						}
					}
				}
			}
			//PartOf relation
			for (String po : majorTerm.getPart_of()) {
				for (Term term : lista) {
					if (term.getType().equals(po)) {
						if (!term.getHas_part().contains(majorTerm.getType())) {
							term.getHas_part().add(majorTerm.getType());
						}
					}
				}
			}
			//HasSub relation
			for (String sub : majorTerm.getHas_subtype()) {
				for (Term term : lista) {
					if (term.getType().equals(sub)) {
						if (!term.getIs_a().contains(majorTerm.getType())) {
							term.getIs_a().add(majorTerm.getType());
						}
					}
				}
			}
			//HasPart relation
			for (String hp : majorTerm.getHas_part()) {
				for (Term term : lista) {
					if (term.getType().equals(hp)) {
						if (!term.getPart_of().contains(majorTerm.getType())) {
							term.getPart_of().add(majorTerm.getType());
						}
					}
				}
			}
		}
		
	}

	//Concatenates
	private static String concatHasContext(Term term) {
		String str = "\"hascontext\": [";
		String lastSub = "";
		
		for (String element : term.getHas_context()) {
			if (element.contains("_")) {
				int indexBegin = element.indexOf("_") + 1;
				int indexFinal = element.length();
				String sub = element.substring(indexBegin, indexFinal);				
				element = sub;
				
				String lastElement = term.getHas_context().get(term.getHas_context().size()-1);
				lastSub = lastElement.substring(indexBegin, lastElement.length());
			}		
			if (!str.contains(element)) {
				str += "\"" + element + "\"";
				if (element.equals(term.getHas_context().get(term.getHas_context().size()-1)) || element.equals(lastSub)) {
					str += "]" + System.lineSeparator();
				}
				else {
					str += ", ";
				}
			}
		}
		
		if (term.getHas_context().size() == 0) {
			str += "]" + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatHasSubtype(Term term) {
		String str = "\"hassubtype\": [";
		
		for (String element : term.getHas_subtype()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getHas_subtype().get(term.getHas_subtype().size()-1))) {
				str += "]," + System.lineSeparator();
			}
			else {
				str += ", ";
			}
		}
		
		if (term.getHas_subtype().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatInstanceOf(Term term) {
		String str = "\"instanceof\": [";
		
		for (String element : term.getInstance_of()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getInstance_of().get(term.getInstance_of().size()-1))) {
				str += "]," + System.lineSeparator();
			}
			else {
				str += ", ";
			}
		}
		
		if (term.getInstance_of().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatHasPart(Term term) {
		String str = "\"haspart\": [";
		
		for (String element : term.getHas_part()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getHas_part().get(term.getHas_part().size()-1))) {
				str += "]," + System.lineSeparator();
			}
			else {
				str += ", ";
			}
		}
		
		if (term.getHas_part().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatPartOf(Term term) {
		String str = "\"partof\": [";
		
		for (String element : term.getPart_of()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getPart_of().get(term.getPart_of().size()-1))) {
				str += "]," + System.lineSeparator();
			}
			else {
				str += ", " + System.lineSeparator();
			}
		}
		
		if (term.getPart_of().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatIsA(Term term) {
		String str = "\"isa\": [";
		
		for (String element : term.getIs_a()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getIs_a().get(term.getIs_a().size()-1))) {
				str += "]," + System.lineSeparator();
			}
			else {
				str += ", ";
			}
		}
		
		if (term.getIs_a().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}

	private static String concatTerms(Term term) {
		String str = "\"terms\": [";
		
		for (String element : term.getTerms()) {
			str += "\"" + element + "\"";
			if (element.equals(term.getTerms().get(term.getTerms().size()-1))) {
				str += ",]" + System.lineSeparator();
			}
			else {
				str += ", ";
			}
		}
		
		if (term.getTerms().size() == 0) {
			str += "]," + System.lineSeparator();
		}
		
		return str;
	}
}
