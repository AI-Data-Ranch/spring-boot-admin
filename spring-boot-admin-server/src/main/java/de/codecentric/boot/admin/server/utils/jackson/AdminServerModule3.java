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

import de.codecentric.boot.admin.server.domain.values.Info;
import de.codecentric.boot.admin.server.domain.values.Registration;

/**
 * Jackson 3.x module for Spring Boot Admin Server. This module is used by Spring Boot
 * 4.0.0 which uses Jackson 3.x (tools.jackson) for HTTP message conversion.
 * <p>
 * This module registers deserializers and serializers for domain classes that need
 * special handling. The serializers flatten map-based value objects to match the behavior
 * of Jackson 2.x @JsonAnyGetter annotations.
 *
 * @author Stefan Rempfer
 */
public class AdminServerModule3 extends SimpleModule {

	/**
	 * Construct the Jackson 3.x module for Spring Boot Admin Server.
	 */
	public AdminServerModule3() {
		super(AdminServerModule3.class.getName());
		addDeserializer(Registration.class, new RegistrationDeserializer3());
		addSerializer(Info.class, new InfoSerializer3());
	}

}
