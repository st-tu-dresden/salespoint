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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import org.jmolecules.ddd.types.ValueObject;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or will be made.
 *
 * @author Hannes Weisbach
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class PaymentMethod implements Serializable, ValueObject {

	private static final long serialVersionUID = -3827889732758783955L;

	/**
	 * Description of the <code>PaymentMethod</code> in human-readable form. Is not {@literal null}.
	 */
	private final @NonNull String description;
}
