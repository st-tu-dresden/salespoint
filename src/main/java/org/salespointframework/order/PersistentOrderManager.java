package org.salespointframework.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
@Transactional
class PersistentOrderManager<T extends Order> implements OrderManager<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistentOrderManager.class);

	private final Inventory<InventoryItem> inventory;
	private final TransactionTemplate txTemplate;
	private final Accountancy<AccountancyEntry> accountancy;
	private final BusinessTime businessTime;
	private final OrderRepository<T> orderRepository;

	/**
	 * Creates a new {@link PersistentOrderManager} using the given {@link Inventory}, {@link Accountancy}
	 * {@link BusinessTime}, {@link OrderRepository} and {@link PlatformTransactionManager}.
	 * 
	 * @param inventory must not be {@literal null}.
	 * @param accountancy must not be {@literal null}.
	 * @param businessTime must not be {@literal null}.
	 * @param orderRepository must not be {@literal null}.
	 * @param txManager must not be {@literal null}.
	 */
	@Autowired
	public PersistentOrderManager(Inventory<InventoryItem> inventory, Accountancy<AccountancyEntry> accountancy,
			BusinessTime businessTime, OrderRepository<T> orderRepository, PlatformTransactionManager txManager) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(accountancy, "Accountancy must not be null!");
		Assert.notNull(businessTime, "BusinessTime must not be null!");
		Assert.notNull(orderRepository, "OrderRepository must not be null!");
		Assert.notNull(txManager, "PlatformTransactionManager must not be null!");

		this.inventory = inventory;
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.orderRepository = orderRepository;
		this.txTemplate = new TransactionTemplate(txManager);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#add(org.salespointframework.order.Order)
	 */
	@Override
	public T add(T order) {
		return save(order);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#save(org.salespointframework.order.Order)
	 */
	@Override
	public T save(T order) {

		Assert.notNull(order, "order must be not null");

		if (order.getDateCreated() == null) {
			order.setDateCreated(businessTime.getTime());
		}

		return orderRepository.save(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#get(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public final Optional<T> get(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "orderIdentifier must not be null");
		return orderRepository.findOne(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#contains(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public final boolean contains(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "orderIdentifier must not be null");
		return orderRepository.exists(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#find(java.time.LocalDateTime, java.time.LocalDateTime)
	 */
	@Override
	public final Iterable<T> find(LocalDateTime from, LocalDateTime to) {

		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");

		return orderRepository.findByDateCreatedBetween(from, to);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#find(org.salespointframework.order.OrderStatus)
	 */
	@Override
	public final Iterable<T> find(OrderStatus orderStatus) {

		Assert.notNull(orderStatus, "orderStatus must not be null");
		return orderRepository.findByOrderStatus(orderStatus);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#find(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	public final Iterable<T> find(UserAccount userAccount) {

		Assert.notNull(userAccount, "userAccount must not be null");
		return orderRepository.findByUserAccount(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#find(org.salespointframework.useraccount.UserAccount, java.time.LocalDateTime, java.time.LocalDateTime)
	 */
	@Override
	public final Iterable<T> find(UserAccount userAccount, LocalDateTime from, LocalDateTime to) {

		Assert.notNull(userAccount, "userAccount must not be null");
		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");

		return orderRepository.findByUserAccountAndDateCreatedBetween(userAccount, from, to);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#update(org.salespointframework.order.Order)
	 */
	@Override
	public final void update(T order) {
		save(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#completeOrder(org.salespointframework.order.Order)
	 */
	@Override
	public OrderCompletionResult completeOrder(final Order order) {

		if (!order.isPaid()) {
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
		}

		Assert.notNull(order, "Order must not be null");

		final Map<InventoryItem, Quantity> goodItems = new HashMap<>();
		final Map<InventoryItem, Quantity> badItems = new HashMap<>();

		Iterable<OrderLine> lineItems = order.getOrderLines();

		// Stream<OrderLine> stream = stream(lineItems.spliterator(), false);
		// Stream<ProductIdentifier> identifiers = stream.map(OrderLine::getProductIdentifier);
		// Stream<Optional<InventoryItem>> items = identifiers.map(identifier ->
		// inventory.findByProductProductIdentifier(identifier));

		// Stream<InventoryItem> filtereditems = items.flatMap(i -> i.map(Stream::of).orElseGet(Stream::empty));

		for (OrderLine orderline : lineItems) {

			ProductIdentifier productIdentifier = orderline.getProductIdentifier();
			Optional<InventoryItem> inventoryItem = inventory.findByProductIdentifier(productIdentifier);

			// TODO was machen wenn nicht im Inventar
			if (!inventoryItem.isPresent()) {
				LOGGER
						.error("No InventoryItem with given ProductIndentifier found in PersistentInventory. Have you initialized your PersistentInventory? Do you need to re-stock your Inventory?");
				break;
			}

			inventoryItem.ifPresent(item -> {

				Quantity orderLineQuantity = orderline.getQuantity();

				if (item.hasSufficientQuantity(orderLineQuantity)) {
					goodItems.put(item, orderLineQuantity);
				} else {
					badItems.put(item, orderLineQuantity);
				}
			});
		}

		return txTemplate
				.execute(status -> {

					if (goodItems.size() != order.getNumberOfLineItems()) {

						status.setRollbackOnly();

						LOGGER
								.error("Number of items requested by the OrderLine is greater than the number available in the Inventory. Please re-stock.");
						return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
					}

					LOGGER.info("Number of items requested by the OrderLine removed from the Inventory.");

					boolean failed = false;

					for (InventoryItem inventoryItem : goodItems.keySet()) {

						try {
							inventoryItem.decreaseQuantity(goodItems.get(inventoryItem));
						} catch (IllegalArgumentException o_O) {
							failed = true;
							break;
						}
					}

					// TODO DRY IT
					if (failed) {

						status.setRollbackOnly();
						return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
					}

					order.complete();
					return new InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL);
				});
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#pay(org.salespointframework.order.Order)
	 */
	@Override
	public boolean payOrder(Order order) {

		Assert.notNull(order, "order must not be null");

		if (!order.isPaymentExpected()) {
			return false;
		}

		accountancy.add(order.markPaid());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#cancelOrder(org.salespointframework.order.Order)
	 */
	@Override
	public boolean cancelOrder(Order order) {

		Assert.notNull(order, "order must not be null");

		if (order.getOrderStatus() == OrderStatus.OPEN) {
			order.cancel();
			return true;
		} else {
			return false;
		}
	}

	private final class InternalOrderCompletionResult implements OrderCompletionResult {
		private final OrderCompletionStatus status;

		public InternalOrderCompletionResult(OrderCompletionStatus status) {
			this.status = status;
		}

		@Override
		public OrderCompletionStatus getStatus() {
			return status;
		}
	}
}
