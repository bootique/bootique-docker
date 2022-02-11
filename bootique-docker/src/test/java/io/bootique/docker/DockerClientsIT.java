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
import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@BQTest
public class DockerClientsIT {

    @BQTestTool
    static final BQTestFactory testFactory = new BQTestFactory().autoLoadModules();

    @Test
    public void testEnvClientZeroConfig() {

        BQRuntime app = testFactory.app().createRuntime();
        DockerClients clients = app.getInstance(DockerClients.class);

        assertTrue(clients.getClientNames().isEmpty());

        DockerClient client = clients.getEnvClient();
        assertNotNull(client);

        client.pingCmd().exec();
    }

    @Test
    public void testNamedClient() {

        BQRuntime app = testFactory.app()
                .module(b -> BQCoreModule.extend(b).setProperty("bq.docker.clients.a.dockerHost", "tcp://example.org:2376"))
                .module(b -> BQCoreModule.extend(b).setProperty("bq.docker.clients.a1.dockerHost", "tcp://example.org:2378"))
                .createRuntime();

        DockerClients clients = app.getInstance(DockerClients.class);

        assertEquals(Set.of("a", "a1"), clients.getClientNames());

        assertNotNull(clients.getClient("a"));
        assertNotNull(clients.getClient("a1"));
    }
}
