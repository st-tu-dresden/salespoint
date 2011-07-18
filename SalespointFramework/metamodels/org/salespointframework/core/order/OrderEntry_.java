package org.salespointframework.core.order;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderEntryStatus;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.order.actions.OrderAction;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(OrderEntry.class)
public class OrderEntry_ { 

    public static volatile ListAttribute<OrderEntry, OrderAction> orderActions;
    public static volatile SingularAttribute<OrderEntry, Date> timeStamp;
    public static volatile SingularAttribute<OrderEntry, OrderEntryStatus> status;
    public static volatile ListAttribute<OrderEntry, ChargeLine> chargeLines;
    public static volatile SingularAttribute<OrderEntry, Date> dateCreated;
    public static volatile SingularAttribute<OrderEntry, OrderIdentifier> orderIdentifier;
    public static volatile ListAttribute<OrderEntry, OrderLine> orderLines;
    public static volatile SingularAttribute<OrderEntry, String> salesChannel;
    public static volatile SingularAttribute<OrderEntry, String> termsAndConditions;

}