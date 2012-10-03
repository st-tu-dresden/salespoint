package org.salespointframework.core.order;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

public class TransientOrderManager implements OrderManager<TransientOrder, TransientOrderLine> 
{
	private static final Map<OrderIdentifier, TransientOrder> orderMap = new ConcurrentHashMap<>();
	
	@Override
	public void add(TransientOrder order) 
	{
		Objects.requireNonNull(order, "order must be not null");
		orderMap.put(order.getIdentifier(), order);
	}

	@Override
	public <E extends TransientOrder> E get(Class<E> clazz, OrderIdentifier orderIdentifier) 
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier must not be null");
		return clazz.cast(orderMap.get(orderIdentifier));
	}

	@Override
	public boolean contains(OrderIdentifier orderIdentifier) 
	{
		Objects.requireNonNull(orderIdentifier, "orderIdentifier must not be null");
		return orderMap.containsKey(orderIdentifier);
	}
	
	@Override
	public <E extends TransientOrder> Iterable<E> find(Class<E> clazz, OrderStatus orderStatus) 
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(orderStatus, "orderStatus must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientOrder order : orderMap.values()) 
		{
			if(order.getClass().isInstance(clazz) && orderStatus == order.getOrderStatus())
			{
				temp.add(clazz.cast(order));
			}
		}
		return Iterables.of(temp);
	}


	@Override
	public <E extends TransientOrder> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to) {
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientOrder order : orderMap.values()) {
			if(order.getClass().isInstance(clazz))
			{
				if(order.getDateCreated().isAfter(from) && order.getDateCreated().isBefore(to)) {
					temp.add(clazz.cast(to));
				}
			}
		}
		return Iterables.of(temp);
		
	}

	@Override
	public <E extends TransientOrder> Iterable<E> find(Class<E> clazz,	UserIdentifier userIdentifier) {
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientOrder order : orderMap.values()) {
			if(order.getClass().isInstance(clazz) && userIdentifier == order.getUserIdentifier())
			{
				temp.add(clazz.cast(order));
			}
		}
		return Iterables.of(temp);
	}


	@Override
	public <E extends TransientOrder> Iterable<E> find(Class<E> clazz, UserIdentifier userIdentifier, DateTime from, DateTime to) 
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		Objects.requireNonNull(from, "from must not be null");
		Objects.requireNonNull(to, "to must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientOrder order : orderMap.values()) {
			if(order.getClass().isInstance(clazz) && userIdentifier == order.getUserIdentifier())
			{
				if(order.getDateCreated().isAfter(from) && order.getDateCreated().isBefore(to)) {
					temp.add(clazz.cast(order));
				}
			}
		}
		return Iterables.of(temp);
	}
}
