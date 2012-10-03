package org.salespointframework.core.order;



import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.TransientProduct;
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
		this.productIdentifier = productIdentifier;
		this.quantity = quantity;
		
		TransientCatalog catalog = (TransientCatalog) Shop.INSTANCE.getCatalog();
	
		TransientProduct product = catalog.get(TransientProduct.class, productIdentifier);
		
		this.price = (Money) product.getPrice().multiply(quantity); 
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
	public Iterable<ProductFeature> getProductFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOrdered() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Quantity getQuantity() {
		return quantity;
	}

	@Override
	public Money getPrice() {
		return price;
	}

}
