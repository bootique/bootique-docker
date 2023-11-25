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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.SSLConfig;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.PolymorphicConfiguration;
import io.bootique.value.Duration;

import java.net.URI;

/**
 * @since 3.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = NoEnvDockerClientFactory.class)
@BQConfig("Configures Docker clients, providing injectable DockerClients object.")
public abstract class HttpTransportDockerClientFactory implements PolymorphicConfiguration {

    protected Integer maxConnections;
    protected Duration connectionTimeout;
    protected Duration responseTimeout;

    public DockerClient createClient() {
        DockerClientConfig config = createConfig();
        DockerHttpClient httpClient = createHttpClient(config.getDockerHost(), config.getSSLConfig());
        return DockerClientImpl.getInstance(config, httpClient);
    }

    protected abstract DockerClientConfig createConfig();

    protected DockerHttpClient createHttpClient(URI dockerHost, SSLConfig sslConfig) {
        return new ZerodepDockerHttpClient.Builder()
                .dockerHost(dockerHost)
                .sslConfig(sslConfig)
                .maxConnections(maxConnections != null ? maxConnections : 100)
                .connectionTimeout(connectionTimeout != null ? connectionTimeout.getDuration() : java.time.Duration.ofSeconds(30))
                .responseTimeout(responseTimeout != null ? responseTimeout.getDuration() : java.time.Duration.ofSeconds(30))
                .build();
    }

    @BQConfigProperty("HTTP transport max connections. The default is 100")
    public HttpTransportDockerClientFactory setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    @BQConfigProperty("HTTP transport connection timeout. The default is 30 seconds")
    public HttpTransportDockerClientFactory setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    @BQConfigProperty("HTTP transport response timeout. The default is 30 seconds")
    public HttpTransportDockerClientFactory setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
        return this;
    }
}
