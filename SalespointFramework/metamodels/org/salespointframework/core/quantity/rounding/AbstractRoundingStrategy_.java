package org.salespointframework.core.quantity.rounding;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(AbstractRoundingStrategy.class)
public class AbstractRoundingStrategy_ { 

    public static volatile SingularAttribute<AbstractRoundingStrategy, Long> id;
    public static volatile SingularAttribute<AbstractRoundingStrategy, Integer> roundingDigit;
    public static volatile SingularAttribute<AbstractRoundingStrategy, Integer> numberOfDigits;
    public static volatile SingularAttribute<AbstractRoundingStrategy, Integer> roundingStep;

}