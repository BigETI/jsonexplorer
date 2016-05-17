package com.jsonexplorer.ui;

import java.math.BigDecimal;

import com.jsonexplorer.core.EAttributeTypes;
import com.jsonexplorer.core.JSONInheritance;

/**
 * Decimal attribute class
 * 
 * @author Ethem Kurt
 *
 */
public class DecimalAttributePanel extends NumberAttributePanel<BigDecimal> {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 3751502966547175817L;

	/**
	 * Constructor
	 * 
	 * Create the panel.
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public DecimalAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.NumberAttributePanel#getAttributeValue()
	 */
	@Override
	public Object getAttributeValue() {
		BigDecimal ret;
		try {
			// getFormattedTextField().commitEdit();
			// ret = new
			// BigDecimal(getFormattedTextField().getValue().toString());
			ret = new BigDecimal(getTextField().getText());
		} catch (Exception e) {
			getTextField().setText(getJSONInheritance().getValue().toString());
			ret = new BigDecimal(getJSONInheritance().getValue().toString());
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
		return EAttributeTypes.DECIMAL;
	}
}
