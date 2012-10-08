package spielwiese.paul;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Quantity;

@Entity
public class QuantityMoneyClass {

	@Id private String id = UUID.randomUUID().toString();
	
	private Money price;
	private Quantity quantity;
	
	public Money getPrice() {
		return price;
	}
	public void setPrice(Money price) {
		this.price = Objects.requireNonNull(price, "price");
	}
	public Quantity getQuantity() {
		return quantity;
	}
	public void setQuantity(Quantity quantity) {
		this.quantity = Objects.requireNonNull(quantity, "quantity");
	}
	public String getId() {
		return id;
	}
	
}
