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

import java.io.IOException;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class StatusInfoMixinTest {

	private final ObjectMapper objectMapper;

	protected StatusInfoMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		objectMapper = Jackson2ObjectMapperBuilder.json().modules(adminServerModule, javaTimeModule).build();
	}

	@Test
	void verifyDeserialize() throws JSONException, JsonProcessingException {
		String json = new JSONObject().put("status", "OFFLINE")
			.put("details", new JSONObject().put("foo", "bar"))
			.toString();

		StatusInfo statusInfo = objectMapper.readValue(json, StatusInfo.class);
		assertThat(statusInfo).isNotNull();
		assertThat(statusInfo.getStatus()).isEqualTo("OFFLINE");
		assertThat(statusInfo.getDetails()).containsOnly(entry("foo", "bar"));
	}

	@Test
	void verifySerialize() throws IOException, JSONException {
		StatusInfo statusInfo = StatusInfo.valueOf("OFFLINE", Collections.singletonMap("foo", "bar"));

		String jsonContent = objectMapper.writeValueAsString(statusInfo);
		JSONObject json = new JSONObject(jsonContent);
		assertThat(json.getString("status")).isEqualTo("OFFLINE");
		assertThat(objectMapper.readValue(json.getJSONObject("details").toString(), java.util.Map.class))
			.containsOnly(entry("foo", "bar"));
		assertThat(json.has("up")).isFalse();
		assertThat(json.has("offline")).isFalse();
		assertThat(json.has("down")).isFalse();
		assertThat(json.has("unknown")).isFalse();
	}

}
