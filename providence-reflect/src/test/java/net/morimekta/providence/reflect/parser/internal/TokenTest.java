package net.morimekta.providence.reflect.parser.internal;

import net.morimekta.providence.reflect.parser.ParseException;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Testing the token parser.
 */
public class TokenTest {
    @Test
    public void testIsDouble() throws IOException, ParseException {
        Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream("\n\n  3.141692,\n".getBytes(StandardCharsets.UTF_8)));
        Token token = tokenizer.next();

        assertThat(token.asString(), is(equalTo("3.141692")));
        assertThat(token.isDouble(), is(true));
    }
}
