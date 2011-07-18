package org.salespointframework.core.product.features;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.product.features.ProductFeature;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(ProductFeatureType.class)
public class ProductFeatureType_ { 

    public static volatile SingularAttribute<ProductFeatureType, Integer> id;
    public static volatile MapAttribute<ProductFeatureType, String, ProductFeature> possibleValues;
    public static volatile SingularAttribute<ProductFeatureType, String> description;
    public static volatile SingularAttribute<ProductFeatureType, String> name;

}