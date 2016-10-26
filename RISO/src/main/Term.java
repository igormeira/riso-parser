package main;

import java.util.ArrayList;
import java.util.List;

public class Term {
	
	private String type;
	private List<String> terms;
	private List<String> is_a;
	private List<String> part_of;
	private List<String> has_part;
	private List<String> instance_of;
	private List<String> has_subtype;
	private List<String> has_context;
	private List<String> context_of;
	private List<String> has_instance;
	private List<String> acronym_of;
	private List<String> has_acronym;
	private List<String> indexing;
	
	public Term() {
		this.terms = new ArrayList<String>();
		this.is_a = new ArrayList<String>();
		this.part_of = new ArrayList<String>();
		this.has_part = new ArrayList<String>();
		this.instance_of = new ArrayList<String>();
		this.has_subtype = new ArrayList<String>();
		this.has_context = new ArrayList<String>();
		this.context_of = new ArrayList<String>();
		this.has_instance = new ArrayList<String>();
		this.has_acronym = new ArrayList<String>();
		this.acronym_of = new ArrayList<String>();
		this.indexing = new ArrayList<String>();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public List<String> getIs_a() {
		return is_a;
	}

	public void setIs_a(List<String> is_a) {
		this.is_a = is_a;
	}

	public List<String> getPart_of() {
		return part_of;
	}

	public void setPart_of(List<String> part_of) {
		this.part_of = part_of;
	}

	public List<String> getHas_part() {
		return has_part;
	}

	public void setHas_part(List<String> has_part) {
		this.has_part = has_part;
	}

	public List<String> getInstance_of() {
		return instance_of;
	}

	public void setInstance_of(List<String> instance_of) {
		this.instance_of = instance_of;
	}

	public List<String> getHas_subtype() {
		return has_subtype;
	}

	public void setHas_subtype(List<String> has_subtype) {
		this.has_subtype = has_subtype;
	}

	public List<String> getHas_context() {
		return has_context;
	}

	public void setHas_context(List<String> has_context) {
		this.has_context = has_context;
	}
	

	public List<String> getContext_of() {
		return context_of;
	}

	public void setContext_of(List<String> context_of) {
		this.context_of = context_of;
	}

	public List<String> getHas_instance() {
		return has_instance;
	}

	public void setHas_instance(List<String> has_instance) {
		this.has_instance = has_instance;
	}

	public List<String> getAcronym_of() {
		return acronym_of;
	}

	public void setAcronym_of(List<String> acronym_of) {
		this.acronym_of = acronym_of;
	}

	public List<String> getHas_acronym() {
		return has_acronym;
	}

	public void setHas_acronym(List<String> has_acronym) {
		this.has_acronym = has_acronym;
	}

	public List<String> getIndexing() {
		return indexing;
	}

	public void setIndexing(List<String> indexing) {
		this.indexing = indexing;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acronym_of == null) ? 0 : acronym_of.hashCode());
		result = prime * result + ((context_of == null) ? 0 : context_of.hashCode());
		result = prime * result + ((has_acronym == null) ? 0 : has_acronym.hashCode());
		result = prime * result + ((has_context == null) ? 0 : has_context.hashCode());
		result = prime * result + ((has_instance == null) ? 0 : has_instance.hashCode());
		result = prime * result + ((has_part == null) ? 0 : has_part.hashCode());
		result = prime * result + ((has_subtype == null) ? 0 : has_subtype.hashCode());
		result = prime * result + ((indexing == null) ? 0 : indexing.hashCode());
		result = prime * result + ((instance_of == null) ? 0 : instance_of.hashCode());
		result = prime * result + ((is_a == null) ? 0 : is_a.hashCode());
		result = prime * result + ((part_of == null) ? 0 : part_of.hashCode());
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (type.equals(other.type)) {
			return true;
		}
		return false;
	}
	
	
	
}
