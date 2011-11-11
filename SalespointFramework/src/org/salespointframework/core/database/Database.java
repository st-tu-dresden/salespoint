package org.salespointframework.core.database;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.salespointframework.util.Objects;

/**
 * Delegates to
 * http://download.oracle.com/javaee/6/api/javax/persistence/Persistence
 * .html#createEntityManagerFactory(java.lang.String)
 * 
 * @author Paul Henke
 * 
 */
public enum Database {
	/**
	 * A singleton instance of the Database.
	 */
	INSTANCE;

	private EntityManagerFactory entityManagerFactory;

	/**
	 * Initialize an {@link EntityManagerFactory} for the named persistence unit.
	 * 
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @return <code>true</code> if the persistence unit exists,
	 *         <code>false</code> otherwise
	 */
	public boolean initializeEntityManagerFactory(String persistenceUnitName)
	{
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		return entityManagerFactory != null;
	}

	/**
	 * Initialize an {@link EntityManagerFactory} for the named persistence unit.
	 * 
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @param properties
	 *            Additional properties to use when creating the factory. The
	 *            values of these properties override any values that may have
	 *            been configured elsewhere
	 * @return <code>true</code> if the persistence unit exists,
	 *         <code>false</code> otherwise
	 */
	public boolean initializeEntityManagerFactory(String persistenceUnitName, @SuppressWarnings("rawtypes") Map properties)
	{
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		Objects.requireNonNull(properties, "properties");
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		return entityManagerFactory != null;
	}

	/**
	 * Returns the initialized {@link EntityManagerFactory}
	 * 
	 * @return The factory that creates {@link EntityManager}s configured according to
	 *         the specified persistence unit.
	 */
	public EntityManagerFactory getEntityManagerFactory()
	{
		if(entityManagerFactory == null) {
			throw new RuntimeException("Please initialize persistence unit first. For exampe by adding \"PersistenceUnitInitializer\" as bean in dispatch-servlet.xml");
		}
		return entityManagerFactory;
	}
}
