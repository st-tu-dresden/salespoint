package org.salespointframework.order;

import java.util.Map;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Quantity;

/**
 * An {@code OrderCompletionResult} is returned after you call
 * {@link OrderManager#completeOrder(Order)}.
 *
 * @author Paul Henke
 */
public interface OrderCompletionResult {

    @SuppressWarnings("javadoc")
    public enum OrderCompletionStatus {

        SUCCESSFUL,
        FAILED,
        FAILED_PRODUCTS_MISSING,
        FAILED_PRODUCTS_UNDERSTOCKED,
        /* SPLITORDER */
    }

    /**
     * @return the OrderCompletionStatus of the {@link Order}
     */
    OrderCompletionStatus getStatus();

    /**
     * Returns all {@link Product}s, 
     * depending on the {@link OrderCompletionStatus}.
     *
     * <br/><br/>
     * 
     * <table>
     * <tr>
     * <th>OrderCompletionStatus</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>{@link OrderCompletionStatus#FAILED}</td>
     * <td>{@link Map} is empty</td>
     * </tr>
     * <tr>
     * <td>{@link OrderCompletionStatus#FAILED_PRODUCTS_MISSING}</td>
     * <td>Contains all {@link ProductIdentifier}s and {@link Quantity}, that is not found in the {@link Inventory}.</td>
     * </tr>
     * <tr>
     * <td>{@link OrderCompletionStatus#FAILED_PRODUCTS_UNDERSTOCKED}</td>
     * <td>Contains all {@link ProductIdentifier}s and {@link Quantity}, that is not sufficient in the {@link Inventory}. The total {@link Quantity}, not the diffenrence will be returned</td>
     * </tr>
     * <tr>
     * <td>{@link OrderCompletionStatus#SUCCESSFUL}</td>
     * <td>Contains all {@link ProductIdentifier}s and {@link Quantity}, that is successfully done by the {@link OrderManager#completeOrder(org.salespointframework.order.Order) }. Should be the same as the {@link Order#getOrderLines()} contains.</td>
     * </tr>
     * </table>
     *
     * @return an {@link Map} of {@link ProductIdentifier}, {@link Quantity}
     */
    Map<ProductIdentifier, Quantity> getProducts();

	// TODO später rollback, split
    /**
     * Call if you don't want a split order
     *
     * @return {@literal true} if rollback was successful, otherwise
     * {@literal false}
     */
	//
	/*
     boolean rollBack();
     */
    /**
     * Creates and returns a Splitorder
     *
     * @return a new {@link Order}
     */
    /*
     Order<OrderLine> splitOrder();
     */
}
