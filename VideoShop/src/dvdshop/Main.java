package dvdshop;

import javax.persistence.EntityManager;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.UserIdentifier;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import dvdshop.model.Customer;
import dvdshop.model.CustomerManager;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Component
@Order(value=0)
public class Main {

	public Main() {
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");

		init();
		//initData();
	}

	private void init() {

		EntityManager em = Database.INSTANCE.getEntityManagerFactory()
				.createEntityManager();

	}

	private void initData() {
		EntityManager em = Database.INSTANCE.getEntityManagerFactory()
				.createEntityManager();

		Shop.INSTANCE.setOrderManager(new PersistentOrderManager());
		
		CustomerManager cm = new CustomerManager();
		Shop.INSTANCE.setUserManager(cm);

		Customer cus = new Customer(new UserIdentifier("hans"), "wurst",
				"221 Baker Street London");
		cm.add(cus);

		VideoCatalog dc = new VideoCatalog();
		Dvd dvd1 = new Dvd("Last Action Hero", Money.ZERO, "Action");
		Dvd dvd2 = new Dvd("Back to the Future", Money.ZERO, "Sci-Fi");
		dc.add(dvd1);
		dc.add(dvd2);

		em.getTransaction().begin();
		em.getTransaction().commit();

		System.out.println("______________________");
		System.out.println("______________________");
		System.out.println("______________________");
		System.out.println("______________________");
		System.out.println("______________________");
		System.out.println("______________________");
		System.out.println("______________________");

	}
}
