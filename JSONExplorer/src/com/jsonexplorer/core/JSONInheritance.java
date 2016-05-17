package com.jsonexplorer.core;

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
	private Object parent;

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
	public JSONInheritance(Object value, Object key, Object parent, String name) {
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
	public Object getParent() {
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
