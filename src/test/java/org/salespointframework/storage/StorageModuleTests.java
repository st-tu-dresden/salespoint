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

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.moduliths.test.PublishedEvents;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for the storage module.
 *
 * @author Oliver Drotbohm
 */
@ModuleTest
@TestPropertySource(properties = "salespoint.storage.location=${user.home}/.salespoint/${random.uuid}")
@RequiredArgsConstructor
class StorageModuleTests {

	private final Storage storage;
	private final Workspace workspace;

	@AfterAll
	void wipeTempDirectory() throws IOException {
		storage.deleteAll();
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

	private static void assertFileStoredAndRetrieved(Storage storage) throws IOException {

		var source = new ClassPathResource("sample.txt", StorageModuleTests.class);
		var resource = storage.store(NamedBinary.of(source));

		assertThat(resource.exists()).isTrue();
		assertThat(resource.isReadable()).isTrue();
		assertThat(storage.getResource("sample.txt")).hasValue(resource);
		assertThat(storage.getAllResources()).contains(resource);
	}
}
