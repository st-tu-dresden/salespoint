package org.salespointframework.core.product;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.ProductFeatureType;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(AbstractProductType.class)
public class AbstractProductType_ { 

    public static volatile SingularAttribute<AbstractProductType, Money> price;
    public static volatile SingularAttribute<AbstractProductType, String> productIdentifier;
    public static volatile SetAttribute<AbstractProductType, ProductFeatureType> featureTypes;

}