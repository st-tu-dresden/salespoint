package org.salespointframework.core.order;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.money.Money;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(ChargeLine.class)
public class ChargeLine_ { 

    public static volatile SingularAttribute<ChargeLine, Money> amount;
    public static volatile SingularAttribute<ChargeLine, String> description;
    public static volatile SingularAttribute<ChargeLine, String> comment;

}