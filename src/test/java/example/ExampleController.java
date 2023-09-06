/*
 * Copyright 2017-2023 the original author or authors.
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
package example;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.*;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Example controller for integration tests.
 *
 * @author Oliver Gierke
 */
@Controller
class ExampleController {

	/**
	 * Expects the value of the request parameter to be {@code äöü€}.
	 *
	 * @param value
	 * @param response
	 */
	@RequestMapping("/encoding")
	void parameterEncoding(@RequestParam String value, HttpServletResponse response) {
		assertThat(value, is("äöü€"));
	}
}
