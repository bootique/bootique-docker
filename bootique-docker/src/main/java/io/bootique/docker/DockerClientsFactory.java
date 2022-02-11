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

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.shutdown.ShutdownManager;

import java.util.Collections;

/**
 * @since 3.0.M1
 */
@BQConfig
public class DockerClientsFactory {

    private EnvAwareDockerClientFactory envClient;

    public DockerClients createClients(ShutdownManager shutdownManager) {

        EnvAwareDockerClientFactory envClientFactory = this.envClient != null
                ? this.envClient
                : new EnvAwareDockerClientFactory();

        DockerClients clients = new DockerClients(Collections.emptyMap(), envClientFactory.createClient());
        shutdownManager.addShutdownHook(clients);
        return clients;
    }

    @BQConfigProperty("Optional configuration for a default unnamed client instantiated from ")
    public DockerClientsFactory setEnvClient(EnvAwareDockerClientFactory envClient) {
        this.envClient = envClient;
        return this;
    }
}
