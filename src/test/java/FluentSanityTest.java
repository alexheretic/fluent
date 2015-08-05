import alexh.Fluent;
import org.junit.Test;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FluentSanityTest {

    static <X extends Fluent.Map, Y extends java.util.Map> FluentMapTester<X, Y> testFluentMap() {
        return new FluentMapTester<>();
    }

    @Test
    public void sanityTest_maps() {
        testFluentMap().withFluent(Fluent.HashMap::new).withExpectedSuperclass(java.util.HashMap.class).run();
        testFluentMap().withFluent(Fluent.LinkedHashMap::new).withExpectedSuperclass(java.util.LinkedHashMap.class).run();
        testFluentMap().withFluent(Fluent.WeakHashMap::new).withExpectedSuperclass(java.util.WeakHashMap.class).run();

        final Iterator<Example> enumVal = Stream.of(Example.values()).iterator();
        testFluentMap().withFluent(() -> new Fluent.EnumMap<>(Example.class))
            .withKeySupplier(enumVal::next).withExpectedSuperclass(java.util.EnumMap.class).run();

        testFluentMap().withFluent(Fluent.IdentityHashMap::new).withExpectedSuperclass(java.util.IdentityHashMap.class).run();
        testFluentMap().withFluent(Fluent.ConcurrentHashMap::new).withExpectedSuperclass(java.util.concurrent.ConcurrentHashMap.class).run();
        testFluentMap().withFluent(Fluent.ConcurrentSkipListMap::new).withExpectedSuperclass(java.util.concurrent.ConcurrentSkipListMap.class).run();
    }

    enum Example {
        ONE, TWO, THREE, FOUR, FIVE, SIX
    }

    static class FluentMapTester<T extends Fluent.Map, V extends java.util.Map> {

        private int counter = 1;

        private Supplier<T> supplier;
        private Supplier<?> keySupplier = () -> "key" + counter++;
        private Class<? extends V> superclass;

        FluentMapTester<T, V> withFluent(Supplier<T> supplier) {
            this.supplier = supplier;
            return this;
        }

        FluentMapTester<T, V> withKeySupplier(Supplier<?> supplier) {
            this.keySupplier = supplier;
            return this;
        }

        FluentMapTester<T, V> withExpectedSuperclass(Class<? extends V> mapClass) {
            this.superclass = mapClass;
            return this;
        }

        void run() {
            final T fluent = supplier.get();
            fluent.clear();

            assertThat(format("Unexpected fluent inheritance %s is not a %s", fluent.getClass().getName(), superclass.getName()),
                fluent, instanceOf(superclass));

            final Fluent.Map appendedFluent = fluent.append(keySupplier.get(), "val");
            assertThat(appendedFluent, sameInstance(fluent));
            assertThat(fluent.size(), is(1));

            java.util.Map<Object, Object> map = new java.util.HashMap<>();
            map.put(keySupplier.get(), "val2");
            map.put(keySupplier.get(), "val3");

            final Fluent.Map allAppendedFluent = fluent.appendAll(map);
            assertThat(allAppendedFluent, sameInstance(fluent));
            assertThat(fluent.size(), is(3));

            final Fluent.Map entryAppendedFluent = fluent.append(new AbstractMap.SimpleEntry<>(keySupplier.get(), "val4"));
            assertThat(entryAppendedFluent, sameInstance(fluent));
            assertThat(fluent.size(), is(4));
        }
    }
}