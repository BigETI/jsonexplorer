package com.jsonexplorer.core;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class to store inheritance of JSON attributes
 * 
 * @author Ethem Kurt
 *
 */
public class JSONInheritance {

	/**
	 * Value
	 */
	private Object value;

	/**
	 * Key
	 */
	private Object key;

	/**
	 * Parent
	 */
	private JSONInheritance parent;

	/**
	 * Name
	 */
	private String name;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            Value
	 * @param key
	 *            Key
	 * @param parent
	 *            Parent
	 * @param name
	 *            Name
	 */
	public JSONInheritance(Object value, Object key, JSONInheritance parent, String name) {
		this.value = value;
		this.key = key;
		this.parent = parent;
		this.name = name;
	}

	/**
	 * Get value
	 * 
	 * @return Value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Get key
	 * 
	 * @return Key
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * Get parent
	 * 
	 * @return Parent
	 */
	public JSONInheritance getParent() {
		return parent;
	}

	/**
	 * Get name
	 * 
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get JSON path
	 * 
	 * @return JSON path
	 */
	public String getPath() {
		StringBuilder ret = new StringBuilder();
		if (parent != null) {
			ret.append(parent.getPath());
			if (parent.value instanceof JSONObject) {
				ret.append(".");
				ret.append(key);
			} else if (parent.value instanceof JSONArray) {
				ret.append("[");
				ret.append(key);
				ret.append("]");
			}
		} else
			ret.append("<Root>");
		return ret.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
