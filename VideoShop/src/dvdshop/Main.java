package dvdshop;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;

//import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import dvdshop.model.Comment;
import dvdshop.model.Customer;
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
		PersistentUserManager userManager = new PersistentUserManager();
		UserIdentifier chef = new UserIdentifier("chef");
		
		if(userManager.contains(chef)) return;
		
		userManager.add(new PersistentUser(chef, "chef"));
		
		Customer customer1 = new Customer(new UserIdentifier("hans"), "wurst", "221 Baker Street London");
		Customer customer2 = new Customer(new UserIdentifier("dexter"), "morgan", "");
		userManager.add(customer1);
		userManager.add(customer2);
		
		VideoCatalog videoCatalog = new VideoCatalog();
		Dvd lah = new Dvd("Last Action Hero", Money.ZERO, "Action");
		lah.addComment(new Comment("Test comment", 10));
		videoCatalog.add(lah);
		
		videoCatalog.add(new Dvd("Back to the Future", Money.ZERO, "Sci-Fi"));
	}
}
