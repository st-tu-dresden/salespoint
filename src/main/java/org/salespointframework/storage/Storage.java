/*
 * Copyright 2021 the original author or authors.
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
package org.salespointframework.storage;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * An application service to store {@link NamedBinary} instances and obtain them as {@link Resource}s.
 *
 * @author Oliver Drotbohm
 * @since 7.5
 */
@Service
public interface Storage {

	/**
	 * Stores the given {@link NamedBinary}. Publishes a {@link FileStored} event for other interested parties to react to
	 * just stored files.
	 *
	 * @param binary must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	Resource store(NamedBinary binary);

	/**
	 * Returns all stored {@link Resource}s.
	 *
	 * @return will never be {@literal null}.
	 */
	Streamable<Resource> getAllResources();

	/**
	 * Returns the {@link Resource} for the given file name.
	 *
	 * @param filename must not be {@literal null} or empty.
	 * @return will never be {@literal null}.
	 */
	Optional<Resource> getResource(String filename);

	/**
	 * Deletes all files currently contained in the {@link Storage}.
	 */
	void deleteAll();

	/**
	 * Returns the {@link Storage} for the {@link UserAccount} identified by the given {@link UserAccountIdentifier}.
	 *
	 * @param userAccountIdentifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	Storage forUser(UserAccountIdentifier userAccountIdentifier);
}
