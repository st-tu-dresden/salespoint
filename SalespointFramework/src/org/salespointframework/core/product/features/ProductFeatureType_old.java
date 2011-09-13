package org.salespointframework.core.product.features;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;


// TODO Delete, Legacy Code
@Entity
@Deprecated
public class ProductFeatureType_old {
	
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private String description;
	
	// Name -> Feature
	@ElementCollection
	private Map<String, ProductFeature_old> possibleValues = new HashMap<String, ProductFeature_old>();
	
	@Deprecated
	protected ProductFeatureType_old() { }
	
	public ProductFeatureType_old(String name, ProductFeature_old... productFeatures) {
		this(name, "", productFeatures);
	}
	
	public ProductFeatureType_old(String name, String description, ProductFeature_old... productFeatures) {
		this.name = Objects.requireNonNull(name, "name");
		this.description = Objects.requireNonNull(description, "description");
		Objects.requireNonNull(productFeatures, "productFeatures");
		if(productFeatures.length == 0) throw new RuntimeException(); 	//TODO bessere Exception
		for(ProductFeature_old pf : productFeatures) {
			if(pf != null) this.addFeature(pf);
		}
	}
	
	public final String getName() {
		return name;
	}
	public final String getDescription() {
		return description;
	}

	public final void addFeature(ProductFeature_old productFeature) {
		possibleValues.put(productFeature.getName(), productFeature);
	}
	
	public final void removeFeature(String name) {
		possibleValues.remove(name);
	}
	
	public final ProductFeature_old getProductFeature(String name) {
		return possibleValues.get(name);
	}
	
	public final Iterable<ProductFeature_old> getProductFeatures() {
		return Iterables.from(possibleValues.values());
	}
}
