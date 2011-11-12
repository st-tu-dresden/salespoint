package spielwiese.paul;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Metric;

public class Prod {

	private ProductIdentifier id = new ProductIdentifier();
	private String name;
	private Money price;
	private Metric metric;

	public Prod(String name, Money price, Metric metric) {
		this.name = name;
		this.price = price;
		this.metric = metric;
	}

	public ProductIdentifier getIdentifier() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Money getPrice() {
		return price;
	}

	public Metric getMetric() {
		return metric;
	}
}
