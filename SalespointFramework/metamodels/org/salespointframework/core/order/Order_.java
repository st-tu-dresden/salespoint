package org.salespointframework.core.order;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.actions.OrderAction;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(Order.class)
public class Order_ { 

    public static volatile ListAttribute<Order, OrderAction> orderActions;
    public static volatile SingularAttribute<Order, Date> timeStamp;
    public static volatile SingularAttribute<Order, OrderStatus> status;
    public static volatile ListAttribute<Order, ChargeLine> chargeLines;
    public static volatile SingularAttribute<Order, Date> dateCreated;
    public static volatile SingularAttribute<Order, OrderIdentifier> orderIdentifier;
    public static volatile ListAttribute<Order, OrderLine> orderLines;
    public static volatile SingularAttribute<Order, String> salesChannel;
    public static volatile SingularAttribute<Order, String> termsAndConditions;

}