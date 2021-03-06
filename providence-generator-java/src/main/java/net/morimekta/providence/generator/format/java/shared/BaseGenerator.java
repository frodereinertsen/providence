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
package net.morimekta.providence.generator.format.java.shared;

import net.morimekta.providence.descriptor.PDeclaredDescriptor;
import net.morimekta.providence.descriptor.PMessageDescriptor;
import net.morimekta.providence.generator.Generator;
import net.morimekta.providence.generator.GeneratorException;
import net.morimekta.providence.generator.format.java.utils.JHelper;
import net.morimekta.providence.generator.format.java.utils.JUtils;
import net.morimekta.providence.generator.util.FileManager;
import net.morimekta.providence.reflect.contained.CEnumDescriptor;
import net.morimekta.providence.reflect.contained.CProgram;
import net.morimekta.providence.reflect.contained.CService;
import net.morimekta.providence.reflect.util.ProgramRegistry;
import net.morimekta.util.io.IndentedPrintWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Stein Eldar Johnsen
 * @since 05.09.15
 */
public abstract class BaseGenerator extends Generator {
    protected final JHelper helper;

    public BaseGenerator(FileManager manager, ProgramRegistry registry) throws GeneratorException {
        super(manager);

        this.helper = new JHelper(registry);
    }

    protected abstract BaseMessageFormatter messageFormatter(IndentedPrintWriter writer);

    protected abstract BaseEnumFormatter enumFormatter(IndentedPrintWriter writer);

    protected abstract BaseProgramFormatter constFomatter(IndentedPrintWriter writer);

    protected abstract BaseProgramFormatter hazelcastFomatter(IndentedPrintWriter writer);

    protected abstract BaseServiceFormatter serviceFormatter(IndentedPrintWriter writer);

    @Override
    @SuppressWarnings("resource")
    public void generate(CProgram document) throws IOException, GeneratorException {
        String javaPackage = JUtils.getJavaPackage(document);

        String path = JUtils.getPackageClassPath(javaPackage);

        if (document.getConstants()
                    .size() > 0) {
            String file = helper.getConstantsClassName(document) + ".java";
            OutputStream out = getFileManager().create(path, file);
            try {
                IndentedPrintWriter writer = new IndentedPrintWriter(out);

                appendFileHeader(writer, document);
                constFomatter(writer).appendProgramClass(document);

                writer.flush();
            } finally {
                try {
                    getFileManager().finalize(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (document.getConstants()
                    .stream().filter(t -> t.getName().equals("FACTORY_ID")).findAny().isPresent() ) { //TODO check if hazelcast flag set?

            String file = helper.getHazelcastFactoryClassName(document) + ".java";
            OutputStream out = getFileManager().create(path, file);
            try {
                IndentedPrintWriter writer = new IndentedPrintWriter(out);

                appendFileHeader(writer, document);
                hazelcastFomatter(writer).appendProgramClass(document);

                writer.flush();
            } finally {
                try {
                    getFileManager().finalize(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (PDeclaredDescriptor<?> type : document.getDeclaredTypes()) {
            String file = JUtils.getClassName(type) + ".java";
            OutputStream out = getFileManager().create(path, file);
            try {
                IndentedPrintWriter writer = new IndentedPrintWriter(out);

                appendFileHeader(writer, document);

                switch (type.getType()) {
                    case MESSAGE:
                        messageFormatter(writer).appendMessageClass((PMessageDescriptor<?, ?>) type);
                        break;
                    case ENUM:
                        enumFormatter(writer).appendEnumClass((CEnumDescriptor) type);
                        break;
                    default:
                        throw new GeneratorException("Unhandled declaration type.");

                }
                writer.flush();
            } finally {
                try {
                    getFileManager().finalize(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (CService service : document.getServices()) {
            String file = JUtils.getClassName(service) + ".java";
            OutputStream out = getFileManager().create(path, file);
            try {
                IndentedPrintWriter writer = new IndentedPrintWriter(out);
                appendFileHeader(writer, document);
                serviceFormatter(writer).appendServiceClass(service);
                writer.flush();
            } finally {
                try {
                    getFileManager().finalize(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void appendFileHeader(IndentedPrintWriter writer,
                                  CProgram document)
            throws GeneratorException, IOException {
        writer.format("package %s;", helper.getJavaPackage(document))
              .newline();
    }

}
