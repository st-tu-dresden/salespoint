package org.salespointframework.core.money;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.quantity.Metric_;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-18T11:45:00")
@StaticMetamodel(Currency.class)
public class Currency_ extends Metric_ {

    public static volatile SingularAttribute<Currency, Float> ratioOfMinorUnitToMajorUnit;
    public static volatile SingularAttribute<Currency, String> minorUnitSymbol;
    public static volatile SingularAttribute<Currency, String> alphabeticCode;
    public static volatile SingularAttribute<Currency, String> majorUnitSymbol;

}