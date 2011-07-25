package org.salespointframework.core.order;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.order.actions.OrderAction;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(OrderEntry.class)
public class OrderEntry_ { 

    public static volatile ListAttribute<OrderEntry, OrderAction> orderActions;
    public static volatile SingularAttribute<OrderEntry, Date> timeStamp;
    public static volatile SingularAttribute<OrderEntry, OrderStatus> status;
    public static volatile SingularAttribute<OrderEntry, Date> dateCreated;
    public static volatile SingularAttribute<OrderEntry, OrderIdentifier> orderIdentifier;
    public static volatile ListAttribute<OrderEntry, OrderLine> orderLines;
    public static volatile SingularAttribute<OrderEntry, String> salesChannel;
    public static volatile SingularAttribute<OrderEntry, String> termsAndConditions;

}