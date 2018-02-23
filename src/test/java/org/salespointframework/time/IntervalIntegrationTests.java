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

import static org.assertj.core.api.Assertions.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link Interval}
 * 
 * @author Oliver Gierke
 */
public class IntervalIntegrationTests extends AbstractIntegrationTests {

	@Autowired SomeEntityRepository repository;
	@Autowired EntityManager em;

	@Test // #152
	public void intervalCanBePersistedAsEmbeddable() {

		LocalDateTime now = LocalDateTime.now();

		SomeEntity entity = new SomeEntity();
		entity.interval = Interval.from(now).to(now.plusDays(1));

		repository.save(entity);
		em.flush();
		em.clear();

		Optional<SomeEntity> result = repository.findById(entity.id);
		assertThat(result).map(it -> it.interval).hasValue(entity.interval);
	}

	@Data
	@Entity
	static class SomeEntity {

		@Id @GeneratedValue Long id;
		@Embedded Interval interval;
	}
}
