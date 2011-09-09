package org.salespointframework.web.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Use this Annotation at Spring Controllers to attach Interceptors to it.
 * Each Action will be intercepted by the given Interceptors. This will
 * only work when using the {@link SalespointAnnotationHandlerMapping}.
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptors {
	/**
	 * HandlerInterceptor
	 */
    Class<? extends HandlerInterceptor>[] value();
}
