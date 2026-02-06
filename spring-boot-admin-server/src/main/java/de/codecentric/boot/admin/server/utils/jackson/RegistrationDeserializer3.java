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

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

import de.codecentric.boot.admin.server.domain.values.Registration;

/**
 * Jackson 3.x deserializer for Registration class. This is needed because Spring Boot
 * 4.0.0 uses Jackson 3.x (tools.jackson) for HTTP message conversion.
 */
public class RegistrationDeserializer3 extends StdDeserializer<Registration> {

	public RegistrationDeserializer3() {
		super(Registration.class);
	}

	@Override
	public Registration deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		JsonNode node = p.readValueAsTree();
		Registration.Builder builder = Registration.builder();

		builder.name(firstNonNullAsText(node, "name"));

		if (node.hasNonNull("url")) {
			String url = firstNonNullAsText(node, "url");
			builder.healthUrl(url.replaceFirst("/+$", "") + "/health").managementUrl(url);
		}
		else {
			builder.healthUrl(firstNonNullAsText(node, "healthUrl", "health_url"));
			builder.managementUrl(firstNonNullAsText(node, "managementUrl", "management_url"));
			builder.serviceUrl(firstNonNullAsText(node, "serviceUrl", "service_url"));
		}

		if (node.has("metadata")) {
			node.get("metadata")
				.properties()
				.forEach((entry) -> builder.metadata(entry.getKey(), entry.getValue().asText()));
		}

		builder.source(firstNonNullAsText(node, "source"));

		return builder.build();
	}

	private String firstNonNullAsText(JsonNode node, String... fieldNames) {
		for (String fieldName : fieldNames) {
			if (node.hasNonNull(fieldName)) {
				return node.get(fieldName).asText();
			}
		}
		return null;
	}

}
