package com.jsonexplorer.ui;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.jsonexplorer.core.JSONInheritance;

/**
 * Integer attribute class
 * 
 * @author Ethem Kurt
 *
 */
public class IntegerAttributePanel extends NumberAttributePanel<BigInteger> {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -482300834939588254L;

	/**
	 * Create the panel.
	 */
	public IntegerAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance, new DecimalFormat("#"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.NumberAttributePanel#getAttributeValue()
	 */
	@Override
	public Object getAttributeValue() {
		BigInteger ret;
		try {
			getFormattedTextField().commitEdit();
			ret = new BigInteger(getFormattedTextField().getValue().toString());
		} catch (ParseException e) {
			ret = new BigInteger(getJSONInheritance().getValue().toString());
		}
		return ret;
	}

}
