package test.calendar;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.calendar.CalendarEntryCapability;
import org.salespointframework.core.calendar.DuplicatedOwnerExcepion;
import org.salespointframework.core.calendar.PersistentCalendar;
import org.salespointframework.core.calendar.PersistentCalendarEntry;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.AbstractEmployee;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Filter;

public class CalendarTest {
    private static final List<CalendarEntry> entries       = new ArrayList<CalendarEntry>();
    private static final DateTime            basicDateTime = new DateTime();

    @BeforeClass
    public static void setUp() {
        Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
    }

    @Test
    public void addEntry() {
        PersistentCalendar calendar = new PersistentCalendar();
        PersistentCalendarEntry entry = new PersistentCalendarEntry(new Worker().getUserIdentifier(), "addEntry", basicDateTime, basicDateTime.plusMinutes(30));
        calendar.addEntry(entry);
        
        assertEquals(entry, calendar.getEntryByID(entry.getCalendarEntryIdentifier()));
    }
    
    @Test(expected=DuplicatedOwnerExcepion.class)
    public void addSecondOwner() {
        PersistentCalendar calendar = new PersistentCalendar();
        PersistentCalendarEntry entry = new PersistentCalendarEntry(new Worker().getUserIdentifier(), "addSecondOwner", basicDateTime, basicDateTime.plusMinutes(15));
        calendar.addEntry(entry);
        entry.addCapability(new Worker().getUserIdentifier(), CalendarEntryCapability.OWNER);
    }
    
    @Test
    public void testOwnership() {
        PersistentCalendar calendar = new PersistentCalendar();

        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry expected = entries.get(i);
            PersistentCalendarEntry actual = calendar.getEntryByID(expected.getCalendarEntryIdentifier());

            assertEquals(expected.getOwner(), actual.getOwner());
        }
    }

    @Test
    public void addUser() {
        PersistentCalendar calendar = new PersistentCalendar();

        Worker w1 = new Worker();
        Worker w2 = new Worker();

        PersistentCalendarEntry entry = new PersistentCalendarEntry(w1.getUserIdentifier(), "addUser", basicDateTime, basicDateTime.plus(10));
        calendar.addEntry(entry);
        
        entry.addCapability(w2.getUserIdentifier(), CalendarEntryCapability.READ);

        calendar.getEntryByID(entry.getCalendarEntryIdentifier()).getUsersByCapability(CalendarEntryCapability.READ);

//        assertThat(w1, equalTo(w2));
    }

    @Test
    public void filterEntries() {
        PersistentCalendar calendar = new PersistentCalendar();
        PersistentCalendarEntry has = new PersistentCalendarEntry(new UserIdentifier(), "early", new DateTime().minusHours(1), new DateTime().plusHours(1));
        PersistentCalendarEntry hasnot = new PersistentCalendarEntry(new UserIdentifier(), "late", new DateTime().plusHours(1), new DateTime().plusHours(2)); 
        calendar.addEntry(has);
        calendar.addEntry(hasnot);
        
        Iterable<PersistentCalendarEntry> actual = calendar.getEntries(new Filter<PersistentCalendarEntry>() {
            @Override
            public Boolean invoke(PersistentCalendarEntry arg) {
                return arg.getStart().isBefore(basicDateTime.plusMinutes(10));
            }
        });

        List<PersistentCalendarEntry> expected = new ArrayList<PersistentCalendarEntry>() {
            private static final long serialVersionUID = 2308635111301759390L;

            {
                for (CalendarEntry entry : entries) {
                    if (entry.getStart().isBefore(basicDateTime.plusMinutes(10)))
                        add((PersistentCalendarEntry) entry);
                }
            }
        };

        assertThat(actual, hasItem(has));
        assertThat(actual, not(hasItem(hasnot)));
    }
}

@Entity
class Worker extends AbstractEmployee {
    public Worker() {
        super(new UserIdentifier(), "1");
    }
}