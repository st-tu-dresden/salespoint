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
package org.salespointframework.spring.converter;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Oliver Gierke
 */
@Component
class SalespointIdentifierConverter implements ConditionalGenericConverter {
	
	private static final TypeDescriptor STRING_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	private static final TypeDescriptor IDENTIFIER_DESCRIPTOR = TypeDescriptor.valueOf(SalespointIdentifier.class);

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, SalespointIdentifier.class));
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isAssignableTo(STRING_DESCRIPTOR) && targetType.isAssignableTo(IDENTIFIER_DESCRIPTOR);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
	
		Class<?> targetClass = targetType.getType();
		
		try {
			
			Constructor<?> constructor = targetClass.getConstructor(String.class);
			return BeanUtils.instantiateClass(constructor, source);

		} catch (NoSuchMethodException | SecurityException e) {
			throw new ConversionFailedException(TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetClass), source, e);
		}
	}
}
