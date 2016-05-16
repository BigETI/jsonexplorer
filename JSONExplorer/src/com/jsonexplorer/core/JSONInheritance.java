package com.jsonexplorer.core;

public class JSONInheritance {

	private Object value;

	private Object key;

	private Object parent;
	
	private String name;

	public JSONInheritance(Object value, Object key, Object parent, String name) {
		this.value = value;
		this.key = key;
		this.parent = parent;
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public Object getKey() {
		return key;
	}

	public Object getParent() {
		return parent;
	}
	
	public Object getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
