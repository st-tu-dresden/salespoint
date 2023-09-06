/*
 * Copyright 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.files;

import lombok.Value;

import java.util.Optional;

import org.jmolecules.event.types.DomainEvent;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * An event published when a {@link NamedBinary} gets stored.
 *
 * @author Oliver Drotbohm
 * @since 7.5
 */
@Value(staticConstructor = "of")
public class FileStored implements DomainEvent {

	/**
	 * The {@link Resource} for the file just stored.
	 */
	Resource resource;

	/**
	 * The {@link UserAccountIdentifier} in case the file was stored in the context of a {@link UserAccount}.
	 */
	Optional<UserAccountIdentifier> userAccountIdentifier;

	/**
	 * Returns whether the file stored belongs to the {@link UserAccount} with the given {@link UserAccountIdentifier}.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return whether the file stored belongs to the {@link UserAccount} with the given {@link UserAccountIdentifier}.
	 */
	public boolean belongsTo(UserAccountIdentifier identifier) {

		Assert.notNull(identifier, "User account identifier must not be null!");

		return userAccountIdentifier.map(identifier::equals).orElse(false);
	}

	/**
	 * Returns whether the file stored is a user-specific file.
	 *
	 * @return whether the file stored is a user-specific file.
	 */
	public boolean isUserFile() {
		return !userAccountIdentifier.isEmpty();
	}
}
