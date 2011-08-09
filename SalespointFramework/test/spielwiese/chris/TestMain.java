package spielwiese.chris;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class TestMain {

	
	public static void main(String[] args) {

		CapaListTest t= new CapaListTest("firstCapalist");
		CapaListTest t2= new CapaListTest("secondCapalist");
		EntityManager em = Persistence
                .createEntityManagerFactory("SalespointFramework")
                .createEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.persist(t2);
        em.getTransaction().commit();
		
		
		
		TestCapas tc=new TestCapas("Capa_toll");
		TestCapas tc2=new TestCapas("Capa_mist");
		TestCapas tc3=new TestCapas("Capa_dumm");
		TestCapas tc4=new TestCapas("Capa_bla");
		em.getTransaction().begin();
		t.addCapa(tc);
		t.addCapa(tc2);
		t.addCapa(tc3);
		em.getTransaction().commit();
		em.getTransaction().begin();
		t2.addCapa(tc3);
		t2.addCapa(tc4);
		em.getTransaction().commit();
		
		
		System.out.println("done");
	}

}
