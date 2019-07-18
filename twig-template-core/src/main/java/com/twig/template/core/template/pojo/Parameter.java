package com.twig.template.core.template.pojo;

import java.util.List;

public class Parameter {
	
	private String name;
	private String type;
	boolean required = true;
	String default_value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getDefault_value() {
		return default_value;
	}
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
	
	@Override
	public String toString() {
		return "Parameter [name=" + name + ", type=" + type + ","  + ", required="
				+ required + ", default_value=" + default_value + "]";
	}

}