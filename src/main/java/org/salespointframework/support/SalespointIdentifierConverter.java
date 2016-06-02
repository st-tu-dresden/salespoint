package org.salespointframework.support;

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
	public SalespointIdentifier convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		Class<?> targetClass = targetType.getType();

		try {

			Constructor<?> constructor = targetClass.getDeclaredConstructor(String.class);
			return (SalespointIdentifier) BeanUtils.instantiateClass(constructor, source);

		} catch (NoSuchMethodException | SecurityException o_O) {
			throw new ConversionFailedException(TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetClass), source,
					o_O);
		}
	}
}
