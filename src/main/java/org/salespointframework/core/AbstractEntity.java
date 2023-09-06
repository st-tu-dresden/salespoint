/*
 * Copyright 2017-2023 the original author or authors.
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
package org.salespointframework.core;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Base class for Salespoint entities to uniquely define {@link #equals(Object)} and {@link #hashCode()}.
 *
 * @author Oliver Gierke
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends Identifier> implements Persistable<ID> {

	private @Transient boolean isNew = true;

	/**
	 * Returns whether the entity has the given identifier.
	 *
	 * @param id must not be {@literal null}.
	 * @return
	 */
	public boolean hasId(ID id) {

		Assert.notNull(id, "Identifier must not be null!");

		return getId().equals(id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Marks the entity as not new not make sure we merge entity instances instead of trying to persist them.
	 */
	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !obj.getClass().equals(this.getClass())) {
			return false;
		}

		AbstractEntity<?> that = (AbstractEntity<?>) obj;

		return ObjectUtils.nullSafeEquals(this.getId(), that.getId());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
