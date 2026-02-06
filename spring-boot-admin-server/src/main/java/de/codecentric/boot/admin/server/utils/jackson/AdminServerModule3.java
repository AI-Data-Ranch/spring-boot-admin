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

import tools.jackson.databind.module.SimpleModule;

import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;

/**
 * Jackson 3.x module for Spring Boot Admin Server. This module is needed because Spring
 * Boot 4.0.0 uses Jackson 3.x (tools.jackson) for HTTP message conversion. <br>
 * This module registers the Jackson 3.x serializers and deserializers for value types.
 *
 * @author Stefan Rempfer
 */
public class AdminServerModule3 extends SimpleModule {

	public AdminServerModule3() {
		super(AdminServerModule3.class.getName());

		// Register serializers and deserializers for value types
		addDeserializer(Registration.class, new RegistrationDeserializer3());
		addSerializer(InstanceId.class, new InstanceIdSerializer3());
		addDeserializer(InstanceId.class, new InstanceIdDeserializer3());
	}

}
