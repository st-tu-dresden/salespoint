/*
 * Copyright 2021-2022 the original author or authors.
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jmolecules.ddd.types.ValueObject;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * An abstraction for files originating from different sources.
 *
 * @author Oliver Drotbohm
 * @since 7.5
 * @see #of(Path)
 * @see #of(Resource)
 * @see #of(MultipartFile)
 */
public interface NamedBinary extends ValueObject {

	/**
	 * The name of the file to be stored.
	 *
	 * @return will never be {@literal null} or empty.
	 */
	String getName();

	/**
	 * The actual file content.
	 *
	 * @return will never be {@literal null}.
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * Creates a new {@link NamedBinary} for the given {@link Path}.
	 *
	 * @param path must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static NamedBinary of(Path path) {

		Assert.notNull(path, "Path must not be null!");

		return new NamedBinary() {

			@Override
			public String getName() {
				return path.getFileName().toString();
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return Files.newInputStream(path);
			}
		};
	}

	/**
	 * Creates a new {@link NamedBinary} for the given {@link Resource}.
	 *
	 * @param resource must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static NamedBinary of(Resource resource) {

		Assert.notNull(resource, "Resource must not be null!");
		Assert.isTrue(resource.exists(), () -> String.format("Resource %s does not exist!", resource));
		Assert.isTrue(resource.isReadable(), () -> String.format("Resource %s is not readable!", resource));

		return new NamedBinary() {

			@Override
			public String getName() {
				return resource.getFilename();
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return resource.getInputStream();
			}
		};
	}

	/**
	 * Creates a new {@link NamedBinary} for the given {@link MultipartFile}.
	 *
	 * @param file must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static NamedBinary of(MultipartFile file) {

		Assert.notNull(file, "Multipart file must not be null!");
		Assert.isTrue(!file.isEmpty(), "Multipart file must not be empty!");

		var fileName = file.getOriginalFilename();

		Assert.hasText(fileName, "Original file name must not be null!");

		return new NamedBinary() {

			@Override
			public String getName() {
				return fileName;
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return file.getInputStream();
			}
		};
	}
}
