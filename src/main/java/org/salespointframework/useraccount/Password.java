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

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

/**
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
public abstract class Password {

	/**
	 * Returns the password's raw {@link String} value.
	 *
	 * @return
	 */
	abstract String asString();

	@Embeddable
	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated))
	public static class EncryptedPassword extends Password implements ValueObject {

		private final String value;

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.useraccount.Password#asString()
		 */
		@Override
		String asString() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.useraccount.Password#toString()
		 */
		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * An unencrypted password.
	 *
	 * @author Oliver Drotbohm
	 */
	@EqualsAndHashCode(callSuper = false)
	public static class UnencryptedPassword extends Password implements ValueObject {

		private final String value;

		/**
		 * Creates a new {@link UnencryptedPassword} for the given raw {@link String} value.
		 *
		 * @param password must not be {@literal null} or empty.
		 */
		private UnencryptedPassword(String password) {

			Assert.hasText(password, "Password must not be null or empty!");

			this.value = password;
		}

		public static UnencryptedPassword of(String password) {
			return new UnencryptedPassword(password);
		}

		/**
		 * Returns the length of the password.
		 *
		 * @return
		 */
		public int getLength() {
			return value.length();
		}

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.useraccount.Password#asString()
		 */
		@Override
		String asString() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.useraccount.Password#toString()
		 */
		@Override
		public String toString() {
			return "********";
		}
	}
}
