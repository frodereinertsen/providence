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

package org.apache.thrift.j2.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.j2.TMessage;
import org.apache.thrift.j2.descriptor.TDescriptor;
import org.apache.thrift.j2.descriptor.TField;

/**
 * Thrift type utilities.
 *
 * @author Stein Eldar Johnsen
 * @since 25.08.15
 */
public class TTypeUtils {
    public static String toString(byte[] bytes) {
        return TBase64Utils.encode(bytes);
    }

    public static String toString(Collection<?> collection) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        boolean first = true;
        for (Object item : collection) {
            if (first) first = false;
            else builder.append(',');
            builder.append(toString(item));
        }
        builder.append(']');
        return builder.toString();
    }

    public static String toString(Map<?,?> map) {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        boolean first = true;
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (first) first = false;
            else builder.append(',');
            builder.append(toString(entry.getKey()))
                   .append(':')
                   .append(toString(entry.getValue()));
        }
        builder.append('}');
        return builder.toString();
    }

    /**
     * Stringify a message.
     *
     * @param message
     * @return
     */
    public static String toString(TMessage<?> message) {
        return new TPrettyPrinter("", "", "").format(message);
    }

    public static String toString(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof Map) {
            return toString((Map<?,?>) o);
        } else if (o instanceof Collection) {
            return toString((Collection<?>) o);
        } else if (o instanceof byte[]) {
            return toString((byte[]) o);
        } else if (o instanceof TMessage) {
            return toString((TMessage<?>) o);
        } else {
            return o.toString();
        }
    }

    public static boolean equals(Object v1, Object v2) {
        if (v1 == v2) return true;
        if ((v1 == null) != (v2 == null)) {
            return false; // only one is null
        } else if (v1 instanceof byte[] && v2 instanceof byte[]) {
            return Arrays.equals((byte[]) v1, (byte[]) v2);
        } else if (v1 instanceof List && v2 instanceof List) {
            List<?> l1 = (List<?>) v1;
            List<?> l2 = (List<?>) v2;
            if (l1.size() != l2.size()) {
                return false;
            }
            for (int i = 0; i < l1.size(); ++i) {
                if (!equals(l1.get(i), l2.get(i))) {
                    return false;
                }
            }
            return true;
        } else if (v1 instanceof Set && v2 instanceof Set) {
            Set<?> s1 = (Set<?>) v1;
            Set<?> s2 = (Set<?>) v2;
            // Maps are equal-checked based on content only, so Tree vs
            // LinkedHash vs Hash should not matter.
            if (s1.size() != s2.size()) {
                return false;
            }
            for (Object o : s1) {
                if (!s2.contains(o)) {
                    return false;
                }
            }
            return true;
        } else if (v1 instanceof Map && v2 instanceof Map) {
            Map<?, ?> m1 = (Map<?, ?>) v1;
            Map<?, ?> m2 = (Map<?, ?>) v2;
            // Maps are equal-checked based on content only, so Tree vs
            // LinkedHash vs Hash should not matter.
            if (m1.size() != m2.size()) {
                return false;
            }
            for (Object key : m1.keySet()) {
                if (!equals(m1.get(key), m2.get(key))) {
                    return false;
                }
            }
            return true;
        } else {
            return v1.equals(v2);
        }
    }

    public static int hashCode(TField<?> field, Object object) {
        return hashCode(field) * hashCode(object);
    }

    public static int hashCode(Object object) {
        if (object == null) {
            return 1;
        }
        if (object instanceof List) {
            return hashCodeList((List<?>) object);
        }
        if (object instanceof Set) {
            return hashCodeSet((Set<?>) object);
        }
        if (object instanceof Map) {
            return hashCodeMap((Map<?, ?>) object);
        }
        return object.getClass().hashCode() * object.hashCode();
    }

    public static boolean equalsQualifiedName(TDescriptor a, TDescriptor b) {
        if ((a == null) != (b == null)) return false;
        if (a == null) return true;
        return a.getQualifiedName(null).equals(b.getQualifiedName(null));
    }

    private static <T> int hashCodeList(List<T> list) {
        int hash = List.class.hashCode();
        int i = 31;
        for (T t : list) {
            hash ^= (++i * t.getClass().hashCode() * hashCode(t));
        }
        return hash;
    }

    private static <T> int hashCodeSet(Set<T> list) {
        int hash = Set.class.hashCode();
        for (T t : list) {
            hash ^= t.getClass().hashCode() * hashCode(t);
        }
        return hash;
    }

    private static <K, V> int hashCodeMap(Map<K, V> map) {
        int hash = Map.class.hashCode();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            hash ^= hashCode(entry.getKey()) * hashCode(entry.getValue());
        }
        return hash;
    }

}