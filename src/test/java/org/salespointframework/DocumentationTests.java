/*
 * Copyright 2020-2022 the original author or authors.
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
package org.salespointframework;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.salespointframework.SalespointApplicationConfigurationTests.SalespointSample;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.docs.Documenter.CanvasOptions;
import org.springframework.modulith.docs.Documenter.DiagramOptions;
import org.springframework.modulith.docs.Documenter.DiagramOptions.DiagramStyle;
import org.springframework.modulith.model.ApplicationModules;

/**
 * @author Oliver Drotbohm
 */
class DocumentationTests {

	@Test
	void verifyModularity() throws IOException {

		((Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)) //
				.setLevel(Level.ERROR);

		var modules = ApplicationModules.of(SalespointSample.class);

		var diagramOptions = DiagramOptions.defaults() //
				.withExclusions(module -> module.getName().matches(".*core|.*support"))
				.withStyle(DiagramStyle.UML);

		var canvasOptions = CanvasOptions.defaults().withApiBase("{javadoc}");

		new Documenter(modules).writeDocumentation(diagramOptions, canvasOptions);
	}
}
