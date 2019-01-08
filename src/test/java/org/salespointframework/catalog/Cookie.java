/*
 * Copyright 2017-2019 the original author or authors.
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
package org.salespointframework.catalog;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

@Entity
public class Cookie extends Product {

	String property;

	@Deprecated
	Cookie() {}

	public Cookie(String name, MonetaryAmount price) {
		super(name, Money.of(9001, Currencies.EURO));
	}
}
