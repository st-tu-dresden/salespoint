package org.salespointframework.core.product.features;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

// TODO umbenennen
@Entity
public class ProductFeatureType {
	
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;
	
	//TODO  @Id
	private String name;
	private String description;
	
	// Name -> Feature
	private Map<String, ProductFeature> possibleValues = new HashMap<String, ProductFeature>();
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}

	public void addFeature(ProductFeature productFeature) {
		possibleValues.put(productFeature.getName(), productFeature);
	}
	
	// TODO String (name) statt ProductFeature?
	public void removeFeature(ProductFeature productFeature) {
		possibleValues.remove(productFeature.getName());
	}
	
	public ProductFeature getProductFeature(String name) {
		return possibleValues.get(name);
	}
	
	@Deprecated
	protected ProductFeatureType() { }
	
	// TODO Validierung
	public ProductFeatureType(String name, String description, ProductFeature... productFeatures) {
		this.name = Objects.requireNonNull(name, "name");
		this.description = Objects.requireNonNull(description, "description");
		Objects.requireNonNull(productFeatures, "productFeatures");
		
		for(ProductFeature pf : productFeatures) {
			if(pf != null) this.addFeature(pf);
		}
		
	}
	
	public Iterable<ProductFeature> getPossibleValues() {
		return SalespointIterable.from(possibleValues.values());
	}
	
}
