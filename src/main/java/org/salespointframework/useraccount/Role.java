/*
 * Copyright 2017-2022 the original author or authors.
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
package org.salespointframework.useraccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * A Role is only identified by a name. This class is immutable.
 *
 * @author Christopher Bellmann
 * @author Paul Henke
 */
@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Role implements Serializable, Comparable<Role> {

	private static final long serialVersionUID = 5440415491197908839L;
	private static final String ROLE_PREFIX = "ROLE_";

	/**
	 * The name of the role.
	 */
	@NonNull String name;

	/**
	 * Creates a new {@link Role} instance with the given name.
	 *
	 * @param name the name of the Role, must not be {@literal null} or empty.
	 */
	public static Role of(String name) {
		return new Role(name);
	}

	/**
	 * Returns the {@link Role}'s name as Spring Security authority token so that it can be used in {@code hasRole('…')}
	 * expressions.
	 *
	 * @return
	 */
	String toAuthority() {
		return name.startsWith(ROLE_PREFIX) ? name : ROLE_PREFIX.concat(name);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(Role other) {
		return this.name.compareTo(other.name);
	}
}
