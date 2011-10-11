package org.salespointframework.web;

import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.core.calendar.CalendarEntryIdentifier;
import org.salespointframework.core.order.ChargeLineIdentifier;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.product.ProductFeatureIdentifier;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.core.user.UserCapability;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.web.spring.propertyeditors.AccountancyEntryIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.CalendarEntryIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.ChargeLineIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.OrderIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.OrderLineIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.ProductFeatureIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.ProductIdentifierEditor;
import org.salespointframework.web.spring.propertyeditors.SerialNumberEditor;
import org.salespointframework.web.spring.propertyeditors.UserCapabilityEditor;
import org.salespointframework.web.spring.propertyeditors.UserIdentifierEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

// http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/validation.html
// http://www.shaunabram.com/data-binding-in-spring-mvc/

/**
 * 
 * @author Paul Henke
 * 
 */
public class GlobalBindingInitializer implements WebBindingInitializer
{
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request)
	{
		binder.registerCustomEditor(AccountancyEntryIdentifier.class, new AccountancyEntryIdentifierEditor());
		binder.registerCustomEditor(ChargeLineIdentifier.class, new ChargeLineIdentifierEditor());
		binder.registerCustomEditor(CalendarEntryIdentifier.class, new CalendarEntryIdentifierEditor());
		binder.registerCustomEditor(OrderIdentifier.class, new OrderIdentifierEditor());
		binder.registerCustomEditor(OrderLineIdentifier.class, new OrderLineIdentifierEditor());
		binder.registerCustomEditor(SerialNumber.class, new SerialNumberEditor());
		binder.registerCustomEditor(ProductFeatureIdentifier.class, new ProductFeatureIdentifierEditor());
		binder.registerCustomEditor(ProductIdentifier.class, new ProductIdentifierEditor());
		binder.registerCustomEditor(UserIdentifier.class, new UserIdentifierEditor());
		binder.registerCustomEditor(UserCapability.class, new UserCapabilityEditor());
	}
}
