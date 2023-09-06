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

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for the storage module.
 *
 * @author Oliver Drotbohm
 */
@ApplicationModuleTest
@TestPropertySource(properties = "salespoint.storage.location=${user.home}/.salespoint/${random.uuid}")
@RequiredArgsConstructor
class FileModuleTests {

	private final FileStorage storage;
	private final Workspace workspace;

	@AfterEach
	void wipeTempDirectory() throws Exception {

		storage.deleteAll();
		workspace.afterPropertiesSet();
	}

	@Test // #382
	void initializesWorkspace() {

		var path = workspace.asPath();

		assertThat(path.toFile().exists()).isTrue();
	}

	@Test // #382
	void storesFile(PublishedEvents events) throws IOException {

		assertFileStoredAndRetrieved(storage);

		assertThat(events.ofType(FileStored.class).matching(it -> !it.isUserFile())).isNotEmpty();
	}

	@Test // #382
	void storesUserSpecificFile(PublishedEvents events) throws IOException {

		var identifier = UserAccountIdentifier.of("some-user");

		assertFileStoredAndRetrieved(storage.forUser(identifier));

		assertThat(events.ofType(FileStored.class).matching(it -> it.belongsTo(identifier))).isNotEmpty();
	}

	@Test // GH-430
	void storesFileWithCustomName() {

		var source = new ClassPathResource("sample.txt", FileModuleTests.class);
		var resource = storage.store(NamedBinary.of(source).withName("custom.txt"));

		assertThat(resource.exists()).isTrue();
		assertThat(resource.isReadable()).isTrue();
		assertThat(storage.getResource("custom.txt")).hasValue(resource);
		assertThat(storage.getAllResources()).contains(resource);
	}

	@Test // GH-430
	void deletesAParticularFile() {

		var first = storeFileAs("sample.txt", "custom1.txt", storage);
		var second = storeFileAs("sample.txt", "custom2.txt", storage);

		assertThat(storage.getAllResources()).containsExactlyInAnyOrder(first, second);

		storage.deleteByName("custom1.txt");

		assertThat(storage.getAllResources()).containsExactlyInAnyOrder(second);
	}

	private static void assertFileStoredAndRetrieved(FileStorage storage) throws IOException {

		var resource = storeFileAs("sample.txt", "sample.txt", storage);

		assertThat(resource.exists()).isTrue();
		assertThat(resource.isReadable()).isTrue();
		assertThat(storage.getResource("sample.txt")).hasValue(resource);
		assertThat(storage.getAllResources()).contains(resource);
	}

	private static Resource storeFileAs(String source, String name, FileStorage files) {

		var resource = new ClassPathResource(source, FileModuleTests.class);
		return files.store(NamedBinary.of(resource).withName(name));
	}
}
