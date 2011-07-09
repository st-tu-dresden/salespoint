package test.calendar;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.users.AbstractEmployee;

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
        
//        entry.addCapability(new Worker(), CalendarEntryCapability.OWNER);
        
        assertEquals(entry,calendar.getEntryByID(entry.getID()));
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

		@Override
		public void changeSalary(Money salary) {
			// TODO Auto-generated method stub
			
		}        
    }
}