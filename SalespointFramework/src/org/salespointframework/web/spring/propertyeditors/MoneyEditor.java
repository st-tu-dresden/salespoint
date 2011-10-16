package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

import org.salespointframework.core.money.Money;

/**
 * 
 * @author Paul Henke
 *
 */
public class MoneyEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		BigDecimal bd = new BigDecimal(text);
		Money money = new Money(bd);
		setValue(money);
	}
}
