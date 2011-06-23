package test.calendar;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
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
        
        TestEntry entry = new TestEntry(new User() {}, "TestEntry", new DateTime(), new DateTime().plusHours(5));
        TestCalendar calendar = new TestCalendar(em);
        calendar.add(entry);
        assertEquals(entry,calendar.getEntryByID(entry.getID()));
    }
    
}