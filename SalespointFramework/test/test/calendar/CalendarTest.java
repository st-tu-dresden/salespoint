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
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.AbstractEmployee;
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
        TestCalendar calendar = new TestCalendar(em);

        EntityTransaction t = em.getTransaction();
        t.begin();

        for (int i = 0; i < 10; i++) {
            TestEntry entry = new TestEntry(new Worker().getUserId(), "TestEntry_" + i, basicDateTime.plusMinutes(i * 10),
                            basicDateTime.plusMinutes(i * 10 + 5));
            entries.add(entry);
            calendar.addEntry(entry);
        }

        t.commit();
    }

    @Test
    public void testOwnership() {
        EntityManager em = emf.createEntityManager();
        TestCalendar calendar = new TestCalendar(em);

        for (int i = 0; i < entries.size(); i++) {
            CalendarEntry expected = entries.get(i);
            TestEntry actual = calendar.getEntryByID(expected.getID());

            assertEquals(expected.getOwner(), actual.getOwner());
        }
    }

    @Test
    public void addUser() {
        EntityManager em = emf.createEntityManager();
        TestCalendar calendar = new TestCalendar(em);

        Worker newUser = new Worker();
        TestEntry expected_entry = calendar.getEntryByID(entries.get(0).getID());

        EntityTransaction t = em.getTransaction();
        t.begin();

        expected_entry.addCapability(newUser.getUserId(), CalendarEntryCapability.READ);

        t.commit();

        TestEntry actual_entry = calendar.getEntryByID(expected_entry.getID());

        assertEquals(expected_entry.getCapabilitiesByUser(newUser.getUserId()), actual_entry.getCapabilitiesByUser(newUser.getUserId()));
    }

    @Test
    public void filterEntries() {
        EntityManager em = emf.createEntityManager();
        TestCalendar calendar = new TestCalendar(em);

        Iterable<TestEntry> actual = calendar.getEntries(new Filter<TestEntry>() {
            @SuppressWarnings("boxing")
            @Override
            public Boolean invoke(TestEntry arg) {
                return arg.getStart().isBefore(basicDateTime.plusMinutes(10));
            }
        });

        List<TestEntry> expected = new ArrayList<TestEntry>() {
            private static final long serialVersionUID = 2308635111301759390L;

            {
                for (CalendarEntry entry : entries) {
                    if (entry.getStart().isBefore(basicDateTime.plusMinutes(10)))
                        add((TestEntry) entry);
                }
            }
        };

        assertEquals(expected, actual);
    }
}

@Entity
class Worker extends AbstractEmployee {
    public Worker() {
        super("worker", "1");
    }
}