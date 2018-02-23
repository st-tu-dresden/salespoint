/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.Iterator;
import java.util.Optional;

import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;
import org.springframework.data.util.Streamable;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCompletionReport implements Streamable<OrderLineCompletion> {

	@NonNull @Getter Order order;
	@NonNull @Getter CompletionStatus status;
	@NonNull Streamable<OrderLineCompletion> completions;

	/**
	 * Creates an {@link OrderCompletionReport} representing a successful verification of the given {@link Order}.
	 * 
	 * @param order must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static OrderCompletionReport success(Order order) {

		return OrderCompletionReport.forCompletions(order,
				Streamable.of(() -> order.getOrderLines().stream().map(it -> OrderLineCompletion.success(it))));
	}

	/**
	 * Creates an {@link OrderCompletionReport} for an {@link Order} that's marked as failed.
	 * 
	 * @param order must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static OrderCompletionReport failed(Order order) {
		return new OrderCompletionReport(order, CompletionStatus.FAILED, Streamable.empty());
	}

	/**
	 * Creates a new {@link OrderCompletionReport} for the given {@link Order} and {@link OrderLineCompletion}s.
	 * 
	 * @param order must not be {@literal null}.
	 * @param completions must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static OrderCompletionReport forCompletions(Order order, Iterable<OrderLineCompletion> completions) {
		return forCompletions(order, Streamable.of(completions));
	}

	/**
	 * Creates a new {@link OrderCompletionReport} for the given {@link Order} and {@link OrderLineCompletion}s.
	 * 
	 * @param order must not be {@literal null}.
	 * @param completions must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	static OrderCompletionReport forCompletions(Order order, Streamable<OrderLineCompletion> completions) {

		return new OrderCompletionReport(order, completions.stream().anyMatch(OrderLineCompletion::isFailure) //
				? CompletionStatus.FAILED //
				: CompletionStatus.SUCCEEDED //
				, completions);
	}

	/**
	 * Returns whether there report contains any errors. If {@literal true}, clients should go ahead and inspect the
	 * report for {@link OrderLineCompletion} (e.g. using {@link #stream()} to access the individual completions).
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return completions.stream().anyMatch(OrderLineCompletion::isFailure);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<OrderLineCompletion> iterator() {
		return completions.iterator();
	}

	/**
	 * The completion status of an {@link OrderLine}.
	 *
	 * @author Oliver Gierke
	 */
	@Value
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	public static class OrderLineCompletion {

		@NonNull OrderLine orderLine;
		@NonNull CompletionStatus status;
		@NonNull Optional<String> message;

		/**
		 * Creates an {@link OrderLineCompletion} representing the successful completion of an {@link OrderLine}.
		 * 
		 * @param orderLine must not be {@literal null}.
		 * @return
		 */
		public static OrderLineCompletion success(OrderLine orderLine) {
			return new OrderLineCompletion(orderLine, CompletionStatus.SUCCEEDED, Optional.empty());
		}

		/**
		 * Creates an {@link OrderLineCompletion} representing an erroneous {@link OrderLine} alongside an error message.
		 * 
		 * @param orderLine must not be {@literal null}.
		 * @param message must not be {@literal null}.
		 * @return
		 */
		public static OrderLineCompletion error(OrderLine orderLine, String message) {
			return new OrderLineCompletion(orderLine, CompletionStatus.FAILED, Optional.of(message));
		}

		/**
		 * Returns whether the {@link OrderLineCompletion} represents a failure.
		 * 
		 * @return
		 */
		public boolean isFailure() {
			return CompletionStatus.FAILED.equals(status);
		}
	}

	/**
	 * The status of a completion.
	 *
	 * @author Oliver Gierke
	 */
	public static enum CompletionStatus {
		SUCCEEDED, FAILED;
	}
}
