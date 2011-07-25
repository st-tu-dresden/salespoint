package org.salespointframework.core.calendar;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.salespointframework.core.calendar.CapabilityList;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2011-07-25T11:37:45")
@StaticMetamodel(AbstractCalendarEntry.class)
public abstract class AbstractCalendarEntry_ { 

    public static volatile SingularAttribute<AbstractCalendarEntry, Long> startTime;
    public static volatile SingularAttribute<AbstractCalendarEntry, String> title;
    public static volatile SingularAttribute<AbstractCalendarEntry, Integer> calendarEntryIdentifier;
    public static volatile SingularAttribute<AbstractCalendarEntry, Long> repeatStep;
    public static volatile SingularAttribute<AbstractCalendarEntry, String> description;
    public static volatile SingularAttribute<AbstractCalendarEntry, Integer> repeatCount;
    public static volatile MapAttribute<AbstractCalendarEntry, Void, CapabilityList> capabilities;
    public static volatile SingularAttribute<AbstractCalendarEntry, Long> endTime;

}