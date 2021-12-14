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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link Storage} implementation based on {@link Workspace}.
 *
 * @author Oliver Drotbohm
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class WorkspaceStorage implements Storage {

	private final Workspace properties;
	private final ApplicationEventPublisher publisher;
	private final Optional<UserAccountIdentifier> context;

	@Autowired
	public WorkspaceStorage(Workspace workspace, ApplicationEventPublisher publisher) {
		this(workspace, publisher, Optional.empty());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.storage.Storage#forUser(org.salespointframework.useraccount.UserAccount.UserAccountIdentifier)
	 */
	@Override
	public Storage forUser(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "UserAccountIdentifier must not be null!");

		return new WorkspaceStorage(properties.forSubFolder(userAccountIdentifier.toString()), publisher,
				Optional.of(userAccountIdentifier));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.storage.Storage#store(org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public Resource store(NamedBinary source) {

		Assert.notNull(source, "Source file must not be null!");

		try {

			var file = properties.resolve(source.getName());

			try (InputStream inputStream = source.getInputStream()) {
				Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
			}

			var resource = new UrlResource(file.toUri());

			publisher.publishEvent(FileStored.of(resource, context));

			return resource;

		} catch (IOException e) {
			throw new RuntimeException("Failed to store file.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.storage.Storage#getAllResources()
	 */
	@Override
	public Streamable<Resource> getAllResources() {

		return Streamable.of(() -> properties.allFiles())
				.map(WorkspaceStorage::toResource)
				.filter(Resource::exists)
				.filter(Resource::isReadable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.storage.Storage#getResource(java.lang.String)
	 */
	@Override
	public Optional<Resource> getResource(String filename) {

		Resource resource = toResource(properties.resolve(filename));

		return Optional.of(resource)
				.filter(Resource::exists)
				.filter(Resource::isReadable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.storage.Storage#deleteAll()
	 */
	@Override
	public void deleteAll() {
		properties.deleteAll();
	}

	private static Resource toResource(Path path) {

		try {
			return new UrlResource(path.toUri());
		} catch (MalformedURLException o_O) {
			throw new IllegalArgumentException("Could not read file: " + path.getFileName().toUri(), o_O);
		}
	}
}
