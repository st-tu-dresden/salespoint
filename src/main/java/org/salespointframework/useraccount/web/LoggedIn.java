/*
 * Copyright 2017 the original author or authors.
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
