package videoshop;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.Capability;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Component;

import videoshop.model.BluRay;
import videoshop.model.Customer;
import videoshop.model.Disc;
import videoshop.model.Dvd;
import videoshop.model.VideoCatalog;


@Component
public class Main {

	public Main() {	
		Shop.INSTANCE.initializePersistent();
		initData();
	}

	private void initData() {
		PersistentUserManager userManager = new PersistentUserManager();
		UserIdentifier bossUI = new UserIdentifier("boss");
		
		if(userManager.contains(bossUI)) return;
		
		PersistentUser boss = new PersistentUser(bossUI, "123");
		boss.addCapability(new Capability("boss"));
		
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
		
		videoCatalog.add(new Dvd("Last Action Hero","lac",new Money(9.99), "Äktschn/Comedy"));
		videoCatalog.add(new Dvd("Back to the Future","bttf", new Money(9.99), "Sci-Fi"));
		videoCatalog.add(new Dvd("Fido", "fido",new Money(9.99), "Comedy/Drama/Horror"));
		videoCatalog.add(new Dvd("Super Fuzz", "sf", new Money(19.99), "Action/Sci-Fi/Comedy"));
		videoCatalog.add(new Dvd("Armour of God II: Operation Condor","aog2oc", new Money(14.99), "Action/Adventure/Comedy"));
		videoCatalog.add(new Dvd("Persepolis","pers", new Money(14.99), "Animation/Biography/Drama"));
		videoCatalog.add(new Dvd("Hot Shots! Part Deux","hspd", Money.OVER9000, "Action/Comedy/War"));
		videoCatalog.add(new Dvd("Avatar: The Last Airbender","tla", new Money(19.99), "Animation/Action/Adventure"));

		
		videoCatalog.add(new BluRay("The Godfather", "tg",new Money(19.99), "Crime/Drama"));
		videoCatalog.add(new BluRay("No Retreat, No Surrender", "nrns",new Money(29.99), "Martial Arts")); 
		videoCatalog.add(new BluRay("The Princess Bride", "tpb",new Money(39.99), "Adventure/Comedy/Family"));
		videoCatalog.add(new BluRay("Top Secret!","ts", new Money(39.99), "Comedy"));
		videoCatalog.add(new BluRay("The Iron Giant","tig", new Money(34.99), "Animation/Action/Adventure"));
		videoCatalog.add(new BluRay("Battle Royale","br", new Money(19.99), "Action/Drama/Thriller"));
		videoCatalog.add(new BluRay("Oldboy","old", new Money(24.99), "Action/Drama/Thriller"));
		videoCatalog.add(new BluRay("Bill & Ted's Excellent Adventure","bt", new Money(29.99), "Adventure/Comedy/Family"));
		
		PersistentInventory inventory = new PersistentInventory();
		
		for(Disc disc : videoCatalog.find(Disc.class)) {
			PersistentInventoryItem inventoryItem = new PersistentInventoryItem(disc, Units.TEN);
			inventory.add(inventoryItem);
		}
	}
}
