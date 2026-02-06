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

import de.codecentric.boot.admin.server.domain.events.InstanceRegistrationUpdatedEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceRegistrationUpdatedEventMixinTest {

	private final JsonMapper objectMapper;

	protected InstanceRegistrationUpdatedEventMixinTest() {
		AdminServerModule adminServerModule = new AdminServerModule(new String[] { ".*password$" });
		objectMapper = JsonMapper.builder().addModule(adminServerModule).build();
	}

	@Test
	void verifyDeserialize() throws JacksonException, JSONException {
		String json = new JSONObject().put("instance", "test123")
			.put("version", 12345678L)
			.put("timestamp", 1587751031.000000000)
			.put("type", "REGISTRATION_UPDATED")
			.put("registration", new JSONObject().put("name", "test").put("healthUrl", "http://localhost:9080/health"))
			.toString();

		InstanceRegistrationUpdatedEvent event = objectMapper.readValue(json, InstanceRegistrationUpdatedEvent.class);
		assertThat(event).isNotNull();
		assertThat(event.getInstance()).isEqualTo(InstanceId.of("test123"));
		assertThat(event.getVersion()).isEqualTo(12345678L);
		assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS));
		assertThat(event.getRegistration().getName()).isEqualTo("test");
	}

	@Test
	void verifySerialize() throws JacksonException, JSONException {
		InstanceId id = InstanceId.of("test123");
		Instant timestamp = Instant.ofEpochSecond(1587751031).truncatedTo(ChronoUnit.SECONDS);
		InstanceRegistrationUpdatedEvent event = new InstanceRegistrationUpdatedEvent(id, 12345678L, timestamp,
				Registration.create("test", "http://localhost:9080/health").build());

		String result = objectMapper.writeValueAsString(event);
		assertThat(result).contains("\"instance\":\"test123\"");
		assertThat(result).contains("\"version\":12345678");
		assertThat(result).contains("\"type\":\"REGISTRATION_UPDATED\"");
		assertThat(result).contains("\"name\":\"test\"");
	}

}
