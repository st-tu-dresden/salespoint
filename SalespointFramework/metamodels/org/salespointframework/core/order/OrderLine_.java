package org.salespointframework.core.order;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderLineIdentifier;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(OrderLine.class)
public class OrderLine_ { 

    public static volatile SingularAttribute<OrderLine, String> description;
    public static volatile ListAttribute<OrderLine, ChargeLine> chargeLines;
    public static volatile SingularAttribute<OrderLine, Integer> numberOrdered;
    public static volatile SingularAttribute<OrderLine, Money> unitPrice;
    public static volatile SingularAttribute<OrderLine, String> comment;
    public static volatile SingularAttribute<OrderLine, OrderLineIdentifier> identifier;
    public static volatile SingularAttribute<OrderLine, Date> expectedDeliveryDate;

}