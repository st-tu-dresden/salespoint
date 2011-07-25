package org.salespointframework.core.accountancy.payment;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(Cheque.class)
public class Cheque_ extends PaymentMethod_ {

    public static volatile SingularAttribute<Cheque, String> accountName;
    public static volatile SingularAttribute<Cheque, String> accountNumber;
    public static volatile SingularAttribute<Cheque, String> chequeNumber;
    public static volatile SingularAttribute<Cheque, DateTime> dateWritten;
    public static volatile SingularAttribute<Cheque, String> payee;
    public static volatile SingularAttribute<Cheque, String> bankName;
    public static volatile SingularAttribute<Cheque, String> bankIdentificationNumber;
    public static volatile SingularAttribute<Cheque, String> bankAddress;

}