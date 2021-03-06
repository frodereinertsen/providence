package net.morimekta.providence.thrift.client;

import net.morimekta.providence.PApplicationException;
import net.morimekta.providence.serializer.BinarySerializer;
import net.morimekta.providence.serializer.Serializer;
import net.morimekta.test.providence.srv.Failure;
import net.morimekta.test.providence.srv.MyService;
import net.morimekta.test.providence.srv.MyService2;
import net.morimekta.test.providence.srv.Request;
import net.morimekta.test.providence.srv.Response;
import net.morimekta.test.thrift.srv.MyService.Iface;
import net.morimekta.test.thrift.srv.MyService.Processor;

import org.apache.commons.codec.DecoderException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.morimekta.providence.PApplicationExceptionType.UNKNOWN_METHOD;
import static net.morimekta.providence.testing.ProvidenceMatchers.equalToMessage;
import static net.morimekta.providence.thrift.util.TestUtil.findFreePort;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test that we can connect to a thrift servlet and get reasonable input and output.
 */
public class NonblockingSocketClientHandlerTest {
    private static ExecutorService    executor;
    private static int                port;
    private static Iface              impl;
    private static TNonblockingServer server;
    private static InetSocketAddress  address;
    private static BinarySerializer   serializer;

    @BeforeClass
    public static void setUpServer() throws Exception {
        port = findFreePort();
        impl = Mockito.mock(Iface.class);

        TNonblockingServerTransport transport = new TNonblockingServerSocket(port);
        server = new TNonblockingServer(
                new TNonblockingServer.Args(transport)
                        .protocolFactory(new TBinaryProtocol.Factory())
                        .processor(new Processor<>(impl)));

        executor = Executors.newSingleThreadExecutor();
        executor.submit(server::serve);
        serializer = new BinarySerializer();
        address = new InetSocketAddress("localhost", port);
    }

    @Before
    public void setUp() throws Exception {
        reset(impl);

    }

    @AfterClass
    public static void tearDownServer() {
        try {
            server.stop();
            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSimpleRequest()
            throws IOException, TException, net.morimekta.test.providence.srv.Failure {
        when(impl.test(new net.morimekta.test.thrift.srv.Request("test")))
                .thenReturn(new net.morimekta.test.thrift.srv.Response("response"));

        MyService.Iface client = new MyService.Client(new NonblockingSocketClientHandler(serializer, address));

        Response response = client.test(Request.builder().setText("test").build());

        verify(impl).test(any(net.morimekta.test.thrift.srv.Request.class));

        assertThat(response, is(equalToMessage(new Response("response"))));
    }

    @Test
    public void testSimpleRequest_exception() throws IOException, TException, Failure {
        when(impl.test(new net.morimekta.test.thrift.srv.Request("test")))
                .thenThrow(new net.morimekta.test.thrift.srv.Failure("failure"));

        MyService.Iface client = new MyService.Client(new NonblockingSocketClientHandler(serializer, address));
        try {
            client.test(Request.builder().setText("test").build());
            fail("no exception");
        } catch (Failure f) {
            assertThat(f, is(equalToMessage(new Failure("failure"))));
        }

        verify(impl).test(any(net.morimekta.test.thrift.srv.Request.class));
    }

    @Test
    public void testSimpleRequest_wrongMethod()
            throws IOException, TException, DecoderException, Failure {
        when(impl.test(any(net.morimekta.test.thrift.srv.Request.class)))
                .thenThrow(new net.morimekta.test.thrift.srv.Failure("failure"));

        MyService2.Iface client = new MyService2.Client(new NonblockingSocketClientHandler(serializer, address));

        try {
            client.testing(new Request("test"));
            fail("no exception");
        } catch (PApplicationException e) {
            assertThat(e.getId(), is(UNKNOWN_METHOD));
            assertThat(e.getMessage(), is(equalTo("Invalid method name: 'testing'")));
        }

        verifyZeroInteractions(impl);
    }

    @Test
    public void testSimpleRequest_cannotConnect() throws IOException, TException, Failure {
        Serializer serializer = new BinarySerializer();
        InetSocketAddress address = new InetSocketAddress("localhost", port - 10);
        MyService.Iface client = new MyService.Client(new NonblockingSocketClientHandler(serializer, address));
        try {
            client.test(Request.builder().setText("test").build());
            fail("no exception");
        } catch (ConnectException e) {
            assertThat(e.getMessage(), startsWith("Connection refused"));
        }

        verifyZeroInteractions(impl);
    }
}
