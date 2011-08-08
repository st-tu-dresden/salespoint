package test.calendar;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

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

    private static EntityManagerFactory      emf;

    private static final List<CalendarEntry> entries       = new ArrayList<CalendarEntry>();
    private static final DateTime            basicDateTime = new DateTime();

    @BeforeClass
    public static void setUp() {
        Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");

        emf = Database.INSTANCE.getEntityManagerFactory();

        EntityManager em = emf.createEntityManager();
        PersistentCalendar calendar = new PersistentCalendar(em);

        EntityTransaction t = em.getTransaction();
        t.begin();

        for (int i = 0; i < 10; i++) {
            PersistentCalendarEntry entry = new PersistentCalendarEntry(new Worker().getUserId(), "TestEntry_" + i, basicDateTime.plusMinutes(i * 10),
                            basicDateTime.plusMinutes(i * 10 + 5));
            entries.add(entry);
            calendar.addEntry(entry);
        }

        t.commit();
    }

    @Test
    public void testOwnership() {
        EntityManager em = emf.createEntityManager();
        PersistentCalendar calendar = new PersistentCalendar(em);

        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry expected = entries.get(i);
            PersistentCalendarEntry actual = calendar.getEntryByID(expected.getID());

            assertEquals(expected.getOwner(), actual.getOwner());
        }
    }

    @Test
    public void addUser() {
        EntityManager em = emf.createEntityManager();
        PersistentCalendar calendar = new PersistentCalendar(em);

        Worker newUser = new Worker();
        PersistentCalendarEntry expected_entry = calendar.getEntryByID(entries.get(0).getID());

        EntityTransaction t = em.getTransaction();
        t.begin();

        expected_entry.addCapability(newUser.getUserId(), CalendarEntryCapability.READ);

        t.commit();

        PersistentCalendarEntry actual_entry = calendar.getEntryByID(expected_entry.getID());

        assertEquals(expected_entry.getCapabilitiesByUser(newUser.getUserId()), actual_entry.getCapabilitiesByUser(newUser.getUserId()));
    }

    @Test
    public void filterEntries() {
        EntityManager em = emf.createEntityManager();
        PersistentCalendar calendar = new PersistentCalendar(em);

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