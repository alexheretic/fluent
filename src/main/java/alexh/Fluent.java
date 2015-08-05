/*
 * Copyright 2015 Alex Butler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package alexh;

import java.util.Comparator;

/**
 * Container for fluent class implementation that allows declarative object building style.
 * Usage: 
 * <pre>{@code
 *  HashMap<String, String> map = new Fluent.HashMap<String, String>()
 *      .append("key1", "val1")
 *      .append("key2", "val2");
 * }</pre>
 * 
 * @author Alex Butler
 */
public class Fluent {

    /** Fluent extension of java.util.Map */
    public interface Map<K, V> extends java.util.Map<K, V> {

        /**
         * @see java.util.Map#put(K, V)
         * @return self-reference
         */
        default Map<K, V> append(K key, V val) {
            put(key, val);
            return this;
        }

        /**
         * @see java.util.Map#putAll(java.util.Map)
         * @return self-reference
         */
        default Map<K, V> appendAll(java.util.Map<? extends K, ? extends V> map) {
            putAll(map);
            return this;
        }

        /**
         * Equivalent to append(key, value) of the input entry
         * @see #append(K, V)
         * @param entry map entry
         * @return self-reference
         */
        default Map<K, V> append(Map.Entry<? extends K, ? extends V> entry) {
            return append(entry.getKey(), entry.getValue());
        }
    }

    public static class HashMap<K, V> extends java.util.HashMap<K, V> implements Fluent.Map<K, V> {
        public HashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        public HashMap(int initialCapacity) {
            super(initialCapacity);
        }
        public HashMap(java.util.Map m) {
            super(m);
        }
        public HashMap() {}
    }

    public static class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements Fluent.Map<K, V> {
        public LinkedHashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        public LinkedHashMap(int initialCapacity) {
            super(initialCapacity);
        }
        public LinkedHashMap(java.util.Map m) {
            super(m);
        }
        public LinkedHashMap() {}
    }

    public static class IdentityHashMap<K, V> extends java.util.IdentityHashMap<K, V> implements Fluent.Map<K, V> {
        public IdentityHashMap() {}
        public IdentityHashMap(int expectedMaxSize) {
            super(expectedMaxSize);
        }
        public IdentityHashMap(java.util.Map<? extends K, ? extends V> m) {
            super(m);
        }
    }

    public static class EnumMap<K extends Enum<K>, V> extends java.util.EnumMap<K, V> implements Fluent.Map<K, V> {
        public EnumMap(Class<K> keyType) {
            super(keyType);
        }
        public EnumMap(java.util.EnumMap<K, ? extends V> m) {
            super(m);
        }
        public EnumMap(java.util.Map<K, ? extends V> m) {
            super(m);
        }
    }

    public static class WeakHashMap<K, V> extends java.util.WeakHashMap<K, V> implements Fluent.Map<K, V> {
        public WeakHashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        public WeakHashMap(int initialCapacity) {
            super(initialCapacity);
        }
        public WeakHashMap() {}
        public WeakHashMap(java.util.Map<? extends K, ? extends V> m) {
            super(m);
        }
    }
    
    public static class ConcurrentSkipListMap<K, V> extends java.util.concurrent.ConcurrentSkipListMap<K, V> implements Fluent.Map<K, V> {
        public ConcurrentSkipListMap() {}
        public ConcurrentSkipListMap(Comparator<? super K> comparator) {
            super(comparator);
        }
        public ConcurrentSkipListMap(java.util.Map<? extends K, ? extends V> m) {
            super(m);
        }
        public ConcurrentSkipListMap(java.util.SortedMap<K, ? extends V> m) {
            super(m);
        }
    }
    
    public static class ConcurrentHashMap<K, V> extends java.util.concurrent.ConcurrentHashMap<K, V> implements Fluent.Map<K, V> {
        public ConcurrentHashMap() {}
        public ConcurrentHashMap(int initialCapacity) {
            super(initialCapacity);
        }
        public ConcurrentHashMap(java.util.Map<? extends K, ? extends V> m) {
            super(m);
        }
        public ConcurrentHashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
            super(initialCapacity, loadFactor, concurrencyLevel);
        }
    }

    private Fluent() {}
}
