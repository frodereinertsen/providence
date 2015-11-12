/*
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

package org.apache.thrift.j2.mio;

import java.io.File;
import java.io.IOException;

import org.apache.thrift.j2.TMessage;
import org.apache.thrift.j2.descriptor.TStructDescriptor;
import org.apache.thrift.j2.mio.utils.Sequence;
import org.apache.thrift.j2.serializer.TSerializer;

/**
 * Read messages (in global order) from a set of files in the format:
 *
 * {name}-{shard}-{seq}
 *
 * @author Stein Eldar Johnsen
 * @since 06.09.15
 */
public class TSequenceMessageReader<T extends TMessage<T>> extends TMessageReader<T> {
    private final TSerializer          mSerializer;
    private final TStructDescriptor<T> mDescriptor;

    private Sequence          mSequence;
    private File              mCurrent;
    private TMessageReader<T> mReader;

    public TSequenceMessageReader(Sequence sequence, TSerializer serializer, TStructDescriptor<T> descriptor) {
        mSerializer = serializer;
        mDescriptor = descriptor;
        mSequence = sequence;
    }

    /**
     * Read the next available message from the files. If no file is available to read
     */
    public T read() throws IOException {
        synchronized (this) {
            while (true) {
                if (mReader == null) {
                    if (mSequence == null) {
                        return null;
                    } else if (!mSequence.hasNext()) {
                        mSequence = null;
                        return null;
                    }
                    mCurrent = mSequence.next();
                    mReader = new TRecordMessageReader<>(mCurrent, mSerializer, mDescriptor);
                }
                T message = mReader.read();
                if (message != null) {
                    return message;
                } else {
                    mReader.close();
                    mReader = null;
                }
            }
        }
    }

    /**
     * Close the reading stream. Does not interfere with ongoing reads, but
     * will stop the read loop if ongoing.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        synchronized (this) {
            mSequence = null;
            if (mReader != null) {
                mReader.close();
            }
            mReader = null;
        }
    }
}