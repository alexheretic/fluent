import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static alexh.Unchecker.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UncheckerTest {

    private boolean workingTests = true;

    @Test(expected = RuntimeException.class)
    public void uncheckRunnableLambda_throws() {
        uncheck(() -> {
            if (workingTests)
                throw new IOException("IO error");
        }).run();
    }

    @Test
    public void uncheckRunnableLambda_works() {
        AtomicInteger i = new AtomicInteger(0);
        uncheck(() -> {
            if (!workingTests)
                throw new IOException("IO error");
            i.incrementAndGet();
        }).run();
        assertThat(i.get(), is(1));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckSupplierLambda_throws() {
        uncheck(() -> {
            if (workingTests)
                throw new IOException("IO error");
            return "321";
        }).get();
    }

    @Test
    public void uncheckSupplierLambda_works() {
        assertThat(uncheck(() -> {
            if (!workingTests)
                throw new IOException("IO error");
            return "321";
        }).get(), is("321"));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckConsumerLambda_throws() {
        uncheck((String s) -> {
            if (s.equals("hello")) throw new IOException("IO error");
        }).accept("hello");
    }

    @Test
    public void uncheckConsumerLambda_works() {
        AtomicReference<String> str = new AtomicReference<>("foo");
        uncheck((String s) -> {
            if (s.equals("hello")) throw new IOException("IO error");
            str.set(s);
        }).accept("bar");

        assertThat(str.get(), is("bar"));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckBiConsumerLambda_throws() {
        uncheck((String s, Integer i) -> {
            if (s.equals("hello") && i > 0) throw new IOException("IO error");
        }).accept("hello", 123);
    }

    @Test
    public void uncheckBiConsumerLambda_works() {
        List<Object> list = new ArrayList<>();
        uncheck((String s, Integer i) -> {
            if (s.equals("hello") && i > 0) throw new IOException("IO error");
            list.add(s);
            list.add(i);
        }).accept("hello", -123);

        assertThat(list.get(0), is("hello"));
        assertThat(list.get(1), is(-123));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckFunctionLambda_throws() {
        uncheck((String s) -> {
            if (s.equals("hello")) throw new IOException("IO error");
            return s.length();
        }).apply("hello");
    }

    @Test
    public void uncheckFunctionLambda_works() {
        assertThat(uncheck((String s) -> {
            if (s.equals("hello")) throw new IOException("IO error");
            return s.length();
        }).apply("world"), is(5));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckBiFunctionLambda_throws() {
        uncheck((String s, Integer i) -> {
            if (s.equals("hello") && i > 0) throw new IOException("IO error");
            return s.length() + i;
        }).apply("hello", 123);
    }

    @Test
    public void uncheckBiFunctionLambda_works() {
        assertThat(uncheck((String s, Integer i) -> {
            if (s.equals("hello") && i > 0) throw new IOException("IO error");
            return s.length() + i;
        }).apply("world", 123), is(128));
    }

    @Test(expected = RuntimeException.class)
    public void unchecked_throws() {
        unchecked(() -> {
            if (workingTests)
                throw new IOException("IO error");
        });
    }

    @Test
    public void unchecked_works() {
        AtomicInteger i = new AtomicInteger(0);
        unchecked(() -> {
            if (!workingTests)
                throw new IOException("IO error");
            i.incrementAndGet();
        });

        assertThat(i.get(), is(1));
    }

    @Test(expected = RuntimeException.class)
    public void uncheckedGet_throws() {
        uncheckedGet(() -> {
            if (workingTests)
                throw new IOException("IO error");
            return "hello";
        });
    }

    @Test
    public void uncheckedGet_works() {
        assertThat(uncheckedGet(() -> {
            if (!workingTests)
                throw new IOException("IO error");
            return "hello";
        }), is("hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void uncheckWithCustomExceptionTransformer() {
        unchecked(() -> {
            throw new IOException("IO error!");
        }, IllegalArgumentException::new);
    }

    @Test(expected = RuntimeException.class)
    public void uncheckAndGetThrowableMethod() {
        uncheck(ThrowingUtility::throwSomethingNeverReturn).get();
    }

    @Test(expected = RuntimeException.class)
    public void uncheckedGetThrowableMethod() {
        uncheckedGet(ThrowingUtility::throwSomethingNeverReturn);
    }

    @Test(expected = RuntimeException.class)
    public void uncheckAndRunThrowableMethod() {
        uncheck(ThrowingUtility::throwSomething).run();
    }

    @Test(expected = RuntimeException.class)
    public void uncheckedThrowableMethod() {
        unchecked(ThrowingUtility::throwSomething);
    }

    static class ThrowingUtility {

        static String throwSomethingNeverReturn() throws Throwable {
            throw new Throwable("YOU MUST HANDLE ME NAOH");
        }

        static void throwSomething() throws Throwable {
            throw new Throwable("YOU MUST WRITE MY NAME ALL THE WAY UP THE STACK");
        }
    }
}
