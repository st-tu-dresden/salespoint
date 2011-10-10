package dvdshop;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Component;

import dvdshop.model.BluRay;
import dvdshop.model.Comment;
import dvdshop.model.Customer;
import dvdshop.model.Disc;
import dvdshop.model.Dvd;
import dvdshop.model.VideoCatalog;

@Component
public class Main {

	public Main() {	
		Shop.initializeShop();
		
		initData();
	}

	private void initData() {
		PersistentUserManager userManager = new PersistentUserManager();
		UserIdentifier chef = new UserIdentifier("chef");
		
		if(userManager.contains(chef)) return;
		
		userManager.add(new PersistentUser(chef, "chef"));

		
		Customer customer1 = new Customer(new UserIdentifier("hans"), "a", "");
		Customer customer2 = new Customer(new UserIdentifier("dexter"), "morgan", "221 Baker Street London");
		userManager.add(customer1);
		userManager.add(customer2);
		
		VideoCatalog videoCatalog = new VideoCatalog();
		Dvd lah = new Dvd("Last Action Hero", Money.ZERO, "Action");
		lah.addComment(new Comment("Test comment", 10));
		videoCatalog.add(lah);
		
		videoCatalog.add(new Dvd("Back to the Future", Money.ZERO, "Sci-Fi"));
		videoCatalog.add(new BluRay("The Godfather", new Money(19.99), "Crime/Drama"));
		
		PersistentInventory inventory = new PersistentInventory();
		
		for(Disc disc : videoCatalog.find(Disc.class)) {
			for(int n = 0; n < 10; n++) {
				inventory.add(new PersistentProductInstance(disc));
			}
		}
		
		
	}
}
