package com.twig.template.core.template.pojo;

import java.util.*;

public class TemplateDefination {
	
	private String templateID;
	private String description;
	
	private List<String> footNoteId;
	
	private List<String> templateStrings;
	private List<String> headTemplates;
	private List<String> footTemplates;
	
	private List<Parameter> templateParameters;
	private List<Parameter> headTemplateParameters;
	private List<Parameter> footTemplateParameters;
	
	public String getTemplateID() {
		return templateID;
	}
	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getFootNoteId() {
		return footNoteId;
	}
	public void setFootNoteId(List<String> footNoteId) {
		this.footNoteId = footNoteId;
	}
	public List<String> getTemplateStrings() {
		return templateStrings;
	}
	public void setTemplateStrings(List<String> templateStrings) {
		this.templateStrings = templateStrings;
	}
	public List<String> getHeadTemplates() {
		return headTemplates;
	}
	public void setHeadTemplates(List<String> headTemplates) {
		this.headTemplates = headTemplates;
	}
	public List<String> getFootTemplates() {
		return footTemplates;
	}
	public void setFootTemplates(List<String> footTemplates) {
		this.footTemplates = footTemplates;
	}
	public List<Parameter> getTemplateParameters() {
		return templateParameters;
	}
	public void setTemplateParameters(List<Parameter> templateParameters) {
		this.templateParameters = templateParameters;
	}
	public List<Parameter> getHeadTemplateParameters() {
		return headTemplateParameters;
	}
	public void setHeadTemplateParameters(List<Parameter> headTemplateParameters) {
		this.headTemplateParameters = headTemplateParameters;
	}
	public List<Parameter> getFootTemplateParameters() {
		return footTemplateParameters;
	}
	public void setFootTemplateParameters(List<Parameter> footTemplateParameters) {
		this.footTemplateParameters = footTemplateParameters;
	}
}
