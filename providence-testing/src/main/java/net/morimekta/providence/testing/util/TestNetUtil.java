/*
 * Copyright 2016 Providence Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.morimekta.providence.testing.util;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import static org.junit.Assert.fail;

/**
 * Networking utility for testing.
 */
public class TestNetUtil {
    public static int getExposedPort(Server server) {
        for (Connector connector : server.getConnectors()) {
            if (connector instanceof ServerConnector) {
                return  ((ServerConnector) connector).getLocalPort();
            }
        }
        fail("Unable to determine port of server");
        return -1;
    }

    public static HttpRequestFactory factory() {
        return transport().createRequestFactory();
    }

    public static HttpRequestFactory factory(HttpRequestInitializer initializer) {
        return transport().createRequestFactory(initializer);
    }

    public static HttpTransport transport() {
        return new NetHttpTransport();
    }
}
