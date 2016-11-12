package org.salespointframework.time;

import static org.assertj.core.api.Assertions.*;

import lombok.Data;

import java.time.LocalDateTime;

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

	/**
	 * @see #152
	 */
	@Test
	public void intervalCanBePersistedAsEmbeddable() {

		LocalDateTime now = LocalDateTime.now();

		SomeEntity entity = new SomeEntity();
		entity.interval = Interval.from(now).to(now.plusDays(1));

		repository.save(entity);
		em.flush();
		em.clear();

		SomeEntity result = repository.findOne(entity.id);
		assertThat(result.interval).isEqualTo(entity.interval);
	}

	@Data
	@Entity
	static class SomeEntity {

		@Id @GeneratedValue Long id;
		@Embedded Interval interval;
	}
}
