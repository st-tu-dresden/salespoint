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
package org.salespointframework.quantity;

/**
 * @author Paul Henke
 */
@SuppressWarnings("serial")
public class MetricMismatchException extends RuntimeException {

	public MetricMismatchException(String text) {
		super(text);
	}

	public MetricMismatchException(Metric m1, Metric m2) {
		super("Metric 1 (" + m1.name() + ") does not match Metric 2 (" + m2.name() + ")");
	}

	public MetricMismatchException(String text, Metric m1, Metric m2) {
		super(text + "\n" + "Metric 1 (" + m1.name() + ") does not match Metric 2 (" + m2.name() + ")");
	}
}
