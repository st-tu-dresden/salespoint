package test.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.calendar.CalendarEntryCapability;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.AbstractEmployee;
import org.salespointframework.core.users.User;

public class CalendarTest {

    private EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
    
    @BeforeClass
    public static void setUp() {
        Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
    }
    
    @Test
    public void addEntry() {
        EntityManager em = emf.createEntityManager();
        TestEntry entry = new TestEntry(new Worker(), "TestEntry_1", new DateTime(), new DateTime().plusHours(5));
        TestCalendar calendar = new TestCalendar(em);
        calendar.addEntry(entry);
        
        assertEquals(entry,calendar.getEntryByID(entry.getID()));
    }
    
    @Test
    public void testOwner() {
        EntityManager em = emf.createEntityManager();
        Worker user = new Worker();
        TestEntry entry = new TestEntry(user, "TestEntry_2", new DateTime(), new DateTime().plusHours(5));
        TestCalendar calendar = new TestCalendar(em);
        calendar.addEntry(entry);
        
        assertEquals(user, calendar.getEntryByID(entry.getID()).getOwner());
    }
    
    @Test
    public void addCapability() {
        EntityManager em = emf.createEntityManager();
        
        Worker user_one = new Worker();
        Worker user_two = new Worker();
        
        TestEntry entry = new TestEntry(user_one, "TestEntry_2", new DateTime(), new DateTime().plusHours(5));
        TestCalendar calendar = new TestCalendar(em);
        calendar.addEntry(entry);
        
        
        calendar.getEntryByID(entry.getID()).addCapability(user_two, CalendarEntryCapability.READ);
        
        List<User> users = (List<User>) calendar.getEntryByID(entry.getID()).getUsersByCapability(CalendarEntryCapability.READ);
        
        assertTrue(users.contains(user_one) && users.contains(user_two));
    }
    
    @Test
    public void modifyEntry() {
        EntityManager em = emf.createEntityManager();
        
        TestEntry entry = new TestEntry(new Worker(), "TestEntry_2", new DateTime(), new DateTime().plusMinutes(180));
        TestCalendar calendar = new TestCalendar(em);
        calendar.addEntry(entry);
        
        assertEquals(entry,calendar.getEntryByID(entry.getID()));
        
        calendar.getEntryByID(entry.getID()).setTitle("New Title");
        
        assertEquals(calendar.getEntryByID(entry.getID()).getTitle(), "New Title");
    }
    
    
    
    class Worker extends AbstractEmployee {
        public Worker() {
            super("worker", "1");
        }
    }
}