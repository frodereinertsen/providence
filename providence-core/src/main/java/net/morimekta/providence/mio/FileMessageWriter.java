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
import net.morimekta.providence.serializer.Serializer;
import net.morimekta.providence.serializer.SerializerException;
import net.morimekta.providence.streams.MessageStreams;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A writer helper class for matching a serializer with an output stream.
 */
public class FileMessageWriter implements MessageWriter, Closeable {
    private final File       file;
    private final Serializer serializer;
    private final boolean    append;

    private OutputStream out;

    public FileMessageWriter(File file, Serializer serializer) {
        this(file, serializer, false);
    }

    public FileMessageWriter(File file, Serializer serializer, boolean append) {
        this.file = file;
        this.serializer = serializer;
        this.append = append;
    }

    @Override
    public <Message extends PMessage<Message, Field>, Field extends PField>
    int write(Message message) throws IOException {
        int ret = serializer.serialize(getOutputStream(), message);
        if (!serializer.binaryProtocol()) {
            out.write(MessageStreams.READABLE_ENTRY_SEP);
        }
        return ret;
    }

    @Override
    public <Message extends PMessage<Message, Field>, Field extends PField>
    int write(PServiceCall<Message, Field> call) throws IOException {
        int ret = serializer.serialize(getOutputStream(), call);
        if (!serializer.binaryProtocol()) {
            out.write(MessageStreams.READABLE_ENTRY_SEP);
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        if (out != null) {
            try {
                out.flush();
                out.close();
            } finally {
                out = null;
            }
        }
    }

    private OutputStream getOutputStream() throws FileNotFoundException {
        if (out == null) {
            out = new BufferedOutputStream(new FileOutputStream(file, append));
        }
        return out;
    }
}
