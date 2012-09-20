package org.salespointframework.core.catalog;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.util.Iterables;

// TODO comment
/**
 * 
 * @author Paul Henke
 *
 */
public class TransientCatalog implements Catalog<TransientProduct>{

	private static final Map<ProductIdentifier, TransientProduct> productMap = new HashMap<>();
	
	@Override
	public void add(TransientProduct product) {
		productMap.put(product.getIdentifier(), product);
	}

	@Override
	public boolean remove(ProductIdentifier productIdentifier) 
	{
		return productMap.remove(productIdentifier) != null;
	}

	@Override
	public boolean contains(ProductIdentifier productIdentifier) 
	{
		return productMap.containsKey(productIdentifier);
	}

	@Override
	public <E extends TransientProduct> E get(Class<E> clazz, ProductIdentifier productIdentifier) 
	{
		return clazz.cast(productMap.get(productIdentifier));
	}

	@Override
	public <E extends TransientProduct> Iterable<E> find(Class<E> clazz) 
	{
		List<E> temp = new LinkedList<E>();
		for(TransientProduct tp : productMap.values()) {
			if(tp.getClass().equals(clazz)) temp.add(clazz.cast(tp));
		}
		return Iterables.of(temp);
	}

	@Override
	public <E extends TransientProduct> Iterable<E> findByName(Class<E> clazz, String name)
	{
		List<E> temp = new LinkedList<E>();
		for(TransientProduct tp : productMap.values()) {
			if(tp.getClass().equals(clazz)) {
				if(tp.getName().contains(name)) temp.add(clazz.cast(tp));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <E extends TransientProduct> Iterable<E> findByCategory(Class<E> clazz, String category) 
	{
		List<E> temp = new LinkedList<E>();
		for(TransientProduct tp : productMap.values()) {
			if(tp.getClass().equals(clazz)) {
				Set<String> cats = Iterables.asSet(tp.getCategories());
				if(cats.contains(category)) temp.add(clazz.cast(tp));
			}
		}
		return Iterables.of(temp);
	}

}
