package test.calendar;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.internal.jpa.metadata.structures.ArrayAccessor;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.calendar.PersistentCalendar;
import org.salespointframework.core.calendar.PersistentCalendarEntry;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.PersistentUser;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Filter;

@SuppressWarnings("javadoc")
public class CalendarTest {
    private static final DateTime          basicDateTime = new DateTime();

    private static PersistentUser          user;
    private static PersistentUser          notUser;
    private static PersistentCalendar      calendar;
    private static PersistentCalendarEntry entry;
    private static PersistentCalendarEntry notEntry;

    @BeforeClass
    public static void setUp() {
        Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");

        calendar = new PersistentCalendar();
        
        user = new PersistentUser(new UserIdentifier("user"), "test");
        entry = new PersistentCalendarEntry(user.getUserIdentifier(), "entry", basicDateTime, basicDateTime.plusMinutes(30));
        calendar.add(entry);
        
        notUser = new PersistentUser(new UserIdentifier("notUser"), "test");
        notEntry = new PersistentCalendarEntry(notUser.getUserIdentifier(), "notEntry", basicDateTime, basicDateTime.plusMinutes(30));
        calendar.add(notEntry);
    }

    @Test
    public void deleteEntry() {
        PersistentCalendarEntry deleteEntry = new PersistentCalendarEntry(new UserIdentifier(), "deleteEntry", basicDateTime, basicDateTime.plusMinutes(10));
        calendar.add(deleteEntry);
        
        assumeThat(calendar.find(deleteEntry.getCalendarEntryIdentifier()), is(deleteEntry));
        
        calendar.remove(deleteEntry.getCalendarEntryIdentifier());
        
        assertNull(calendar.find(deleteEntry.getCalendarEntryIdentifier()));
    }
    
    @Test
    public void getEntriesByTitle() {
        Iterable<PersistentCalendarEntry> actual = calendar.findByTitle(entry.getTitle()); 
        assertThat(actual, hasItem(entry));
        assertThat(actual, not(hasItem(notEntry)));
    }

    @Test
    public void getEntriesByOwner() {
        Iterable<PersistentCalendarEntry> actual = calendar.findByUserIdentifier(user.getUserIdentifier()); 
        assertThat(actual, hasItem(entry));
        assertThat(actual, not(hasItem(notEntry)));
    }

    @Test
    public void filterEntries() {
        PersistentCalendar calendar = new PersistentCalendar();
        PersistentCalendarEntry has = new PersistentCalendarEntry(new UserIdentifier(), "early", new DateTime().minusHours(1), new DateTime().plusHours(1));
        PersistentCalendarEntry hasnot = new PersistentCalendarEntry(new UserIdentifier(), "late", new DateTime().plusHours(1), new DateTime().plusHours(2)); 
        calendar.add(has);
        calendar.add(hasnot);
        
        List<PersistentCalendarEntry> actual = new ArrayList<PersistentCalendarEntry>();
        for (PersistentCalendarEntry entry : calendar.getAllEntries()) {
            if (entry.getStart().isBefore(basicDateTime.plusMinutes(10)))
                actual.add(entry);
        }

        assertThat(actual, hasItem(has));
        assertThat(actual, not(hasItem(hasnot)));
    }
}