/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.management;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.api.management.JmxSystemPropertyKeys;
import org.apache.camel.management.util.AvailablePortFinder;
import org.junit.Before;

/**
 * Base class for JMX tests.
 */
public abstract class ManagementTestSupport extends ContextTestSupport {

    protected int registryPort;
    protected String url;

    @Override
    protected boolean useJmx() {
        return true;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        registryPort = AvailablePortFinder.getNextAvailable();
        log.info("Using port " + registryPort);

        // need to explicit set it to false to use non-platform mbs
        System.setProperty(JmxSystemPropertyKeys.CREATE_CONNECTOR, "true");
        System.setProperty(JmxSystemPropertyKeys.REGISTRY_PORT, "" + registryPort);
        super.setUp();
    }

    protected MBeanServer getMBeanServer() {
        return context.getManagementStrategy().getManagementAgent().getMBeanServer();
    }

    @SuppressWarnings("unchecked")
    protected <T> T invoke(MBeanServerConnection server, ObjectName name, String operationName)
            throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        return (T)server.invoke(name, operationName, null, null);
    }

    @SuppressWarnings("unchecked")
    protected <T> T invoke(MBeanServerConnection server, ObjectName name, String operationName, Object[] params, String[] signature)
            throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        return (T)server.invoke(name, operationName, params, signature);
    }
}
