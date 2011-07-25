package org.salespointframework.core.product;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeature;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(AbstractProductInstance.class)
public abstract class AbstractProductInstance_ { 

    public static volatile MapAttribute<AbstractProductInstance, String, ProductFeature> productFeatures;
    public static volatile SingularAttribute<AbstractProductInstance, Money> price;
    public static volatile SingularAttribute<AbstractProductInstance, Long> serialNumber;
    public static volatile SingularAttribute<AbstractProductInstance, ? extends Object> productType;

}