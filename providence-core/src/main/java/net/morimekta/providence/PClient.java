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
package net.morimekta.providence;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Interface for handling a call request from a synchronous client.
 */
public abstract class PClient {
    /**
     * Create a base client.
     */
    protected PClient() {
        nextSequenceId = new AtomicInteger(0);
    }

    /**
     * Get the next available sequence ID.
     *
     * @return The sequence ID to use.
     */
    protected int getNextSequenceId() {
        return nextSequenceId.getAndIncrement();
    }

    private AtomicInteger nextSequenceId;
}
