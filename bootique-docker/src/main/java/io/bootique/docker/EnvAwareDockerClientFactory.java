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

/**
 * @since 3.0
 */
@BQConfig("Builds a DockerClient from the surrounding environment properties as specified in the Docker Client docs")
@JsonTypeName("env")
public class EnvAwareDockerClientFactory extends HttpTransportDockerClientFactory {

    @Override
    protected DockerClientConfig createConfig() {
        // this will load various env properties per
        // https://github.com/docker-java/docker-java/blob/master/docs/getting_started.md#instantiating-a-dockerclientconfig
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }
}
