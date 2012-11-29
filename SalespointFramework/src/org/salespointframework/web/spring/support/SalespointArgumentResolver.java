package org.salespointframework.web.spring.support;

import java.util.Map;

import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.core.calendar.Calendar;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.calendar.CalendarEntryIdentifier;
import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.InventoryItem;
import org.salespointframework.core.inventory.InventoryItemIdentifier;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderManager;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

public class SalespointArgumentResolver implements HandlerMethodArgumentResolver {

	@SuppressWarnings("unchecked")
	private String getValue(NativeWebRequest webRequest, String name) {
		String value = (String)webRequest.getParameter(name);
		if(value != null) {
			return value;
		}
		Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		return (uriTemplateVars != null) ? uriTemplateVars.get(name) : null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		String name = parameter.getParameterAnnotation(org.salespointframework.web.annotation.Get.class).value();
		
		String value = this.getValue(webRequest, name);
		
		if(value == null) return null;
		
		Class clazz = parameter.getParameterType();

		if(User.class.isAssignableFrom(clazz)) {
			return ((UserManager<User>) Shop.INSTANCE.getUserManager()).get(clazz, new UserIdentifier(value));
		}
		
		if(AccountancyEntry.class.isAssignableFrom(clazz)) {
			return ((Accountancy<AccountancyEntry>) Shop.INSTANCE.getAccountancy()).get(clazz, new AccountancyEntryIdentifier(value));
		}
		
		if(CalendarEntry.class.isAssignableFrom(clazz)) {
			return ((Calendar<CalendarEntry>) Shop.INSTANCE.getCalendar()).get(clazz, new CalendarEntryIdentifier(value));
		}
		
		if(Product.class.isAssignableFrom(clazz)) {
			return ((Catalog<Product>) Shop.INSTANCE.getCatalog()).get(clazz, new ProductIdentifier(value));
		}
		
		if(InventoryItem.class.isAssignableFrom(clazz)) {
			return ((Inventory<InventoryItem>) Shop.INSTANCE.getInventory()).get(clazz, new InventoryItemIdentifier(value));
		}
		
		if(Order.class.isAssignableFrom(clazz)) {
			return ((OrderManager<Order<OrderLine>, OrderLine>) Shop.INSTANCE.getOrderManager()).get(clazz, new OrderIdentifier(value));
		}

		
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class clazz = parameter.getParameterType();
		return parameter.hasParameterAnnotation(org.salespointframework.web.annotation.Get.class) && 
			   (User.class.isAssignableFrom(clazz) || AccountancyEntry.class.isAssignableFrom(clazz) || CalendarEntry.class.isAssignableFrom(clazz) ||
			   Product.class.isAssignableFrom(clazz) || InventoryItem.class.isAssignableFrom(clazz) || Order.class.isAssignableFrom(clazz));
	}

}
