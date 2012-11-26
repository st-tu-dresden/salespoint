package org.salespointframework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.salespointframework.core.user.User;

/**
 * 
 * @author Paul Henke
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggedInUser {
	Class<? extends User> value() default User.class;
	String name() default "user";
}
