# Dynamic Method-Calling for Java

## Introduction

This is library for **calling methods with name only**.(Inspired by `nashorn`, which is a JS engine in Java, and it can call Java methods dynamically including overloaded methods)

Though there must be an implementation, I want to make one which is easier to use and understand the principles.

## Usage

Add this to pom.xml under `<dependencies>` tag.

```xml
<dependency>
    <groupId>io.github.kifuan</groupId>
    <artifactId>dymethod</artifactId>
    <version>1.2.1</version>
</dependency>
```

## Examples

All codes here could be seen in test cases except print statements.

1. Call non-static methods

```java
List<Integer> list = new ArrayList<>();

DynamicMethod add = DynamicMethod.getInstanceMethods(list, "add");
add.call(114);
add.call(514);
// Calls overloaded method
add.call(0, 1919810);

// [1919810, 114, 514]
System.out.println(list);

DynamicMethod get = DynamicMethod.getInstanceMethods(list, "get");
// 1919810
System.out.println(get.call(0));
```

2. Call static methods

```java
DynamicMethod valueOf = DynamicMethod.getStaticMethods(String.class, "valueOf");

// class java.lang.String
System.out.println(valueOf.call(114514).getClass());
// 114514
System.out.println(valueOf.call(114514));

// It will throw an exception, as there is no compatible method for 6 ints.
System.out.println(valueOf.call(1, 1, 4, 5, 1, 4));
```

3. Call non-static method but give no instance

```java
// It will throw an exception, as we did not give the instance.
DynamicMethod toString = DynamicMethod.getStaticMethods(Object.class, "toString");

// The sentence below will never exceute.
System.out.println(toString.call());
```

4. Compatibility checks

```java
// Note that the Integer.class below could not be replaced by int.class,
// because primitive types have no methods like parseInt
DynamicMethod parseInt = DynamicMethod.getStaticMethods(Integer.class, "parseInt");

// Both are true, as it can process primitive values.
System.out.println(parseInt.hasCompatible(String.class, int.class));
System.out.println(parseInt.hasCompatible(String.class, Integer.class));

// true
System.out.println(parseInt.hasCompatible(String.class));

// false, as there is no parseInt(double)
System.out.println(parseInt.hasCompatible(double.class));
```

5. Call constructors

```java
DynamicMethod ctor = DynamicMethod.getConstructors(ArrayList.class);
@SuppressWarnings("unchecked")
List<Integer> list = (List<Integer>) ctor.call();

list.add(114);
list.add(514);
list.add(1919810);

// [114, 514, 1919810]
System.out.println(list);
```

