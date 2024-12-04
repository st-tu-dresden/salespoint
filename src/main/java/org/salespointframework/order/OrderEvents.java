/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.order;

import lombok.Value;

import org.jmolecules.event.types.DomainEvent;

/**
 * Events published by the order module.
 *
 * @author Oliver Drotbohm
 */
public class OrderEvents {

	/**
	 * Signals the processing of an {@link Order} having been completed, i.e. goods having been sent our, services
	 * delivered etc. A completed {@link Order} might still be canceled later on.
	 *
	 * @author Oliver Drotbohm
	 * @see OrderCanceled
	 */
	@Value(staticConstructor = "of")
	public static class OrderCompleted implements DomainEvent {

		Order order;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OrderCompleted";
		}
	}

	/**
	 * Signals an order having been paid. In other words, the step in an {@link Order}'s lifecycle in which we receive the
	 * customer's money for a particular order.
	 *
	 * @author Oliver Drotbohm
	 */
	@Value(staticConstructor = "of")
	public static class OrderPaid implements DomainEvent {

		Order order;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OrderPaid";
		}
	}

	/**
	 * Signals an {@link Order} being canceled. Likely to cause other modules in the system to take action to compensate
	 * for a previously handled {@link OrderCompleted}.
	 *
	 * @author Oliver Drotbohm
	 */
	@Value(staticConstructor = "of")
	public static class OrderCanceled implements DomainEvent {

		Order order;
		String reason;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OrderCanceled: " + reason;
		}
	}
}
