package spielwiese.annotation;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;

@SuppressWarnings("javadoc")
public class EntityTest {

	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void add() {
		IdentifiableEntity ie = new IdentifiableEntity();

		if(ie == null) {
			System.out.println("IE is null");
			return;
		}
		
		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(ie);
		//em.persist(te);
		em.getTransaction().commit();

		if(ie.getIdentifier() == null) {
			System.out.println("Identifier is null");
			return;
		}
		TestEntity te = new TestEntity(ie.getIdentifier());
		long id;
		System.out.println("Auto Generated ID: " + ie.getIdentifier());

		/*
		id = te.getId();
		System.out.println("Loading TE with ID " + id);
		em.getTransaction().begin();
		te = em.find(TestEntity.class, id);
		em.getTransaction().commit();
		
		System.out.println("Looking for IE with ID: " + te.getIdentifier());
		em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		ie = em.find(IdentifiableEntity.class, te.getIdentifier());
		em.getTransaction().commit();
		if(ie != null)
			System.out.println("Found IE with ID: " + ie.getIdentifier());
		else
			System.out.println("IE with ID " + te.getIdentifier() + " not found");
*/
	}
	
	@Test
	public void multiple() {
		SpecialEntity se = new SpecialEntity();
		TestEntity te = new TestEntity(se.getIdentifier());
		long id;
		System.out.println("Generated ID: " + se.getIdentifier());
		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(se);
		em.persist(te);
		em.getTransaction().commit();
		
		id = te.getId();
		System.out.println("Loading TE with ID " + id);
		em.getTransaction().begin();
		te = em.find(TestEntity.class, id);
		em.getTransaction().commit();
		
		System.out.println("Looking for SE with ID: " + te.getSpecialIdentifier());
		em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		se = em.find(SpecialEntity.class, te.getSpecialIdentifier());
		em.getTransaction().commit();
		if(se != null)
			System.out.println("Found IE with ID: " + se.getIdentifier());
		else
			System.out.println("IE with ID " + te.getIdentifier() + " not found");
		
	}
	
}
