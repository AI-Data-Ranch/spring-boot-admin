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

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import de.codecentric.boot.admin.server.domain.events.InstanceDeregisteredEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceDeregisteredEventMixinTest {

	private final JsonMapper objectMapper;

	protected InstanceDeregisteredEventMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		objectMapper = JsonMapper.builder().addModule(adminServerModule).build();
	}

	@Test
	void verifyDeserialize() throws JacksonException, JSONException {
		String json = new JSONObject().put("instance", "test123")
			.put("version", 12345678L)
			.put("timestamp", 1587751031.000000000)
			.put("type", "DEREGISTERED")
			.toString();

		InstanceDeregisteredEvent event = objectMapper.readValue(json, InstanceDeregisteredEvent.class);
		assertThat(event).isNotNull();
		assertThat(event.getInstance()).isEqualTo(InstanceId.of("test123"));
		assertThat(event.getVersion()).isEqualTo(12345678L);
		assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS));
	}

	@Test
	void verifyDeserializeWithOnlyRequiredProperties() throws JacksonException, JSONException {
		String json = new JSONObject().put("instance", "test123")
			.put("timestamp", 1587751031.000000000)
			.put("type", "DEREGISTERED")
			.toString();

		InstanceDeregisteredEvent event = objectMapper.readValue(json, InstanceDeregisteredEvent.class);
		assertThat(event).isNotNull();
		assertThat(event.getInstance()).isEqualTo(InstanceId.of("test123"));
		assertThat(event.getVersion()).isZero();
		assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS));
	}

	@Test
	void verifySerialize() throws JacksonException, JSONException {
		InstanceId id = InstanceId.of("test123");
		Instant timestamp = Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS);
		InstanceDeregisteredEvent event = new InstanceDeregisteredEvent(id, 12345678L, timestamp);

		String result = objectMapper.writeValueAsString(event);
		assertThat(result).contains("\"instance\":\"test123\"");
		assertThat(result).contains("\"version\":12345678");
		assertThat(result).contains("\"type\":\"DEREGISTERED\"");
	}

	@Test
	void verifySerializeWithOnlyRequiredProperties() throws JacksonException, JSONException {
		InstanceId id = InstanceId.of("test123");
		Instant timestamp = Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS);
		InstanceDeregisteredEvent event = new InstanceDeregisteredEvent(id, 0L, timestamp);

		String result = objectMapper.writeValueAsString(event);
		assertThat(result).contains("\"instance\":\"test123\"");
		assertThat(result).contains("\"version\":0");
		assertThat(result).contains("\"type\":\"DEREGISTERED\"");
	}

}
