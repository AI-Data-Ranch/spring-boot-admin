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

package de.codecentric.boot.admin.server.eventstore;

import java.util.List;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;

import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("docker")
public class HazelcastEventStoreWithClientConfigTest extends AbstractEventStoreTest {

	private static GenericContainer<?> hazelcastServer;

	private HazelcastInstance hazelcast;

	@BeforeAll
	static void startContainer() {
		assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker is not available");
		hazelcastServer = new GenericContainer<>("hazelcast/hazelcast:4.2.2").withExposedPorts(5701);
		hazelcastServer.start();
	}

	@AfterAll
	static void stopContainer() {
		if (hazelcastServer != null) {
			hazelcastServer.stop();
		}
	}

	@Override
	protected InstanceEventStore createStore(int maxLogSizePerAggregate) {
		if (this.hazelcast == null) {
			this.hazelcast = createHazelcastInstance();
		}
		IMap<InstanceId, List<InstanceEvent>> eventLog = this.hazelcast.getMap("testList" + System.currentTimeMillis());
		return new HazelcastEventStore(maxLogSizePerAggregate, eventLog);
	}

	@Override
	protected void shutdownStore() {
		if (this.hazelcast != null) {
			this.hazelcast.shutdown();
			this.hazelcast = null;
		}
	}

	private HazelcastInstance createHazelcastInstance() {
		String address = hazelcastServer.getHost() + ":" + hazelcastServer.getMappedPort(5701);

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getNetworkConfig().addAddress(address);

		return HazelcastClient.newHazelcastClient(clientConfig);
	}

}
