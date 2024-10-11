# Java Regular Expression (Regex) line ending behaviour

Example code for a blog post about using Java's [Regular Expression line ending Boundary Matchers](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html#lt)
(`$`, `\z` and `\Z`) and Multiline mode with [Pattern.matches()](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html#matches(java.lang.String,java.lang.CharSequence)) 
and [Matcher.find()](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html#matches(java.lang.String,java.lang.CharSequence)). 

- https://alansegar.co.uk/apl/2024/10/11/java-regular-expression-line-ending-matchers/

Background information:
- https://github.com/openjdk/jdk/commit/35acb891660fd5e0fee48b56acb16a6a193417ed
- https://bugs.openjdk.org/browse/JDK-8296292