import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import alexh.Fluent;
import java.time.LocalDate;
import java.util.*;
import org.junit.Test;

public class FluentMapTest {

    @Test
    public void usage() {
        Map<String, Object> inner2 = new LinkedHashMap<>();
        inner2.put("a date", LocalDate.of(2015, 2, 15));
        inner2.put("a string", "hello there");

        Map<Inner, Object> inner1 = new EnumMap<>(Inner.class);
        inner1.put(Inner.KEY1, Inner.KEY1.toString());
        inner1.put(Inner.KEY2, "foo");
        inner1.put(Inner.KEY3, inner2);

        Map<Object, Object> nonFluent = new HashMap<>();
        nonFluent.put("a key", "a value");
        nonFluent.put("inner", inner1);
        nonFluent.put("another key?", "oh yeah");

        Map<Object, Object> fluent = new Fluent.HashMap<>()
            .append("a key", "a value")
            .append("inner", new Fluent.EnumMap<>(Inner.class)
                .append(Inner.KEY1, Inner.KEY1.toString())
                .append(Inner.KEY2, "foo")
                .append(Inner.KEY3, new Fluent.LinkedHashMap<>()
                    .append("a date", LocalDate.of(2015, 2, 15))
                    .append(new AbstractMap.SimpleEntry<>("a string", "hello there"))))
            .append("another key?", "oh yeah");

        assertThat(fluent).isEqualTo(nonFluent);
    }

    @Test
    public void fluentUnmodifiable() {
        Map<String, Integer> immutable = new Fluent.ConcurrentHashMap<String, Integer>(3)
            .append("one", 1)
            .append("two", 2)
            .append("three", 3)
            .unmodifiable();

        assertThat(immutable).hasSize(3);

        assertThatThrownBy(() -> immutable.put("four", 4))
            .isInstanceOf(RuntimeException.class);

        assertThat(immutable).hasSize(3);
    }

    enum Inner {
        KEY1, KEY2, KEY3
    }
}
