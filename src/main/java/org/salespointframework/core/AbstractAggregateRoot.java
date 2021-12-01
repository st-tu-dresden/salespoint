/*
 * Copyright 2020 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.MappedSuperclass;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

/**
 * Base class for Aggregate root entities. It exposes a {@link #registerEvent(Object)} method that allows sub-classes to
 * register events to be published as soon as the aggregates are persisted using calls to
 * {@link CrudRepository#save(Object)}.
 *
 * @author Oliver Drotbohm
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.domain-events">Publishing
 *      domain events from aggregate roots</a>
 * @since 7.3
 * @soundtrack Dave Matthews Band - #41 (Live at Hollywood Bowl)
 */
@MappedSuperclass
public abstract class AbstractAggregateRoot<ID extends Identifier> extends AbstractEntity<ID> {

	private transient final Collection<Object> events = new ArrayList<>();

	/**
	 * Registers the given event with the aggregate root for publication on persisting, i.e.
	 * {@link CrudRepository#save(Object)}.
	 *
	 * @param <T> the type of the event
	 * @param event must not be {@literal null}.
	 * @return
	 */
	protected final <T> T registerEvent(T event) {

		Assert.notNull(event, "Event must not be null!");

		this.events.add(event);

		return event;
	}

	/**
	 * Exposes the events to the publication infrastructure.
	 *
	 * @return the events to be published.
	 */
	@DomainEvents
	Collection<Object> getEvents() {
		return Collections.unmodifiableCollection(events);
	}

	/**
	 * Resets the events after publication.
	 */
	@AfterDomainEventPublication
	void wipeEvents() {
		this.events.clear();
	}
}
