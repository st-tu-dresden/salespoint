package org.salespointframework.core;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;
import org.springframework.util.ObjectUtils;

/**
 * Base class for Salespoint entities to uniquely define {@link #equals(Object)} and {@link #hashCode()}.
 *
 * @author Oliver Gierke
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends SalespointIdentifier> implements Persistable<ID> {

	private static final long serialVersionUID = -9081339328621393981L;

	private @Transient boolean isNew = true;

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
	@PostPersist
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

		if (obj == null || !(obj.getClass().equals(this.getClass()))) {
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
