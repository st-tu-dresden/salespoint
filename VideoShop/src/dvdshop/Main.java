package dvdshop;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserCapability;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Component;

import dvdshop.model.BluRay;
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
		UserIdentifier bossUI = new UserIdentifier("boss");
		
		if(userManager.contains(bossUI)) return;
		
		PersistentUser boss = new PersistentUser(bossUI, "123");
		boss.addCapability(new UserCapability("boss"));
		
		Customer customer1 = new Customer(new UserIdentifier("hans"), "wurst", "");
		Customer customer2 = new Customer(new UserIdentifier("dexter"), "morgan", "Miami-Dade County");
		Customer customer3 = new Customer(new UserIdentifier("earl"), "hickey", "Camden County - Motel");
		Customer customer4 = new Customer(new UserIdentifier("mclovin"), "fogell", "Los Angeles");
		
		userManager.add(boss);
		userManager.add(customer1);
		userManager.add(customer2);
		userManager.add(customer3);
		userManager.add(customer4);
		
		VideoCatalog videoCatalog = new VideoCatalog();
		
		videoCatalog.add(new Dvd("Last Action Hero","",new Money(9.99), "Äktschn/Comedy"));
		videoCatalog.add(new Dvd("Back to the Future","", new Money(9.99), "Sci-Fi"));
		videoCatalog.add(new Dvd("Fido", "",new Money(9.99), "Comedy/Drama/Horror"));

		
		videoCatalog.add(new BluRay("The Godfather", "",new Money(19.99), "Crime/Drama"));
		videoCatalog.add(new BluRay("No Retreat, No Surrender", "",new Money(29.99), "Martial Arts")); 
		videoCatalog.add(new BluRay("The Princess Bride", "",new Money(39.99), "Adventure/Comedy/Family")); 
		
		PersistentInventory inventory = new PersistentInventory();
		
		for(Disc disc : videoCatalog.find(Disc.class)) {
			for(int n = 0; n < 15; n++) {
				inventory.add(new PersistentProductInstance(disc));
			}
		}
	}
}
