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

package net.morimekta.providence.generator.format.java;

import net.morimekta.providence.PType;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PList;
import net.morimekta.providence.descriptor.PMap;
import net.morimekta.providence.descriptor.PPrimitive;
import net.morimekta.providence.descriptor.PRequirement;
import net.morimekta.providence.descriptor.PSet;
import net.morimekta.providence.generator.GeneratorException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static net.morimekta.util.Strings.c_case;
import static net.morimekta.util.Strings.camelCase;

/**
 *
 */
public class JField {
    private final PField<?> field;
    private final JHelper   helper;
    private final int       index;

    public JField(PField<?> field, JHelper helper, int index) {
        this.field = field;
        this.helper = helper;
        this.index = index;
    }

    public int index() {
        return index;
    }

    public PField<?> getPField() {
        return field;
    }

    public PType type() {
        return field.getType();
    }

    public boolean binary() {
        return field.getType() == PType.BINARY;
    }

    public int id() {
        return field.getKey();
    }

    public String name() {
        return field.getName();
    }

    public String param() {
        return camelCase("p", field.getName());
    }

    public String member() {
        return camelCase("m", field.getName());
    }

    public String isSet() {
        return camelCase("isSet", field.getName());
    }

    public String getter() {
        if (field.getType() == PType.BOOL) {
            return camelCase("is", field.getName());
        }
        return camelCase("get", field.getName());
    }

    public String presence() {
        return camelCase("has", field.getName());
    }

    public String counter() {
        return camelCase("num", field.getName());
    }

    public String setter() {
        return camelCase("set", field.getName());
    }

    public String adder() {
        if (field.getType() == PType.MAP) {
            return camelCase("putIn", field.getName());
        } else {
            return camelCase("addTo", field.getName());
        }
    }

    public String resetter() {
        return camelCase("clear", field.getName());
    }

    public String fieldEnum() {
        return c_case("", field.getName()).toUpperCase();
    }

    public String kDefault() {
        return camelCase("kDefault", field.getName());
    }

    public boolean hasDefault() {
        return alwaysPresent() || field.hasDefaultValue();
    }

    public boolean isRequired() {
        return field.getRequirement() == PRequirement.REQUIRED;
    }

    public boolean container() {
        switch (field.getType()) {
            case MAP:
            case SET:
            case LIST:
                return true;
            default:
                return false;
        }
    }

    public ContainerType containerType() {
        return JAnnotation.containerType(this);
    }

    public boolean alwaysPresent() {
        return field.getRequirement() != PRequirement.OPTIONAL &&
               field.getDescriptor() instanceof PPrimitive &&
               ((PPrimitive) field.getDescriptor()).getDefaultValue() != null;
    }

    public String valueType() throws GeneratorException {
        return helper.getValueType(field.getDescriptor());
    }

    public String fieldType() throws GeneratorException {
        if (alwaysPresent()) {
            return valueType();
        }
        return helper.getFieldType(field.getDescriptor());
    }

    public String instanceType() throws GeneratorException {
        return helper.getInstanceClassName(field);
    }

    public String builderFieldType() throws GeneratorException  {
        switch (field.getType()) {
            case MAP: return PMap.Builder.class.getName().replace('$', '.');
            case SET: return PSet.Builder.class.getName().replace('$', '.');
            case LIST: return PList.Builder.class.getName().replace('$', '.');
            default: return fieldType();
        }
    }

    public String fieldInstanceType() throws GeneratorException  {
        switch (field.getType()) {
            case MAP:
                switch (containerType()) {
                    case DEFAULT: return ImmutableMap.class.getName().replace('$', '.');
                    case SORTED: return ImmutableSortedMap.class.getName().replace('$', '.');
                    case ORDERED: return LinkedHashMap.class.getName().replace('$', '.');
                }
            case SET:
                switch (containerType()) {
                    case DEFAULT: return ImmutableSet.class.getName().replace('$', '.');
                    case SORTED: return ImmutableSortedSet.class.getName().replace('$', '.');
                    case ORDERED: return LinkedHashSet.class.getName().replace('$', '.');
                }
            case LIST:
                return ImmutableList.class.getName().replace('$', '.');
            default:
                return fieldType();
        }
    }

    public String builderInstanceType() throws GeneratorException  {
        switch (field.getType()) {
            case MAP:
                switch (containerType()) {
                    case DEFAULT: return PMap.ImmutableMapBuilder.class.getName().replace('$', '.');
                    case SORTED: return PMap.ImmutableSortedMapBuilder.class.getName().replace('$', '.');
                    case ORDERED: return PMap.LinkedHashMapBuilder.class.getName().replace('$', '.');
                }
            case SET:
                switch (containerType()) {
                    case DEFAULT: return PSet.ImmutableSetBuilder.class.getName().replace('$', '.');
                    case SORTED: return PSet.ImmutableSortedSetBuilder.class.getName().replace('$', '.');
                    case ORDERED: return PSet.LinkedHashSetBuilder.class.getName().replace('$', '.');
                }
            case LIST:
                return PList.ImmutableListBuilder.class.getName().replace('$', '.');
            default:
                return fieldType();
        }
    }

    public boolean hasComment() {
        return field.getComment() != null;
    }

    public String comment() {
        return field.getComment();
    }
}
