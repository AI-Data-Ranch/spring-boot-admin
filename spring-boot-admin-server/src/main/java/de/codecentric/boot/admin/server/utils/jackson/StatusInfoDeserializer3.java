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

import java.util.HashMap;
import java.util.Map;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;

public class StatusInfoDeserializer3 extends StdDeserializer<StatusInfo> {

	public StatusInfoDeserializer3() {
		super(StatusInfo.class);
	}

	@Override
	public StatusInfo deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		JsonNode node = p.readValueAsTree();
		String status = node.get("status").asText();
		Map<String, Object> details = null;
		if (node.has("details") && !node.get("details").isNull()) {
			details = nodeToMap(node.get("details"));
		}
		return StatusInfo.valueOf(status, details);
	}

	private Map<String, Object> nodeToMap(JsonNode node) {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, JsonNode> entry : node.properties()) {
			map.put(entry.getKey(), nodeToValue(entry.getValue()));
		}
		return map;
	}

	private Object nodeToValue(JsonNode node) {
		if (node.isTextual()) {
			return node.asText();
		}
		else if (node.isNumber()) {
			return node.numberValue();
		}
		else if (node.isBoolean()) {
			return node.asBoolean();
		}
		else if (node.isNull()) {
			return null;
		}
		else if (node.isObject()) {
			return nodeToMap(node);
		}
		return node.asText();
	}

}
