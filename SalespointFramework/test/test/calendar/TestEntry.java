package test.calendar;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.salespointframework.core.calendar.AbstractCalendarEntry;

@Entity
public class TestEntry extends AbstractCalendarEntry {

    @Deprecated
    protected TestEntry() {
    }
    
    public TestEntry(String owner, String title, DateTime start, DateTime end) {
        super(owner, title, start, end);
    }
}
