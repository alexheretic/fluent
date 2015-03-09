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

import java.util.function.*;

/**
 * Utility methods to handle unwanted checked exceptions, avoiding try-catch blocks that simply wrap checked
 * exceptions in unchecked exceptions
 */
public class Unchecker {

    private static final Function<Throwable, ? extends RuntimeException> DEFAULT_EXCEPTION_TRANSFORMER = RuntimeException::new;

    public static <T> Supplier<T> uncheck(ThrowingSupplier<T> supplier, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return () -> {
            try { return supplier.get(); }
            catch (RuntimeException | Error e) { throw e; }
            catch (Throwable t) { throw exTransformer.apply(t); }
        };
    }

    public static <T> T uncheckedGet(ThrowingSupplier<T> supplier, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return uncheck(supplier, exTransformer).get();
    }

    public static void unchecked(ThrowingRunnable runnable, Function<Throwable, ? extends RuntimeException> exTransformer) {
        uncheck(runnable, exTransformer).run();
    }

    public static <In, Out> Function<In, Out> uncheck(ThrowingFunction<In, Out> function, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return (In in) -> uncheckedGet(() -> function.apply(in), exTransformer);
    }

    public static <In1, In2, Out> BiFunction<In1, In2, Out> uncheck(ThrowingBiFunction<In1, In2, Out> function, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return (In1 in1, In2 in2) -> uncheckedGet(() -> function.apply(in1, in2), exTransformer);
    }

    public static Runnable uncheck(ThrowingRunnable runnable, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return () -> uncheckedGet(() -> {
            runnable.run();
            return null;
        }, exTransformer);
    }

    public static <T> Consumer<T> uncheck(ThrowingConsumer<T> consumer, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return (T t) -> unchecked(() -> consumer.accept(t), exTransformer);
    }

    public static <T, U> BiConsumer<T, U> uncheck(ThrowingBiConsumer<T, U> consumer, Function<Throwable, ? extends RuntimeException> exTransformer) {
        return (T t, U u) -> unchecked(() -> consumer.accept(t, u), exTransformer);
    }

    public static <T> Supplier<T> uncheck(ThrowingSupplier<T> supplier) {
        return uncheck(supplier, DEFAULT_EXCEPTION_TRANSFORMER);
    }
    
    public static <T> T uncheckedGet(ThrowingSupplier<T> supplier) {
        return uncheckedGet(supplier, DEFAULT_EXCEPTION_TRANSFORMER);
    }
    
    public static void unchecked(ThrowingRunnable runnable) {
        unchecked(runnable, DEFAULT_EXCEPTION_TRANSFORMER);
    }
    
    public static <In, Out> Function<In, Out> uncheck(ThrowingFunction<In, Out> function) {
        return uncheck(function, DEFAULT_EXCEPTION_TRANSFORMER);
    }
    
    public static <In1, In2, Out> BiFunction<In1, In2, Out> uncheck(ThrowingBiFunction<In1, In2, Out> function) {
        return uncheck(function, DEFAULT_EXCEPTION_TRANSFORMER);
    }

    public static Runnable uncheck(ThrowingRunnable runnable) {
        return uncheck(runnable, DEFAULT_EXCEPTION_TRANSFORMER);
    }
    
    public static <T> Consumer<T> uncheck(ThrowingConsumer<T> consumer) {
        return uncheck(consumer, DEFAULT_EXCEPTION_TRANSFORMER);
    }

    public static <T, U> BiConsumer<T, U> uncheck(ThrowingBiConsumer<T, U> consumer) {
        return uncheck(consumer, DEFAULT_EXCEPTION_TRANSFORMER);
    }

    /**
     * Represents a supplier of results, that could throw a checked exception
     * @see java.util.function.Supplier
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    /**
     * Operation that returns no result, but could throw a checked exception
     * @see java.lang.Runnable
     */
    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Throwable;
    }

    /**
     * Function that accepts a single argument and produces a result, but could throw a checked exception
     * @see java.util.function.Function
     */
    @FunctionalInterface
    public interface ThrowingFunction<In, Out> {
        Out apply(In in) throws Throwable;
    }

    /**
     * Function that accepts two arguments and produces a result, but could throw a checked exception
     * @see java.util.function.BiFunction
     */
    @FunctionalInterface
    public interface ThrowingBiFunction<In1, In2, Out> {
        Out apply(In1 in1, In2 in2) throws Throwable;
    }

    /**
     * Operation that accepts a single input argument and returns no result, but could throw a checked exception
     * @see java.util.function.Consumer
     */
    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Throwable;
    }

    /**
     * Operation that accepts two arguments and returns no result, but could throw a checked exception
     * @see java.util.function.BiConsumer
     */
    @FunctionalInterface
    public interface ThrowingBiConsumer<T, U> {
        void accept(T t, U u) throws Throwable;
    }
}
