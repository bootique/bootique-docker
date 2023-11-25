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

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

import java.io.File;
import java.util.Objects;

/**
 * @since 3.0
 */
@BQConfig
@JsonTypeName("noenv")
public class NoEnvDockerClientFactory extends HttpTransportDockerClientFactory {

    private String dockerHost;
    private Boolean dockerTlsVerify;
    private File dockerCertPath;
    private String apiVersion;
    private String registryUrl;
    private String registryUserName;
    private String registryPassword;
    private String registryEmail;

    @Override
    protected DockerClientConfig createConfig() {

        // skip env properties. Just use configuration specified in the factory
        return new DefaultDockerClientConfig.Builder()
                .withDockerHost(getDockerHost())
                .withDockerTlsVerify(getDockerTlsVerify())
                .withDockerCertPath(getDockerCertPath())
                .withApiVersion(apiVersion)
                .withRegistryUrl(registryUrl)
                .withRegistryUsername(registryUserName)
                .withRegistryPassword(registryPassword)
                .withRegistryEmail(registryEmail)
                .build();
    }

    @BQConfigProperty("Docker host URI, e.g. 'unix:///var/run/docker.sock' or 'tcp://example.org:2378'")
    public NoEnvDockerClientFactory setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setDockerTlsVerify(boolean dockerTlsVerify) {
        this.dockerTlsVerify = dockerTlsVerify;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setDockerCertPath(File dockerCertPath) {
        this.dockerCertPath = dockerCertPath;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setRegistryUserName(String registryUserName) {
        this.registryUserName = registryUserName;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setRegistryPassword(String registryPassword) {
        this.registryPassword = registryPassword;
        return this;
    }

    @BQConfigProperty
    public NoEnvDockerClientFactory setRegistryEmail(String registryEmail) {
        this.registryEmail = registryEmail;
        return this;
    }

    protected String getDockerHost() {
        return Objects.requireNonNull(dockerHost, "'dockerHost' must be set");
    }

    protected Boolean getDockerTlsVerify() {
        return this.dockerTlsVerify != null ? dockerTlsVerify : Boolean.FALSE;
    }

    public String getDockerCertPath() {
        return dockerCertPath != null ? dockerCertPath.getAbsolutePath() : null;
    }
}
