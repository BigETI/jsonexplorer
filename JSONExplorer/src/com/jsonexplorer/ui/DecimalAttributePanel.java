package com.jsonexplorer.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

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
	 * Create the panel.
	 */
	public DecimalAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance, new DecimalFormat("#.#"));
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
			getFormattedTextField().commitEdit();
			ret = new BigDecimal(getFormattedTextField().getValue().toString());
		} catch (ParseException e) {
			ret = new BigDecimal(getJSONInheritance().getValue().toString());
		}
		return ret;
	}

}
