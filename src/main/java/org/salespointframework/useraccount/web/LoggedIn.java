package org.salespointframework.useraccount.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.web.bind.ServletRequestBindingException;

/**
 * Annotation to mark the method parameter with that shall get the {@link UserAccount} of the currently logged in user
 * injected. The method parameter needs to be either of type {@code Optional<UserAccount>} (for methods that can support
 * non-authenticated access) or {@link UserAccount}. In the latter case a {@link ServletRequestBindingException} will be
 * thrown if no {@link UserAccount} could have been obtained in the first place.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 * @see LoggedInUserAccountArgumentResolver
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggedIn {
}
