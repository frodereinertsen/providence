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
package net.morimekta.providence.thrift.client;

import net.morimekta.providence.PApplicationException;
import net.morimekta.providence.PApplicationExceptionType;
import net.morimekta.providence.PMessage;
import net.morimekta.providence.PServiceCall;
import net.morimekta.providence.PServiceCallHandler;
import net.morimekta.providence.PServiceCallType;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PService;
import net.morimekta.providence.serializer.Serializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Client handler for thrift RPC using the TSimpleServer that does <b>not</b>
 * use the TFramedTransport message wrapper.
 */
public class SocketClientHandler implements PServiceCallHandler {
    private final Serializer    serializer;
    private final SocketAddress address;
    private final int connect_timeout;
    private final int read_timeout;

    public SocketClientHandler(Serializer serializer, SocketAddress address) {
        this(serializer, address, 10000, 10000);
    }

    public SocketClientHandler(Serializer serializer, SocketAddress address, int connect_timeout, int read_timeout) {
        this.serializer = serializer;
        this.address = address;
        this.connect_timeout = connect_timeout;
        this.read_timeout = read_timeout;
    }

    private synchronized Socket connect() throws IOException {
        Socket socket = new Socket();
        socket.setSoLinger(false, 0);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        socket.setSoTimeout(read_timeout);
        socket.connect(address, connect_timeout);
        return socket;
    }

    @Override
    public <Request extends PMessage<Request, RequestField>,
            Response extends PMessage<Response, ResponseField>,
            RequestField extends PField,
            ResponseField extends PField>
    PServiceCall<Response, ResponseField> handleCall(PServiceCall<Request, RequestField> call, PService service)
            throws IOException {
        if (call.getType() == PServiceCallType.EXCEPTION || call.getType() == PServiceCallType.REPLY) {
            throw new PApplicationException("Request with invalid call type: " + call.getType(),
                                            PApplicationExceptionType.INVALID_MESSAGE_TYPE);
        }

        try (Socket socket = connect()) {
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            serializer.serialize(out, call);
            out.flush();

            PServiceCall<Response, ResponseField> reply = null;
            if (call.getType() == PServiceCallType.CALL) {
                InputStream in = new BufferedInputStream(socket.getInputStream());
                reply = serializer.deserialize(in, service);

                if (reply.getType() == PServiceCallType.CALL || reply.getType() == PServiceCallType.ONEWAY) {
                    throw new PApplicationException("Reply with invalid call type: " + reply.getType(),
                                                    PApplicationExceptionType.INVALID_MESSAGE_TYPE);
                }
                if (reply.getSequence() != call.getSequence()) {
                    throw new PApplicationException("Reply sequence out of order: call = " + call.getSequence() + ", reply = " + reply.getSequence(),
                                                    PApplicationExceptionType.BAD_SEQUENCE_ID);
                }
            }
            return reply;
        }
    }
}
