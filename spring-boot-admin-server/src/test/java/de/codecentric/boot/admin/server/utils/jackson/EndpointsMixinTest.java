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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import de.codecentric.boot.admin.server.domain.values.Endpoint;
import de.codecentric.boot.admin.server.domain.values.Endpoints;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointsMixinTest {

	private final ObjectMapper objectMapper;

	protected EndpointsMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		objectMapper = Jackson2ObjectMapperBuilder.json().modules(adminServerModule, javaTimeModule).build();
	}

	@Test
	void verifyDeserialize() throws JSONException, JsonProcessingException {
		String json = new JSONArray().put(new JSONObject().put("id", "info").put("url", "http://localhost:8080/info"))
			.put(new JSONObject().put("id", "health").put("url", "http://localhost:8080/health"))
			.toString();

		Endpoints endpoints = objectMapper.readValue(json, Endpoints.class);
		assertThat(endpoints).isNotNull()
			.containsExactlyInAnyOrder(Endpoint.of("info", "http://localhost:8080/info"),
					Endpoint.of("health", "http://localhost:8080/health"));
	}

	@Test
	void verifySerialize() throws JsonProcessingException {
		Endpoints endpoints = Endpoints.single("info", "http://localhost:8080/info")
			.withEndpoint("health", "http://localhost:8080/health");

		String json = objectMapper.writeValueAsString(endpoints);
		JsonNode jsonNode = objectMapper.readTree(json);
		assertThat(jsonNode.isArray()).isTrue();
		assertThat(jsonNode.size()).isEqualTo(2);

		String id0 = jsonNode.get(0).get("id").asText();
		String url0 = jsonNode.get(0).get("url").asText();
		assertThat(id0).isIn("info", "health");
		assertThat(url0).isIn("http://localhost:8080/info", "http://localhost:8080/health");

		String id1 = jsonNode.get(1).get("id").asText();
		String url1 = jsonNode.get(1).get("url").asText();
		assertThat(id1).isIn("info", "health");
		assertThat(url1).isIn("http://localhost:8080/info", "http://localhost:8080/health");
	}

}
