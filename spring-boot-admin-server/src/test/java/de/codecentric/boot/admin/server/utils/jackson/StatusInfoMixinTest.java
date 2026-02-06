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

import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class StatusInfoMixinTest {

	private final JsonMapper objectMapper;

	protected StatusInfoMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		objectMapper = JsonMapper.builder().addModule(adminServerModule).build();
	}

	@Test
	void verifyDeserialize() throws JacksonException, JSONException {
		String json = new JSONObject().put("status", "OFFLINE")
			.put("details", new JSONObject().put("foo", "bar"))
			.toString();

		StatusInfo statusInfo = objectMapper.readValue(json, StatusInfo.class);
		assertThat(statusInfo).isNotNull();
		assertThat(statusInfo.getStatus()).isEqualTo("OFFLINE");
		assertThat(statusInfo.getDetails()).containsOnly(entry("foo", "bar"));
	}

	@Test
	void verifySerialize() throws JacksonException, JSONException {
		StatusInfo statusInfo = StatusInfo.valueOf("OFFLINE", Collections.singletonMap("foo", "bar"));

		String result = objectMapper.writeValueAsString(statusInfo);
		assertThat(result).contains("\"status\":\"OFFLINE\"");
		assertThat(result).contains("\"details\":{\"foo\":\"bar\"}");
	}

}
