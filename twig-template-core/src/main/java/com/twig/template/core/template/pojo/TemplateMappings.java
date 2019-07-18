package com.twig.template.core.template.pojo;

import java.util.ArrayList;
import java.util.List;

public class TemplateMappings {
	String filename;
	String description;
	List<TemplateDefination> allMappings;

	public TemplateMappings() {
		super();
		this.allMappings = new ArrayList<TemplateDefination>();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TemplateDefination> getAllMappings() {
		return allMappings;
	}

	public void setAllMappings(List<TemplateDefination> allMappings) {
		this.allMappings = allMappings;
	}

	public void addMapping(TemplateDefination mapping) {
		this.allMappings.add(mapping);
	}

	
}
