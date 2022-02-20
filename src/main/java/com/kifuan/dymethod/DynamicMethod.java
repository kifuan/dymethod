package com.kifuan.dymethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface DynamicMethod {
    /**
     * Calls the method with given arguments.
     *
     * @param args arguments for calling method
     * @return the result of calling method
     */
    Object call(Object... args);

    /**
     * Gets the most closed method of given argument classes.
     *
     * @param classes the classes of arguments
     * @return the most closed method or null if there is not yet
     */
    Method getMostClosed(Class<?>... classes);

    /**
     * Gets whether given argument classes have a compatible method.
     * In other words, gets whether given classes of arguments can
     * be called without exceptions.
     *
     * @param classes the argument classes
     * @return whether given classes have a compatible method.
     */
    default boolean hasCompatible(Class<?>... classes) {
        return getMostClosed(classes) != null;
    }

    /**
     * Gets a static method.
     *
     * @param cls the class to get the static method
     * @param name the name of the static method
     * @return the static method
     */
    static DynamicMethod getInstance(Class<?> cls, String name) {
        return getInstance(cls, null, name);
    }

    /**
     * Gets a instance method.
     *
     * @param obj the object to get the instance method
     * @param name the name of the instance method
     * @return the instance method
     */
    static DynamicMethod getInstance(Object obj, String name) {
        return getInstance(obj.getClass(), obj, name);
    }

    private static DynamicMethod getInstance(Class<?> cls, Object obj, String name) {
        List<DynamicSingleMethod> methods = Arrays.stream(cls.getMethods())
                .filter(m -> m.getName().equals(name))
                .map(m -> new DynamicSingleMethod(m, obj))
                .collect(Collectors.toList());

        if (methods.isEmpty()) {
            throw new IllegalStateException("no such method");
        } else if (methods.size() == 1) {
            return methods.get(0);
        } else {
            return new DynamicOverloadedMethods(methods);
        }
    }
}
