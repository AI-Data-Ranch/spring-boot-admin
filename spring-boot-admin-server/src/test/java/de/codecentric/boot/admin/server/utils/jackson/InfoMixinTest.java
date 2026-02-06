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
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import de.codecentric.boot.admin.server.domain.values.Info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class InfoMixinTest {

	private final JsonMapper objectMapper;

	protected InfoMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		objectMapper = JsonMapper.builder().addModule(adminServerModule).build();
	}

	@Test
	void verifyDeserialize() throws JacksonException, JSONException {
		String json = new JSONObject().put("build", new JSONObject().put("version", "1.0.0"))
			.put("foo", "bar")
			.toString();

		Info info = objectMapper.readValue(json, Info.class);
		assertThat(info).isNotNull();
		assertThat(info.getValues()).containsOnly(entry("build", Collections.singletonMap("version", "1.0.0")),
				entry("foo", "bar"));
	}

	@Test
	void verifySerialize() throws JacksonException, JSONException {
		Map<String, Object> data = new HashMap<>();
		data.put("build", Collections.singletonMap("version", "1.0.0"));
		data.put("foo", "bar");
		Info info = Info.from(data);

		String result = objectMapper.writeValueAsString(info);
		assertThat(result).contains("\"build\"");
		assertThat(result).contains("\"version\":\"1.0.0\"");
		assertThat(result).contains("\"foo\":\"bar\"");
	}

}
