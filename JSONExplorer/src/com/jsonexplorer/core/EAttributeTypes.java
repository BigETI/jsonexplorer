package com.jsonexplorer.core;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.json.JSONObject;

/**
 * Enumerator of all editable attribute types
 * 
 * @author Ethem Kurt
 *
 */
public enum EAttributeTypes {

	/**
	 * null
	 */
	NULL(JSONObject.NULL, "null"),

	/**
	 * String
	 */
	STRING("", "String"),

	/**
	 * Integer
	 */
	INTEGER(new BigInteger("0"), "Integer"),

	/**
	 * Decimal
	 */
	DECIMAL(new BigDecimal(0.0), "Decimal"),

	/**
	 * Boolean
	 */
	BOOLEAN(new Boolean(false), "Boolean");

	/**
	 * Default value
	 */
	private Object default_value;

	/**
	 * Name
	 */
	private String name;

	/**
	 * Constructor
	 * 
	 * @param default_value
	 *            Default value
	 * @param name
	 *            Name
	 */
	EAttributeTypes(Object default_value, String name) {
		this.default_value = default_value;
		this.name = name;
	}

	/**
	 * Get default value
	 * 
	 * @return Default value
	 */
	public Object getDefaultValue() {
		return default_value;
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
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
