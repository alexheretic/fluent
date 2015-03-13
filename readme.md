Fluent
------

Fluent is an ultra-lightweight Java library to aid fluent coding in Java 8 onwards

### Map Building
Classes to aid Map building in a declarative way. A bit nicer than the anonymous sub-class with initializer block approach, don't you think?

```java
// simple usage, assuming someMap is a Map<String, String> already declared
Map<String, String> example = new Fluent.LinkedHashMap<String, String>()
    .append("key1", "val1")
    .append("key2", "val2")
    .appendAll(someMap);

// nested structures declared all at once
Map<String, Object> users = new Fluent.HashMap<String, Object>()
    .append("David", new Fluent.HashMap<>()
        .append("customers", asList(
                new Fluent.HashMap<>()
                    .append("name", "Darrel")
                    .append("age", 33),
                new Fluent.HashMap<>()
                    .append("name", "John")
                    .append("age", 29)
            )
        )
        .append("keywords", asList("word1", "word2"))
    )
    .append("Karen", new Fluent.HashMap<>()
        .append("customers", emptyList())
        .append("keywords", asList("word33"))
    );
```

### Checked Exception Handling With Functional Wrapping
Utility methods to wrap the propagation of a possible checked exception in an unchecked one. 
Particularly useful in making fluent functional code involving checked exceptions not a horror to behold.

```java
// in short Unchecker#unchecked lets you do this
unchecked(() -> methodThatThrowsACheckedException());

// which is equivalent to
try {
    methodThatThrowsACheckedException();
}
catch (RuntimeException | Error e) {
    throw e;
}
catch (Throwable t) {
    throw new RuntimeException(t);
}
```

```java
/*
 * Unchecker#uncheckedGet returns a throwing supplier's result
 *          (wrapping checked in RuntimeExceptions by default)
 * Unchecker#uncheck transforms a throwing lambda/method into a unchecked one
 */
List<String> uncheckThrower(CheckMe object) {
    return uncheckedGet(() -> {
        object.youMustHandleMyErrorCases();
        return object.atLeastWriteThrowsAllOverYourCode().stream()
            .map(uncheck(ImportantUtility::dontIgnoreMeeeee))
            .collect(toList());
    });
}

// instead of this...
List<String> uncheckThrowerManual(CheckMe object) {
    try {
        object.youMustHandleMyErrorCases();
        return object.atLeastWriteThrowsAllOverYourCode().stream()
            .map(number -> {
                try {
                    return ImportantUtility.dontIgnoreMeeeee(number);
                }
                catch (IOException | GeneralSecurityException | NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            })
            .collect(toList());
    }
    catch (RuntimeException | Error e) {
        throw e;
    }
    catch (Throwable throwable) {
        throw new RuntimeException(throwable);
    }
}

// some Exception throwing instance
static interface CheckMe {
    void youMustHandleMyErrorCases() throws Exception;
    List<Integer> atLeastWriteThrowsAllOverYourCode() throws Throwable;
}

// some exception throwing utility class
static class ImportantUtility {
    public static String dontIgnoreMeeeee(Integer i)
        throws IOException, GeneralSecurityException, NoSuchMethodException {
        /* some important logic ... */
    }
}
```


Fluent is licensed under the [Apache 2.0 licence](http://www.apache.org/licenses/LICENSE-2.0.html).

### Releases

1.0 is the current latest release. Requiring JDK 1.8 or later.