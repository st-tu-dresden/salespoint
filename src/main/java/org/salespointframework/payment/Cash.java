/*
 * Copyright 2017-2022 the original author or authors.
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
package org.salespointframework.payment;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * The cash {@link PaymentMethod} is used to designate all payments made in cash.
 * 
 * @author Hannes Weisbach
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class Cash extends PaymentMethod {

	private static final long serialVersionUID = 5087632169177352654L;

	/**
	 * A convenience instance {@link Cash#CASH} is defined in this class, which can be reused, instead of instantiating a
	 * new instance every time one is needed.
	 */
	public static final Cash CASH = new Cash();

	/**
	 * Creates a new cash instance. You can use {@link Cash#CASH} instead of instantiating a new one.
	 */
	private Cash() {
		super("Cash");
	}
}
