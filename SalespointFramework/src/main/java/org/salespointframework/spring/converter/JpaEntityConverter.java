/*
 * Copyright 2013-2014 the original author or authors.
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
package org.salespointframework.spring.converter;

import java.util.Collections;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link Converter} that can convert {@link String}s into JPA managed domain types. We expect the identifier types to
 * have a constructor that takes a {@link String} as arguments.
 * 
 * @author Oliver Gierke
 */
@Component
class JpaEntityConverter implements ConditionalGenericConverter {

	private final SalespointIdentifierConverter identifierConverter;
	private final EntityManager em;

	/**
	 * Creates a new {@link JpaEntityConverter} using the given {@link SalespointIdentifierConverter}.
	 * 
	 * @param identifierConverter must not be {@literal null}.
	 * @param em must not be {@literal null}.
	 */
	@Autowired
	public JpaEntityConverter(SalespointIdentifierConverter identifierConverter, EntityManager em) {
		
		Assert.notNull(identifierConverter, "Identifier converter must not be null!");
		Assert.notNull(em, "EntityManager must not be null!");
		
		this.identifierConverter = identifierConverter;
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Object.class));
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {

		try {
			return em.getMetamodel().entity(targetType.getType()) != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		EntityType<?> entityType = em.getMetamodel().entity(targetType.getType());
		Class<?> idType = entityType.getIdType().getJavaType();
		Object id = identifierConverter.convert(source, sourceType, TypeDescriptor.valueOf(idType));
		
		return em.find(targetType.getType(), id);
	}
}
