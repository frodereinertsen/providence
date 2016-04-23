package net.morimekta.providence.thrift;

import net.morimekta.providence.serializer.PBinarySerializer;
import net.morimekta.providence.serializer.PSerializeException;
import net.morimekta.providence.streams.MessageCollectors;
import net.morimekta.providence.streams.MessageStreams;
import net.morimekta.test.providence.Containers;
import net.morimekta.test.providence.srv.Request;
import net.morimekta.util.Strings;
import net.morimekta.util.io.IOUtils;

import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.morimekta.providence.testing.ProvidenceHelper.arrayListFromJsonResource;
import static net.morimekta.providence.testing.ProvidenceMatchers.messageEq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Incidentally actually a test that the PBinarySerializer generates the same output as
 * TBinaryProtocolSerializer, and can also read back what TBinaryProtocolSerializer
 * generates.
 */
public class PBinarySerializerTest {
    private static final PBinarySerializer providence = new PBinarySerializer();
    private static final TBinaryProtocolSerializer thrift = new TBinaryProtocolSerializer();
    private static ArrayList<Containers> containers;

    @Before
    public void setUp() throws PSerializeException, IOException {
        synchronized (PBinarySerializerTest.class) {
            // Since these are immutable, we don't need to read for each test.
            if (containers == null) {
                containers = arrayListFromJsonResource("/providence/test.json", Containers.kDescriptor);
            }
        }
    }

    @Test
    public void testThriftToProvidence_simple() throws IOException, PSerializeException {
        Request request = new Request("test");

        // Providence client talks to thrift service.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        thrift.serialize(baos, request);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Request out = providence.deserialize(bais, Request.kDescriptor);

        assertThat(out, messageEq(request));
    }

    @Test
    public void testProvidenceToThrift_simple() throws IOException, PSerializeException {
        Request request = new Request("test");

        // Providence client talks to thrift service.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        providence.serialize(baos, request);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Request out = thrift.deserialize(bais, Request.kDescriptor);

        assertThat(out, messageEq(request));
    }

    @Test
    public void testProvidenceToThrift_containers() throws IOException {
        // Providence client talks to thrift service.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        containers.stream().collect(MessageCollectors.toStream(baos, providence));
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        List<Containers> out = MessageStreams.stream(bais, thrift, Containers.kDescriptor).collect(Collectors.toList());

        assertEquals(containers.size(), out.size());
    }

    @Test
    public void testThriftToProvidence() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        containers.stream().collect(MessageCollectors.toStream(baos, thrift));
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        List<Containers> out = MessageStreams.stream(bais, providence, Containers.kDescriptor).collect(Collectors.toList());

        assertEquals(containers.size(), out.size());
    }
}
