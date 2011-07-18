package org.salespointframework.core.order;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderLineIdentifier;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(ChargeLine.class)
public class ChargeLine_ { 

    public static volatile SingularAttribute<ChargeLine, Money> amount;
    public static volatile SingularAttribute<ChargeLine, Long> id;
    public static volatile SingularAttribute<ChargeLine, String> description;
    public static volatile SingularAttribute<ChargeLine, String> comment;
    public static volatile SingularAttribute<ChargeLine, OrderLineIdentifier> identifier;

}