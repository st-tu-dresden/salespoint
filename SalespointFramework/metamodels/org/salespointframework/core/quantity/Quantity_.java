package org.salespointframework.core.quantity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.rounding.AbstractRoundingStrategy;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(Quantity.class)
public class Quantity_ { 

    public static volatile SingularAttribute<Quantity, BigDecimal> amount;
    public static volatile SingularAttribute<Quantity, Long> id;
    public static volatile SingularAttribute<Quantity, Metric> metric;
    public static volatile SingularAttribute<Quantity, AbstractRoundingStrategy> roundingStrategy;

}