/*
 * Copyright 2013 the original author or authors.
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
package org.salespointframework.core;

import org.springframework.util.ObjectUtils;

/**
 * Base class for Salespoint entities to uniquely define {@link #equals(Object)} and {@link #hashCode()}.
 *
 * @author Oliver Gierke
 */
public abstract class AbstractEntity<ID extends SalespointIdentifier> {

	/**
	 * Returns the {@link SalespointIdentifier}.
	 * 
	 * @return
	 */
	public abstract ID getIdentifier();

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (obj == null || !(obj.getClass().equals(this.getClass()))) {
			return false;
		}
		
		AbstractEntity<?> that = (AbstractEntity<?>) obj;
		
		return ObjectUtils.nullSafeEquals(this.getIdentifier(), that.getIdentifier());
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}
}
