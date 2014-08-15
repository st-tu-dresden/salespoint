package org.salespointframework.order;

import java.time.LocalDateTime;

import org.salespointframework.core.SalespointRepository;
import org.salespointframework.useraccount.UserAccount;

/**
 * Repository interface for {@link Order}s.
 *
 * @author Oliver Gierke
 */
interface OrderRepository<T extends Order> extends SalespointRepository<T, OrderIdentifier> {

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<T> findByDateCreatedBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * @param orderStatus
	 * @return
	 */
	Iterable<T> findByOrderStatus(OrderStatus orderStatus);

	/**
	 * @param userAccount
	 * @return
	 */
	Iterable<T> findByUserAccount(UserAccount userAccount);

	/**
	 * @param userAccount
	 * @param from
	 * @param to
	 * @return
	 */
	Iterable<T> findByUserAccountAndDateCreatedBetween(UserAccount userAccount, LocalDateTime from, LocalDateTime to);
}
