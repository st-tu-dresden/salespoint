package test.calendar;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.calendar.CalendarEntryCapability;
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
//        PersistentCalendar calendar = new PersistentCalendar();
//
//        for (int i = 0; i < 10; i++) {
//            PersistentCalendarEntry entry = new PersistentCalendarEntry(new Worker().getUserIdentifier(), "TestEntry_" + i, basicDateTime.plusMinutes(i * 10),
//                            basicDateTime.plusMinutes(i * 10 + 5));
//            entries.add(entry);
//            calendar.addEntry(entry);
//        }
    }

    @Test
    public void addEntry() {
        PersistentCalendar calendar = new PersistentCalendar();
        PersistentCalendarEntry entry = new PersistentCalendarEntry(new Worker().getUserIdentifier(), "Test", basicDateTime, basicDateTime.plusMinutes(30));
        calendar.addEntry(entry);
        
        assertEquals(entry, calendar.getEntryByID(entry.getCalendarEntryIdentifier()));
    }
    
//    @Test
    public void testOwnership() {
        PersistentCalendar calendar = new PersistentCalendar();

        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry expected = entries.get(i);
            PersistentCalendarEntry actual = calendar.getEntryByID(expected.getCalendarEntryIdentifier());

            assertEquals(expected.getOwner(), actual.getOwner());
        }
    }

//    @Test
    public void addUser() {
        PersistentCalendar calendar = new PersistentCalendar();

        Worker newUser = new Worker();
        PersistentCalendarEntry expected_entry = calendar.getEntryByID(entries.get(0).getCalendarEntryIdentifier());
        expected_entry.addCapability(newUser.getUserIdentifier(), CalendarEntryCapability.READ);
        PersistentCalendarEntry actual_entry = calendar.getEntryByID(expected_entry.getCalendarEntryIdentifier());

        assertEquals(expected_entry.getCapabilitiesByUser(newUser.getUserIdentifier()), actual_entry.getCapabilitiesByUser(newUser.getUserIdentifier()));
    }

//    @Test
    public void filterEntries() {
        PersistentCalendar calendar = new PersistentCalendar();

        Iterable<PersistentCalendarEntry> actual = calendar.getEntries(new Filter<PersistentCalendarEntry>() {
            @SuppressWarnings("boxing")
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

        assertEquals(expected, actual);
    }
}

@Entity
class Worker extends AbstractEmployee {
    public Worker() {
        super(new UserIdentifier("worker"), "1");
    }
}