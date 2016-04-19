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

package net.morimekta.providence.descriptor;

import net.morimekta.providence.PBuilder;
import net.morimekta.providence.PBuilderFactory;
import net.morimekta.providence.PType;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

/**
 * Descriptor for a list with item type.
 */
public class PList<I> extends PContainer<I, List<I>> {
    private final BuilderFactory<I> builderFactory;

    public PList(PDescriptorProvider<I> itemType,
                 BuilderFactory<I> builderFactory) {
        super(itemType);
        this.builderFactory = builderFactory;
    }

    @Override
    public String getName() {
        return "list<" + itemDescriptor().getName() + ">";
    }

    @Override
    public String getQualifiedName(String packageName) {
        return "list<" + itemDescriptor().getQualifiedName(packageName) + ">";
    }

    @Override
    public PType getType() {
        return PType.LIST;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof PList)) {
            return false;
        }
        PList<?> other = (PList<?>) o;
        return other.itemDescriptor()
                    .equals(itemDescriptor());
    }

    @Override
    public int hashCode() {
        return PList.class.hashCode() + itemDescriptor().hashCode();
    }

    public interface Builder<V> extends PBuilder<List<V>> {
        void add(V value);
        void addAll(Collection<V> items);
        void clear();

        @Override
        List<V> build();
    }

    public interface BuilderFactory<I> extends PBuilderFactory<List<I>> {
        @Override
        Builder<I> builder();
    }

    public static class ImmutableListBuilder<I> implements Builder<I> {
        private ImmutableList.Builder<I> builder;

        public ImmutableListBuilder() {
            builder = ImmutableList.builder();
        }

        @Override
        public void add(I value) {
            builder.add(value);
        }

        @Override
        public void addAll(Collection<I> items) {
            builder.addAll(items);
        }

        @Override
        public void clear() {
            builder = ImmutableList.builder();
        }

        @Override
        public List<I> build() {
            return builder.build();
        }
    }

    @Override
    public Builder<I> builder() {
        return builderFactory.builder();
    }

    public static <I> PContainerProvider<I, List<I>, PList<I>> provider(PDescriptorProvider<I> itemDesc) {
        BuilderFactory<I> factory = new BuilderFactory<I>() {
            @Override
            public Builder<I> builder() {
                return new ImmutableListBuilder<>();
            }
        };
        return provider(itemDesc, factory);
    }

    public static <I> PContainerProvider<I, List<I>, PList<I>> provider(PDescriptorProvider<I> itemDesc,
                                                                        BuilderFactory<I> builderFactory) {
        return new PContainerProvider<>(new PList<>(itemDesc, builderFactory));
    }
}
