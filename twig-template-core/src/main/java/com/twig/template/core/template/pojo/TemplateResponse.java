package com.twig.template.core.template.pojo;

import java.util.List;

public class TemplateResponse {

	private String templateID;
	private String templateNlg;
	private String headNlg;
	private String footNlg;
	private List<String> footNoteIds;
	private String desctiption;
	
	public String getTemplateID() {
		return templateID;
	}
	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	public String getTemplateNlg() {
		return templateNlg;
	}
	public void setTemplateNlg(String templateNlg) {
		this.templateNlg = templateNlg;
	}
	public String getHeadNlg() {
		return headNlg;
	}
	public void setHeadNlg(String headNlg) {
		this.headNlg = headNlg;
	}
	public String getFootNlg() {
		return footNlg;
	}
	public void setFootNlg(String footNlg) {
		this.footNlg = footNlg;
	}
	public List<String> getFootNoteIds() {
		return footNoteIds;
	}
	public void setFootNoteIds(List<String> footNoteIds) {
		this.footNoteIds = footNoteIds;
	}
	public String getDesctiption() {
		return desctiption;
	}
	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}
}
