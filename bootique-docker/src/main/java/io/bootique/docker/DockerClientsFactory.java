/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.bootique.docker;

import com.github.dockerjava.api.DockerClient;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.shutdown.ShutdownManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.0
 */
@BQConfig
public class DockerClientsFactory {

    private EnvAwareDockerClientFactory envClient;
    private Map<String, HttpTransportDockerClientFactory> clients;

    public DockerClients createClients(ShutdownManager shutdownManager) {
        return shutdownManager.onShutdown(new DockerClients(createClients(), createEnvClient()));
    }

    protected DockerClient createEnvClient() {
        EnvAwareDockerClientFactory envClientFactory = this.envClient != null
                ? this.envClient
                : new EnvAwareDockerClientFactory();
        return envClientFactory.createClient();
    }

    protected Map<String, DockerClient> createClients() {

        if (clients == null || clients.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, DockerClient> map = new HashMap<>();
        clients.forEach((k, v) -> map.put(k, v.createClient()));

        return map;
    }

    @BQConfigProperty("Optional transport configuration for a default unnamed client instantiated from the environment " +
            "properties recognized by Docker Client")
    public DockerClientsFactory setEnvClient(EnvAwareDockerClientFactory envClient) {
        this.envClient = envClient;
        return this;
    }

    @BQConfigProperty("A map of named client configurations")
    public DockerClientsFactory setClients(Map<String, HttpTransportDockerClientFactory> clients) {
        this.clients = clients;
        return this;
    }
}
