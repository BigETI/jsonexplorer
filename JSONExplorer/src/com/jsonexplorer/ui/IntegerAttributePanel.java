package com.jsonexplorer.ui;

import java.math.BigInteger;

import com.jsonexplorer.core.EAttributeTypes;
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
	 * Constructor
	 * 
	 * Create the panel.
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public IntegerAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance);
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
			// getFormattedTextField().commitEdit();
			// ret = new
			// BigInteger(getFormattedTextField().getValue().toString());
			ret = new BigInteger(getTextField().getText());
		} catch (Exception e) {
			getTextField().setText(getJSONInheritance().getValue().toString());
			ret = new BigInteger(getJSONInheritance().getValue().toString());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeType()
	 */
	@Override
	public EAttributeTypes getAttributeType() {
		return EAttributeTypes.INTEGER;
	}
}
