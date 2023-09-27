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
package org.salespointframework.util;

import net.minidev.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

/**
 * Little helper to build a changelog from the tickets of a particular milestone.
 *
 * @author Oliver Drotbohm
 */
public class ChangelogCreator {

	private static final String MILESTONE_ID = "25";
	private static final String URI_TEMPLATE = "https://api.github.com/repos/st-tu-dresden/salespoint/issues?milestone={id}&state=closed&sort=updated";
	private static final String TICKET_TEMPLATE = "- {linkbase}/%s[#%s] - %s";

	@SuppressWarnings("resource")
	public static void main(String... args) throws Exception {

		var response = new RestTemplate().getForObject(URI_TEMPLATE, String.class, MILESTONE_ID);

		var titlePath = JsonPath.compile("$[*].title");
		var idPath = JsonPath.compile("$[*].number");

		var titles = titlePath.<JSONArray> read(response);
		var ids = ((JSONArray) idPath.read(response)).iterator();

		var date = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
		var milestone = JsonPath.read(response, "$[0].milestone.title").toString();

		System.out.println(":numbered!:");
		System.out.println(String.format("[%s]", milestone.replace(" ", "-")));
		System.out.println(String.format("== %s\n", milestone));
		System.out.println(String.format("Release date: %s\n", date));

		for (Object title : titles) {

			Object id = ids.next();

			String ticket = String.format(TICKET_TEMPLATE, id, id, title);
			ticket = ticket.endsWith(".") ? ticket : ticket.concat(".");

			System.out.println(ticket);
		}
	}
}
