package org.salespointframework.time;

import org.salespointframework.time.IntervalIntegrationTests.SomeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link SomeEntity}.
 *
 * @author Oliver Gierke
 */
public interface SomeEntityRepository extends CrudRepository<SomeEntity, Long> {}
