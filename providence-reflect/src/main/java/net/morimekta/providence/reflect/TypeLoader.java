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
package net.morimekta.providence.reflect;

import net.morimekta.providence.model.ProgramType;
import net.morimekta.providence.reflect.contained.CProgram;
import net.morimekta.providence.reflect.parser.ParseException;
import net.morimekta.providence.reflect.parser.ProgramParser;
import net.morimekta.providence.reflect.util.ProgramConverter;
import net.morimekta.providence.reflect.util.ProgramRegistry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Stein Eldar Johnsen
 * @since 07.09.15
 */
public class TypeLoader {
    private final ProgramRegistry registry;

    private final ProgramConverter         converter;
    private final ProgramParser            parser;
    private final Map<String, ProgramType> loadedDocuments;
    private final Collection<File>         includes;

    /**
     * Construct a type loader for file types matches with the given parser.
     *
     * @param includes List of files with include path roots. For includes
     *                 search these in order.
     * @param parser   The thrift file parser.
     */
    public TypeLoader(Collection<File> includes, ProgramParser parser) {
        this(includes, parser, new ProgramRegistry());
    }

    /**
     * Intermediate constructor.
     *
     * @param includes List of files with include path roots. For includes
     *                 search these in order.
     * @param parser   The thrift file parser.
     * @param registry Type registry to keep parsed types in.
     */
    private TypeLoader(Collection<File> includes, ProgramParser parser, ProgramRegistry registry) {
        this(includes, parser, registry, new ProgramConverter(registry));
    }

    /**
     * Constructor with injected functionality.
     *
     * @param includes  List of files with include path roots. For includes
     *                  search these in order.
     * @param parser    The thrift file parser.
     * @param registry  The type registry.
     * @param converter The document converter
     */
    protected TypeLoader(Collection<File> includes, ProgramParser parser, ProgramRegistry registry, ProgramConverter converter) {
        this.includes = includes;
        this.parser = parser;
        this.registry = registry;
        this.converter = converter;

        loadedDocuments = new LinkedHashMap<>();
    }

    /**
     * @return Set of loaded documents.
     */
    public Collection<ProgramType> loadedPrograms() {
        return loadedDocuments.values();
    }

    /**
     * Load a thrift definition from file including all it's dependencies.
     *
     * @param file The file to load.
     * @return The loaded contained document.
     * @throws IOException If the file could not be read.
     * @throws ParseException If the file could not be parsed.
     */
    public CProgram load(File file) throws IOException, ParseException {
        file = file.getCanonicalFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("No such file " + file.getCanonicalPath());
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException(
                    "Unable to load thrift definition from directory: " + file.getCanonicalPath());
        }

        CProgram cdoc = registry.getDocument(file.getCanonicalPath());
        if (cdoc != null) {
            return cdoc;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ProgramType doc = parser.parse(in, file, includes);

        LinkedList<File> queue = new LinkedList<>();
        if (doc.hasIncludes()) {
            for (String include : doc.getIncludes()) {
                File location = new File(file.getParent(), include).getCanonicalFile();
                if (!location.exists()) {
                    for (File inc : includes) {
                        File i = new File(inc, include);
                        if (i.exists()) {
                            location = i.getCanonicalFile();
                            break;
                        }
                    }
                }

                if (location.exists() && !queue.contains(location)) {
                    queue.add(location);
                }
            }
        }

        // Load includes in reverse order, in case of serial dependencies.
        Collections.reverse(queue);

        loadedDocuments.put(file.getCanonicalPath(), doc);
        for (File include : queue) {
            if (!loadedDocuments.containsKey(include.getCanonicalPath())) {
                load(include);
            }
        }

        // Now everything it depends on is loaded.

        cdoc = converter.convert(doc);
        registry.putDocument(file.getCanonicalPath(), cdoc);
        return cdoc;
    }

    /**
     * @return The local registry.
     */
    public ProgramRegistry getRegistry() {
        return registry;
    }
}
