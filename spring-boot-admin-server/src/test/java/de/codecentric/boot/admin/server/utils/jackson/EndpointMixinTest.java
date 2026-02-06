/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.boot.admin.server.utils.jackson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import de.codecentric.boot.admin.server.domain.values.Endpoint;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointMixinTest {

	private final JsonMapper objectMapper;

	protected EndpointMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		objectMapper = JsonMapper.builder().addModule(adminServerModule).build();
	}

	@Test
	void verifyDeserialize() throws JacksonException, JSONException {
		String json = new JSONObject().put("id", "info").put("url", "http://localhost:8080/info").toString();

		Endpoint endpoint = objectMapper.readValue(json, Endpoint.class);
		assertThat(endpoint).isNotNull();
		assertThat(endpoint.getId()).isEqualTo("info");
		assertThat(endpoint.getUrl()).isEqualTo("http://localhost:8080/info");
	}

	@Test
	void verifySerialize() throws JacksonException, JSONException {
		Endpoint endpoint = Endpoint.of("info", "http://localhost:8080/info");

		String result = objectMapper.writeValueAsString(endpoint);
		assertThat(result).contains("\"id\":\"info\"");
		assertThat(result).contains("\"url\":\"http://localhost:8080/info\"");
	}

}
