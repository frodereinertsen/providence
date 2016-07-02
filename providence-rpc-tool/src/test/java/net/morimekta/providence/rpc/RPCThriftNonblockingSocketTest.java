package net.morimekta.providence.rpc;

import net.morimekta.console.util.TerminalSize;
import net.morimekta.providence.rpc.util.NoLogging;
import net.morimekta.test.thrift.Failure;
import net.morimekta.test.thrift.MyService;
import net.morimekta.test.thrift.Request;
import net.morimekta.test.thrift.Response;
import net.morimekta.util.io.IOUtils;

import org.apache.commons.codec.DecoderException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.eclipse.jetty.util.log.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.morimekta.providence.rpc.util.TestUtil.findFreePort;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test that we can connect to a thrift servlet and get reasonable input and output.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TerminalSize.class)
@PowerMockIgnore("javax.net.ssl.*")
public class RPCThriftNonblockingSocketTest {
    private static InputStream defaultIn;
    private static PrintStream defaultOut;
    private static PrintStream defaultErr;

    public TemporaryFolder temp;

    private OutputStream outContent;
    private OutputStream errContent;

    private static ExecutorService      executor;
    private static int                  port;
    private static MyService.Iface impl;
    private static TServer              server;

    private int             exitCode;
    private RPC             rpc;

    public String endpoint() {
        return "thrift+nonblocking://localhost:" + port;
    }

    @BeforeClass
    public static void setUpServer() throws Exception {
        defaultIn = System.in;
        defaultOut = System.out;
        defaultErr = System.err;

        Log.setLog(new NoLogging());

        port = findFreePort();
        impl = Mockito.mock(MyService.Iface.class);

        TNonblockingServerTransport transport = new TNonblockingServerSocket(port);
        server = new TNonblockingServer(
                new TNonblockingServer.Args(transport)
                        .protocolFactory(new TBinaryProtocol.Factory())
                        .processor(new MyService.Processor<>(impl)));

        executor = Executors.newSingleThreadExecutor();
        executor.submit(server::serve);
    }

    @Before
    public void setUp() throws Exception {
        mockStatic(TerminalSize.class);
        PowerMockito.when(TerminalSize.get()).thenReturn(new TerminalSize(40, 100));

        reset(impl);

        temp = new TemporaryFolder();
        temp.create();

        File thriftFile = temp.newFile("test.thrift");
        FileOutputStream file = new FileOutputStream(thriftFile);
        IOUtils.copy(getClass().getResourceAsStream("/test.thrift"), file);
        file.flush();
        file.close();

        exitCode = 0;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        rpc = new RPC() {
            @Override
            protected void exit(int i) {
                exitCode = i;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        System.setErr(defaultErr);
        System.setOut(defaultOut);
        System.setIn(defaultIn);

        temp.delete();
    }

    @AfterClass
    public static void tearDownServer() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSimpleRequest() throws IOException, TException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try (InputStream in = getClass().getResourceAsStream("/req1.json")) {
            IOUtils.copy(in, tmp);
        }
        System.setIn(new ByteArrayInputStream(tmp.toByteArray()));

        when(impl.test(any(Request.class))).thenReturn(new Response("response"));

        rpc.run("-I", temp.getRoot().getAbsolutePath(),
                "-s", "test.MyService",
                endpoint());

        verify(impl).test(any(Request.class));

        assertEquals("", errContent.toString());
        assertEquals("[\n" +
                     "    \"test\",\n" +
                     "    \"reply\",\n" +
                     "    44,\n" +
                     "    {\n" +
                     "        \"success\": {\n" +
                     "            \"text\": \"response\"\n" +
                     "        }\n" +
                     "    }\n" +
                     "]\n", outContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testSimpleRequest_FileIO() throws IOException, TException {
        File inFile = temp.newFile();
        File outFile = temp.newFile();

        try (InputStream in = getClass().getResourceAsStream("/req1.json");
             FileOutputStream fos = new FileOutputStream(inFile)) {
            IOUtils.copy(in, fos);
            fos.flush();
        }

        when(impl.test(any(Request.class))).thenReturn(new Response("response"));

        rpc.run("-I", temp.getRoot().getAbsolutePath(),
                "-s", "test.MyService",
                "-i", "file:" + inFile.getAbsolutePath(),
                "-o", "json,file:" + outFile.getAbsolutePath(),
                endpoint());

        verify(impl).test(any(Request.class));

        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);

        String out = new String(Files.readAllBytes(outFile.toPath()));
        assertEquals("[\"test\",2,44,{\"0\":{\"1\":\"response\"}}]", out);
    }

    @Test
    public void testSimpleRequest_exception() throws IOException, TException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try (InputStream in = getClass().getResourceAsStream("/req1.json")) {
            IOUtils.copy(in, tmp);
        }
        System.setIn(new ByteArrayInputStream(tmp.toByteArray()));

        when(impl.test(any(Request.class))).thenThrow(new Failure("failure"));

        rpc.run("-I", temp.getRoot().getAbsolutePath(),
                "-s", "test.MyService",
                endpoint());

        verify(impl).test(any(Request.class));

        assertEquals("", errContent.toString());
        assertEquals("[\n" +
                     "    \"test\",\n" +
                     "    \"reply\",\n" +
                     "    44,\n" +
                     "    {\n" +
                     "        \"f\": {\n" +
                     "            \"text\": \"failure\"\n" +
                     "        }\n" +
                     "    }\n" +
                     "]\n", outContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testSimpleRequest_wrongMethod() throws IOException, TException, DecoderException {
        byte[] tmp = ("[\n" +
                      "    \"testing\",\n" +
                      "    \"call\",\n" +
                      "    44,\n" +
                      "    {\n" +
                      "        \"request\": {\n" +
                      "            \"text\": \"request\"\n" +
                      "        }\n" +
                      "    }\n" +
                      "]").getBytes(UTF_8);
        System.setIn(new ByteArrayInputStream(tmp));

        when(impl.test(any(Request.class))).thenThrow(new Failure("failure"));

        rpc.run("-I", temp.getRoot().getAbsolutePath(),
                "-s", "test.MyService2",
                endpoint());

        verifyZeroInteractions(impl);

        assertEquals("", errContent.toString());
        assertEquals("[\n" +
                     "    \"testing\",\n" +
                     "    \"exception\",\n" +
                     "    44,\n" +
                     "    {\n" +
                     "        \"message\": \"Invalid method name: 'testing'\",\n" +
                     "        \"id\": \"UNKNOWN_METHOD\"\n" +
                     "    }\n" +
                     "]\n", outContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testSimpleRequest_cannotConnect() throws IOException, TException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try (InputStream in = getClass().getResourceAsStream("/req1.json")) {
            IOUtils.copy(in, tmp);
        }
        System.setIn(new ByteArrayInputStream(tmp.toByteArray()));

        when(impl.test(any(Request.class))).thenReturn(new Response("failure"));

        rpc.run("-I", temp.getRoot().getAbsolutePath(),
                "-s", "test.MyService",
                "thrift://localhost:" + (port - 10));

        verifyZeroInteractions(impl);

        assertEquals("", outContent.toString());
        assertEquals("Unable to connect to thrift://localhost:" + (port - 10)+ ": Connection refused\n",
                     errContent.toString());
        assertEquals(1, exitCode);
    }
}