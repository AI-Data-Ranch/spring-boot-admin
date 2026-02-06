/*
 * Copyright 2014-2024 the original author or authors.
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
import tools.jackson.databind.deser.std.StdDeserializer;

import de.codecentric.boot.admin.server.domain.values.InstanceId;

/**
 * Jackson 3.x deserializer for InstanceId class. This is needed because Spring Boot 4.0.0
 * uses Jackson 3.x (tools.jackson) for HTTP message conversion.
 */
public class InstanceIdDeserializer3 extends StdDeserializer<InstanceId> {

	public InstanceIdDeserializer3() {
		super(InstanceId.class);
	}

	@Override
	public InstanceId deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		return InstanceId.of(p.getValueAsString());
	}

}
