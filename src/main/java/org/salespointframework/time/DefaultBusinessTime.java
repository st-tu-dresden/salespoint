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
package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * A mutable implementation of {@link BusinessTime} to record {@link Duration}s to calculate the current business time
 * by accumulating them.
 * 
 * @author Oliver Gierke
 */
@Service
class DefaultBusinessTime implements BusinessTime {

	private Duration duration = Duration.ZERO;

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#getTime()
	 */
	@Override
	public LocalDateTime getTime() {
		return LocalDateTime.now().plus(duration);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#forward(java.time.Duration)
	 */
	@Override
	public void forward(Duration duration) {
		this.duration = this.duration.plus(duration);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#getOffset()
	 */
	@Override
	public Duration getOffset() {
		return this.duration;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#reset()
	 */
	@Override
	public void reset() {
		this.duration = Duration.ZERO;
	}
}
