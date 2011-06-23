package test.calendar;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.salespointframework.core.calendar.AbstractCalendarEntry;
import org.salespointframework.core.users.User;

@Entity
public class TestEntry extends AbstractCalendarEntry {

    @Deprecated
    protected TestEntry() {
    }
    
    public TestEntry(User owner, String title, DateTime start, DateTime end) throws IllegalArgumentException {
        super(owner, title, start, end);
    }
}
