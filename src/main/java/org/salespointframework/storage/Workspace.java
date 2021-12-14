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

import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;

/**
 * An abstraction of some {@link Path}-based workspace configurable via the {@code salespoint.storage} application
 * property.
 *
 * @author Oliver Drotbohm
 */
@EqualsAndHashCode
@ConstructorBinding
@ConfigurationProperties("salespoint.storage")
class Workspace implements InitializingBean {

	/**
	 * Folder location for storing files
	 */
	private final Path root;

	/**
	 * Creates a new {@link Workspace} at the given location.
	 *
	 * @param location must not be {@literal null}.
	 */
	Workspace(@DefaultValue("${user.home}/.salespoint/uploads") Path location) {

		Assert.notNull(location, "Location must not be null!");

		this.root = location;
	}

	/**
	 * Returns a new {@link Workspace} for the given sub folder.
	 *
	 * @param subFolder must not be {@literal null} or empty.
	 * @return
	 */
	public Workspace forSubFolder(String subFolder) {

		Assert.hasText(subFolder, "Sub folder must not be null or empty!");

		return new Workspace(root.resolve(subFolder)).init();
	}

	/**
	 * Returns all files in the workspace as {@link Path} instances.
	 *
	 * @return
	 */
	public Stream<Path> allFiles() {

		try {

			return Files.walk(root, 1)
					.filter(it -> !Files.isDirectory(it));

		} catch (IOException o_O) {
			return Stream.empty();
		}
	}

	/**
	 * Resolves the given file name into a {@link Path}. Sanitizes the given name to effectively map to a file located in
	 * the workspace root.
	 *
	 * @param filename must not be {@literal null} or empty.
	 * @return
	 */
	public Path resolve(String filename) {

		Assert.hasText(filename, "File name must not be null or empty!");

		var sanitized = filename.substring(Math.max(filename.lastIndexOf('/'), 0));

		return root.resolve(sanitized).normalize().toAbsolutePath();
	}

	/**
	 * Deletes all files in the current workspace.
	 *
	 * @return
	 */
	public Workspace deleteAll() {

		FileSystemUtils.deleteRecursively(root.toFile());
		return this;
	}

	/**
	 * Returns the current workspace as {@link Path}.
	 *
	 * @return
	 */
	public Path asPath() {
		return root;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private Workspace init() {

		try {

			Path created = Files.createDirectories(root);

			Assert.isTrue(created.toFile().exists(), "Foo!");

			return this;
		} catch (IOException o_O) {
			throw new IllegalStateException(o_O);
		}
	}
}
