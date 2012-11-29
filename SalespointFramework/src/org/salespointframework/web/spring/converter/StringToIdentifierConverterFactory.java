package org.salespointframework.web.spring.converter;

import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.core.calendar.CalendarEntryIdentifier;
import org.salespointframework.core.inventory.InventoryItemIdentifier;
import org.salespointframework.core.order.ChargeLineIdentifier;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.SalespointIdentifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Converts a String to any subclass of {@link SalespointIdentifier}
 * @author Paul Henke
 *
 */
public class StringToIdentifierConverterFactory implements ConverterFactory<String, SalespointIdentifier> {

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SalespointIdentifier> Converter<String, T> getConverter(Class<T> clazz) {

		if(clazz.equals(AccountancyEntryIdentifier.class)) return (Converter<String, T>) new StringToAccountancyEntryIdentifier();
		if(clazz.equals(CalendarEntryIdentifier.class)) return  (Converter<String, T>) new StringToCalendarEntryIdentifier();
		if(clazz.equals(ProductIdentifier.class)) return  (Converter<String, T>) new StringToProductIdentifier();
		if(clazz.equals(InventoryItemIdentifier.class)) return  (Converter<String, T>) new StringToInventoryItemIdentifier();
		if(clazz.equals(OrderIdentifier.class)) return  (Converter<String, T>) new StringToOrderIdentifier();
		if(clazz.equals(OrderLineIdentifier.class)) return  (Converter<String, T>) new StringToOrderLineIdentifier();
		if(clazz.equals(ChargeLineIdentifier.class)) return  (Converter<String, T>) new StringToChargeLineIdentifier();
		if(clazz.equals(UserIdentifier.class)) return  (Converter<String, T>) new StringToUserIdentifier();

		return null;
	}
	
	private class StringToAccountancyEntryIdentifier implements Converter<String, AccountancyEntryIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public AccountancyEntryIdentifier convert(String identifier) {
			return new AccountancyEntryIdentifier(identifier);
		}
	}
	
	private class StringToCalendarEntryIdentifier implements Converter<String, CalendarEntryIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public CalendarEntryIdentifier convert(String identifier) {
			return new CalendarEntryIdentifier(identifier);
		}
	}
	
	private class StringToProductIdentifier implements Converter<String, ProductIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public ProductIdentifier convert(String identifier) {
			return new ProductIdentifier(identifier);
		}
	}
	
	private class StringToInventoryItemIdentifier implements Converter<String, InventoryItemIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public InventoryItemIdentifier convert(String identifier) {
			return new InventoryItemIdentifier(identifier);
		}
	}
	
	private class StringToOrderIdentifier implements Converter<String, OrderIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public OrderIdentifier convert(String identifier) {
			return new OrderIdentifier(identifier);
		}
	}
	
	private class StringToOrderLineIdentifier implements Converter<String, OrderLineIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public OrderLineIdentifier convert(String identifier) {
			return new OrderLineIdentifier(identifier);
		}
	}
	
	private class StringToChargeLineIdentifier implements Converter<String, ChargeLineIdentifier> {
		@SuppressWarnings("deprecation")
		@Override
		public ChargeLineIdentifier convert(String identifier) {
			return new ChargeLineIdentifier(identifier);
		}
	}

	private class StringToUserIdentifier implements Converter<String, UserIdentifier > {
		@Override
		public UserIdentifier  convert(String identifier) {
			return new UserIdentifier(identifier);
		}
	}
}
