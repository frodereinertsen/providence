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

package net.morimekta.providence.compiler.format.java2;

import net.morimekta.providence.PType;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PPrimitive;
import net.morimekta.providence.descriptor.PRequirement;
import net.morimekta.providence.util.PStringUtils;

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
        return PStringUtils.camelCase("p", field.getName());
    }

    public String member() {
        return PStringUtils.camelCase("m", field.getName());
    }

    public String getter() {
        if (field.getType() == PType.BOOL) {
            return PStringUtils.camelCase("is", field.getName());
        }
        return PStringUtils.camelCase("get", field.getName());
    }

    public String presence() {
        return PStringUtils.camelCase("has", field.getName());
    }

    public String counter() {
        return PStringUtils.camelCase("num", field.getName());
    }

    public String setter() {
        return PStringUtils.camelCase("set", field.getName());
    }

    public String adder() {
        if (field.getType() == PType.MAP) {
            return PStringUtils.camelCase("putTo", field.getName());
        } else {
            return PStringUtils.camelCase("addTo", field.getName());
        }
    }

    public String resetter() {
        return PStringUtils.camelCase("clear", field.getName());
    }

    public String fieldEnum() {
        return  PStringUtils.c_case("", field.getName()).toUpperCase();
    }

    public String kDefault() {
        return PStringUtils.camelCase("kDefault", field.getName());
    }

    public boolean hasDefault() {
        return alwaysPresent() ||
               field.hasDefaultValue();
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

    public boolean alwaysPresent() {
        return field.getRequirement() != PRequirement.OPTIONAL &&
               field.getDescriptor() instanceof PPrimitive &&
               ((PPrimitive) field.getDescriptor()).getDefaultValue() != null;
    }

    public String valueType() {
        return helper.getValueType(field.getDescriptor());
    }

    public String fieldType() {
        if (alwaysPresent()) {
            return valueType();
        }
        return helper.getFieldType(field.getDescriptor());
    }

    public String instanceType() {
        return helper.getInstanceClassName(field.getDescriptor());
    }

    public Object getDefaultValue(PField<?> field) {
        if (field.hasDefaultValue()) {
            return field.getDefaultValue();
        }
        if (field.getDescriptor() instanceof PPrimitive) {
            return ((PPrimitive) field.getDescriptor()).getDefaultValue();
        }
        return null;
    }

    public String provider() {
        return helper.getProviderName(field.getDescriptor());
    }

    public boolean hasComment() {
        return field.getComment() != null;
    }

    public String comment() {
        return field.getComment();
    }
}