package org.salespointframework.core.product.features;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;


// TODO umbenennen
@Entity
public class ProductFeatureType {
	
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private String description;
	
	// Name -> Feature
	@ElementCollection
	private Map<String, ProductFeature> possibleValues = new HashMap<String, ProductFeature>();
	
	@Deprecated
	protected ProductFeatureType() { }
	
	public ProductFeatureType(String name, ProductFeature... productFeatures) {
		this(name, "", productFeatures);
	}
	
	public ProductFeatureType(String name, String description, ProductFeature... productFeatures) {
		this.name = Objects.requireNonNull(name, "name");
		this.description = Objects.requireNonNull(description, "description");
		Objects.requireNonNull(productFeatures, "productFeatures");
		if(productFeatures.length == 0) throw new RuntimeException(); 	//TODO bessere Exception
		for(ProductFeature pf : productFeatures) {
			if(pf != null) this.addFeature(pf);
		}
	}
	
	public final String getName() {
		return name;
	}
	public final String getDescription() {
		return description;
	}

	public final void addFeature(ProductFeature productFeature) {
		possibleValues.put(productFeature.getName(), productFeature);
	}
	
	public final void removeFeature(String name) {
		possibleValues.remove(name);
	}
	
	public final ProductFeature getProductFeature(String name) {
		return possibleValues.get(name);
	}
	
	public final Iterable<ProductFeature> getProductFeatures() {
		return Iterables.from(possibleValues.values());
	}
}
