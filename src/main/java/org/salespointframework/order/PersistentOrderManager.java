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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
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
     * {@link BusinessTime}, {@link OrderRepository} and
     * {@link PlatformTransactionManager}.
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
	 * @see org.salespointframework.order.OrderManager#findOrdersBetween(java.time.LocalDateTime, java.time.LocalDateTime)
	 */
	@Override
	public Iterable<T> findOrdersBetween(LocalDateTime from, LocalDateTime to) {
		
		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");
		Assert.isTrue(from.isBefore(to) || from.isEqual(to), "LocalDateTime 'from' must be before or equal to LocalDateTime 'to'");
		
		return orderRepository.findByDateCreatedBetween(from, to);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findOrdersByOrderStatus(org.salespointframework.order.OrderStatus)
	 */
	@Override
	public Iterable<T> findOrdersByOrderStatus(OrderStatus orderStatus) {
		
		Assert.notNull(orderStatus, "orderStatus must not be null");
		return orderRepository.findByOrderStatus(orderStatus);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findOrdersByUserAccount(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	public Iterable<T> findOrdersByUserAccount(UserAccount userAccount) {
		
		Assert.notNull(userAccount, "userAccount must not be null");
		return orderRepository.findByUserAccount(userAccount);
	}

    /*
     * (non-Javadoc)
     * @see org.salespointframework.order.OrderManager#findOrders(org.salespointframework.useraccount.UserAccount, java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Override
    public Iterable<T> findOrders(UserAccount userAccount, LocalDateTime from, LocalDateTime to) {

        Assert.notNull(userAccount, "userAccount must not be null");
        Assert.notNull(from, "from must not be null");
        Assert.notNull(to, "to must not be null");
        Assert.isTrue(from.isBefore(to) || from.isEqual(to), "time from must be before or equal to time to");

        return orderRepository.findByUserAccountAndDateCreatedBetween(userAccount, from, to);
    }

    /*
     * (non-Javadoc)
     * @see org.salespointframework.order.OrderManager#completeOrder(org.salespointframework.order.Order)
     */
    @Override
    public OrderCompletionResult completeOrder(final T order) {

        Assert.notNull(order, "Order must not be null");

        if (!order.isPaid()) {
            return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
        }

        final Map<InventoryItem, Quantity> goodItems = new HashMap<>();
        final Map<ProductIdentifier, Quantity> understockedItems = new HashMap<>();
        final Map<ProductIdentifier, Quantity> missingItems = new HashMap<>();

        for (OrderLine orderline : order.getOrderLines()) {

            final ProductIdentifier productIdentifier = orderline.getProductIdentifier();
            final Optional<InventoryItem> optItem = inventory.findByProductIdentifier(productIdentifier);
            final Quantity orderLineQuantity = orderline.getQuantity();

            if (false == optItem.isPresent()) {
                missingItems.put(productIdentifier, orderLineQuantity);
                LOGGER.error("No InventoryItem with given ProductIndentifier found in PersistentInventory. "
                        + "Have you initialized your PersistentInventory? Do you need to re-stock your Inventory?");

                continue;
            }

            final InventoryItem item = optItem.get();

            if (item.hasSufficientQuantity(orderLineQuantity)) {
                goodItems.put(item, orderLineQuantity);
            } else {
                understockedItems.put(productIdentifier, orderLineQuantity);
            }
        }

        if (false == missingItems.isEmpty()) {

            return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED_PRODUCTS_MISSING, missingItems);
        }

        if (false == understockedItems.isEmpty()) {

            return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED_PRODUCTS_UNDERSTOCKED, understockedItems);
        }

        return txTemplate.execute(new TransactionCallback<InternalOrderCompletionResult>() {

            @Override
            public InternalOrderCompletionResult doInTransaction(TransactionStatus ts) {

                for (InventoryItem inventoryItem : goodItems.keySet()) {

                    try {
                        inventoryItem.decreaseQuantity(goodItems.get(inventoryItem));
                        
                    } catch (IllegalArgumentException o_O) {
                        ts.setRollbackOnly();
                        return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
                    }
                }

                order.complete();
                save(order);
                
                Map<ProductIdentifier, Quantity> productResult = new HashMap<>(goodItems.size());
                goodItems.forEach((k,v) -> productResult.put(k.getProduct().getIdentifier(), v));
                
                return new InternalOrderCompletionResult(
                        OrderCompletionStatus.SUCCESSFUL,
                        productResult);
            }
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
        private final Map<ProductIdentifier, Quantity> failedProducts;

        public InternalOrderCompletionResult(OrderCompletionStatus status) {
            this.status = status;
            this.failedProducts = new HashMap<>();
        }

        public InternalOrderCompletionResult(OrderCompletionStatus status,
                Map<ProductIdentifier, Quantity> failedProducts) {
            this.status = status;
            this.failedProducts = failedProducts;
        }

        @Override
        public OrderCompletionStatus getStatus() {
            return status;
        }

        @Override
        public Map<ProductIdentifier, Quantity> getProducts() {
            return failedProducts;
        }
    }
}
