package org.salespointframework.inventory;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

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
}
