package dvdshop;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.UserIdentifier;

//import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import dvdshop.model.Customer;
import dvdshop.model.CustomerManager;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Component
//@Order(value=0)
public class Main {

	public Main() {
		
		System.out.println("MAAAAAAAAAAAAAAAAIN");
		System.out.println("MAAAAAAAAAAAAAAAAIN");
		System.out.println("MAAAAAAAAAAAAAAAAIN");
		System.out.println("MAAAAAAAAAAAAAAAAIN");
		System.out.println("MAAAAAAAAAAAAAAAAIN");
		
		Database.INSTANCE.initializeEntityManagerFactory("DVDShop");

		Shop.initializeShop();
		
		initData();
	}

	private void initData() {
		CustomerManager customerManager = new CustomerManager();
		Customer customer = new Customer(new UserIdentifier("hans"), "wurst", "221 Baker Street London");
		customerManager.add(customer);

		VideoCatalog videoCatalog = new VideoCatalog();
		videoCatalog.add(new Dvd("Last Action Hero", Money.ZERO, "Action"));
		videoCatalog.add(new Dvd("Back to the Future", Money.ZERO, "Sci-Fi"));
	}
}
