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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * A map of preconfigured named Docker clients
 *
 * @since 3.0.M1
 */
public class DockerClients implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerClients.class);

    private final Map<String, DockerClient> clients;
    private final DockerClient envClient;

    public DockerClients(Map<String, DockerClient> clients, DockerClient envClient) {
        this.clients = clients;
        this.envClient = envClient;
    }

    /**
     * Returns a DockerClient configured from the surrounding environment properties as specified in the Docker Client
     * <a href="https://github.com/docker-java/docker-java/blob/master/docs/getting_started.md#instantiating-a-dockerclientconfig">documentation</a>.
     */
    public DockerClient getEnvClient() {
        return envClient;
    }

    public Set<String> getClientNames() {
        return clients.keySet();
    }

    public DockerClient getClient(String name) {
        DockerClient client = clients.get(name);

        if (client == null) {
            throw new IllegalArgumentException("No Docker client configured for name: " + name);
        }

        return client;
    }

    @Override
    public void close() {
        clients.forEach(this::closeClient);
        closeClient("_default_env_", envClient);
    }

    protected void closeClient(String name, DockerClient client) {
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.warn("Error closing Docker client '{}', ignoring: {}", name, e.getMessage());
        }
    }
}
