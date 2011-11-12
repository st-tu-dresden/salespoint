package spielwiese.paul;

import java.util.HashMap;
import java.util.Map;

import org.salespointframework.core.quantity.Quantity;

public class Inv {
	
	private Map<Prod, Quantity> map = new HashMap<Prod,Quantity>();
	
	public void add(Prod product, Quantity quantity) {
		if(!product.getMetric().equals(quantity.getMetric())) throw new RuntimeException();
			
		Quantity q = map.get(product);
		if(q == null) {
			q = quantity;
		} else {
			q = q.add(quantity);
		}
		map.put(product, q);
	}
	
	public void remove(Prod product, Quantity quantity) {
		if(!product.getMetric().equals(quantity.getMetric())) throw new RuntimeException();
		
		Quantity q = map.get(product);
		if(q == null) {
			q = quantity;
		} else {
			q = q.subtract(quantity);
		}
		map.put(product, q);
		
	}
	
	public Quantity getQuantity(Prod product) {
		return map.get(product);
	}

}
