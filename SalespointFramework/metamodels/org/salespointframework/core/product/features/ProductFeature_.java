package org.salespointframework.core.product.features;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(ProductFeature.class)
public class ProductFeature_ { 

    public static volatile SingularAttribute<ProductFeature, Integer> id;
    public static volatile SingularAttribute<ProductFeature, Money> price;
    public static volatile SingularAttribute<ProductFeature, String> name;

}