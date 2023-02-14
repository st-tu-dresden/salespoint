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
package org.salespointframework.time;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link Interval}
 *
 * @author Oliver Gierke
 */
@Transactional
@ApplicationModuleTest
class IntervalIntegrationTests {

	@Autowired SomeEntityRepository repository;
	@Autowired EntityManager em;

	@Test // #152
	void intervalCanBePersistedAsEmbeddable() {

		// Make sure we only use microseconds precision as H2 will only work
		// with that by default, the OS might run at a higher precision and we
		// would like to use ….equals(…) comparisons further below.
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);

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
		Interval interval;
	}
}
