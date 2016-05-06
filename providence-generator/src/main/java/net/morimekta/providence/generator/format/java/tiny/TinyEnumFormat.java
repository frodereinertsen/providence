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

package net.morimekta.providence.generator.format.java.tiny;

import net.morimekta.providence.PEnumValue;
import net.morimekta.providence.generator.GeneratorException;
import net.morimekta.providence.generator.format.java.utils.BlockCommentBuilder;
import net.morimekta.providence.generator.format.java.utils.JAnnotation;
import net.morimekta.providence.generator.format.java.utils.JHelper;
import net.morimekta.providence.generator.format.java.utils.JOptions;
import net.morimekta.providence.generator.format.java.utils.JUtils;
import net.morimekta.providence.reflect.contained.CAnnotatedDescriptor;
import net.morimekta.providence.reflect.contained.CEnumValue;
import net.morimekta.providence.reflect.contained.CEnumDescriptor;
import net.morimekta.util.Stringable;
import net.morimekta.util.io.IndentedPrintWriter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Stein Eldar Johnsen
 * @since 20.09.15
 */
public class TinyEnumFormat {
    private final JHelper  helper;
    private final JOptions options;

    public TinyEnumFormat(JHelper helper, JOptions options) {
        this.helper = helper;
        this.options = options;
    }

    public void format(IndentedPrintWriter writer, CEnumDescriptor type) throws GeneratorException {
        String simpleClass = JUtils.getClassName(type);

        if (type.getComment() != null) {
            new BlockCommentBuilder(writer)
                    .comment(type.getComment())
                    .finish();
        }
        if (JAnnotation.isDeprecated((CAnnotatedDescriptor) type)) {
            writer.appendln(JAnnotation.DEPRECATED);
        }
        writer.formatln("public enum %s implements %s {",
                        simpleClass, Stringable.class.getName())
              .begin();

        for (CEnumValue v : type.getValues()) {
            if (v.getComment() != null) {
                new BlockCommentBuilder(writer)
                        .comment(type.getComment())
                        .finish();
            }
            if (JAnnotation.isDeprecated(v)) {
                writer.appendln(JAnnotation.DEPRECATED);
            }
            writer.formatln("%s(%d, \"%s\"),",
                            JUtils.enumConst(v),
                            v.getValue(),
                            v.getName());
        }
        writer.appendln(';')
              .newline();

        writer.appendln("private final int mValue;")
              .appendln("private final String mName;")
              .newline()
              .formatln("%s(int value, String name) {", simpleClass)
              .begin()
              .appendln("mValue = value;")
              .appendln("mName = name;")
              .end()
              .appendln("}")
              .newline();

        if (options.jackson) {
            writer.formatln("@%s", JsonValue.class.getName());
        }
        writer.append("@Override")
              .appendln("public String asString() {")
              .begin()
              .appendln("return mName;")
              .end()
              .appendln('}')
              .newline();

        writer.formatln("public static %s forValue(int value) {", simpleClass)
              .begin()
              .appendln("switch (value) {")
              .begin();
        for (PEnumValue<?> value : type.getValues()) {
            writer.formatln("case %d: return %s.%s;",
                            value.getValue(),
                            simpleClass,
                            value.getName()
                                 .toUpperCase());
        }
        writer.appendln("default: return null;")
              .end()
              .appendln('}')
              .end()
              .appendln('}')
              .newline();

        if (options.jackson) {
            writer.formatln("@%s", JsonCreator.class.getName());
        }
        writer.formatln("public static %s forName(String name) {", simpleClass)
              .begin()
              .appendln("switch (name) {")
              .begin();
        for (PEnumValue<?> value : type.getValues()) {
            writer.formatln("case \"%s\": return %s.%s;",
                            value.getName(),
                            simpleClass,
                            value.getName()
                                 .toUpperCase());
        }
        writer.appendln("default: return null;")
              .end()
              .appendln('}')
              .end()
              .appendln('}')
              .end()
              .appendln('}')
              .newline();
    }
}
