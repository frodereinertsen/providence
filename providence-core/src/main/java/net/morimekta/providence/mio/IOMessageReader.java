/*
 * Copyright 2015-2016 Providence Authors
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
package net.morimekta.providence.mio;

import net.morimekta.providence.PMessage;
import net.morimekta.providence.PServiceCall;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PMessageDescriptor;
import net.morimekta.providence.descriptor.PService;
import net.morimekta.providence.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * A reader helper class for matching a serializer with an input stream.
 */
public class IOMessageReader implements MessageReader {
    private final Serializer  serializer;
    private final InputStream in;

    public IOMessageReader(InputStream in, Serializer serializer) {
        this.in = in;
        this.serializer = serializer;
    }

    @Override
    public <Message extends PMessage<Message, Field>, Field extends PField> Message
    read(PMessageDescriptor<Message, Field> descriptor) throws IOException {
        return serializer.deserialize(in, descriptor);
    }

    @Override
    public <Message extends PMessage<Message, Field>, Field extends PField> PServiceCall<Message, Field>
    read(PService service) throws IOException {
        return serializer.deserialize(in, service);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
