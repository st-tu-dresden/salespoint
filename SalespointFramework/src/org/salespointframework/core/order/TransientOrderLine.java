package org.salespointframework.core.order;



import java.util.Objects;

import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.shop.Shop;


public class TransientOrderLine implements OrderLine {
	
	private OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	private ProductIdentifier productIdentifier;

	private Quantity quantity;

	private Money price = Money.ZERO;

	private String productName;
	
	public TransientOrderLine(ProductIdentifier productIdentifier, Quantity quantity)
	{
		
		this.productIdentifier = Objects.requireNonNull(productIdentifier, "productIdentifier must be not null");
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");

		Catalog<?> temp = Shop.INSTANCE.getCatalog();
		
		if(temp == null) 
		{
			throw new RuntimeException("Shop.INSTANCE.getCatalog() returns null");
		}
		
		if(!(temp instanceof TransientCatalog)) 
		{
			throw new RuntimeException("Shop.INSTANCE.getCatalog() returns a non TransientCatalog");
		}
		
		TransientCatalog catalog = (TransientCatalog) temp;
	
		TransientProduct product = catalog.get(TransientProduct.class, productIdentifier);
		
		if(!product.getMetric().equals(quantity.getMetric())) {
			throw new MetricMismatchException("product.getMetric is not equal to quantity.getMetric");
		}
		
		this.price = quantity.multiply(product.getPrice());
		this.productName = product.getName();
	}

	@Override
	public OrderLineIdentifier getIdentifier() {
		return orderLineIdentifier;
	}

	@Override
	public ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}


	@Override
	public Quantity getQuantity() {
		return quantity;
	}

	@Override
	public Money getPrice() {
		return price;
	}
	
	@Override
	public String getProductName() {
		return productName;
	}
	
	@Override
	public int hashCode() {
		return orderLineIdentifier.hashCode();
	}
	
	@Override
	public String toString() {
		return productName + "(" + productIdentifier.toString() + ")" + " - " + quantity;
	}
}
