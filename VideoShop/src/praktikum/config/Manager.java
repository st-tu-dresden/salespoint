package praktikum.config;

import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.user.PersistentUserManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import videoshop.model.VideoCatalog;

@Configuration
public class Manager {

    @Bean
    public VideoCatalog getVideoCatalog() {
    	return new VideoCatalog();
    }
    
    @Bean
    public PersistentInventory gePersistentInventory() {
    	return new PersistentInventory();
    }
    
    @Bean
    public PersistentOrderManager getPersistentOrderManager() {
    	return new PersistentOrderManager();
    }
    
    @Bean
    public PersistentUserManager getPersistentUserManager() {
    	return new PersistentUserManager();
    }

}
