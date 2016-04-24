package net.morimekta.providence.serializer;

import net.morimekta.providence.PMessage;
import net.morimekta.providence.PServiceCall;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PService;
import net.morimekta.providence.descriptor.PStructDescriptor;

import java.io.IOException;
import java.io.InputStream;

/**
 * A reader helper class for matching a serializer with an input stream.
 */
public class MessageReader {
    private final InputStream in;
    private final Serializer  serializer;

    public MessageReader(InputStream in, Serializer serializer) {
        this.in = in;
        this.serializer = serializer;
    }

    public <T extends PMessage<T>, TF extends PField> T
    read(PStructDescriptor<T, TF> descriptor) throws IOException, SerializerException {
        return serializer.deserialize(in, descriptor);
    }

    public <T extends PMessage<T>> PServiceCall<T>
    read(PService service) throws IOException, SerializerException {
        return serializer.deserialize(in, service);
    }
}
