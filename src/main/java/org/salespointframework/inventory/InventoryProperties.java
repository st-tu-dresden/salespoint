package org.salespointframework.inventory;

import lombok.Value;

import org.salespointframework.quantity.Quantity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.Nullable;

/**
 * Properties to configure Salespoint's inventory. Declare {@code salespoint.inventory.…} in application properties to
 * tweak settings.
 *
 * @author Martin Morgenstern
 * @author Oliver Drotbohm
 * @since 7.2.2
 */
@Value
@ConstructorBinding
@ConfigurationProperties("salespoint.inventory")
class InventoryProperties {

	/**
	 * Disable inventory updates completely, defaults to {@literal false}.
	 */
	private boolean disableUpdates;

	/**
	 * The threshold at which a {@link InventoryEvents.StockShort} is supposed to be triggered during inventory updates.
	 */
	private Quantity restockThreshold;

	InventoryProperties(boolean disableUpdates, @Nullable Quantity restockThreshold) {

		this.disableUpdates = disableUpdates;
		this.restockThreshold = restockThreshold == null ? Quantity.NONE : restockThreshold;
	}
}
