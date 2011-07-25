package org.salespointframework.core.order.actions;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.users.UserIdentifier;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(OrderAction.class)
public class OrderAction_ { 

    public static volatile SingularAttribute<OrderAction, Long> id;
    public static volatile SingularAttribute<OrderAction, Boolean> processed;
    public static volatile SingularAttribute<OrderAction, DateTime> dateAuthorized;
    public static volatile SingularAttribute<OrderAction, UserIdentifier> authorization;
    public static volatile SingularAttribute<OrderAction, OrderIdentifier> orderIdentifier;

}