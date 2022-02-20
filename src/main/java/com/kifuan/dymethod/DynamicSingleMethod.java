package com.kifuan.dymethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;


final class DynamicSingleMethod implements DynamicMethod {
    private final Method method;
    private final Object object;

    private static final Map<Class<?>, Class<?>> primitives = Map.of(
            int.class, Integer.class,
            float.class, Float.class,
            double.class, Double.class,
            long.class, Long.class,
            boolean.class, Boolean.class,
            short.class, Short.class,
            byte.class, Byte.class,
            void.class, Void.class
    );

    DynamicSingleMethod(Method method, Object object) {
        this.method = method;
        this.object = object;

        if (!Modifier.isStatic(method.getModifiers()) && object == null) {
            throw new IllegalStateException("given method " + method + " is not static but there is no instance provided");
        }
    }


    private static Class<?> getBoxed(Class<?> cls) {
        return primitives.getOrDefault(cls, cls);
    }

    /**
     * Gets the distance between two classes.
     *
     * <p>
     * It will return {@code Integer.MAX_VALUE} when
     * {@code target} is not super class of {@code src}.
     *
     * The distance can reflect the relationship of two classes.
     * </p>
     *
     * <p>
     * In a word, the distance gets lower as the
     * relationship get more closed.
     * </p>
     *
     * @param target the target class(should be super class of or the same as src)
     * @param src the source class
     * @return the distance between target and src
     */
    private static int getDistance(Class<?> target, Class<?> src) {
        // Make sure there is no primitive classes(those who have no super classes)
        src = getBoxed(src);
        target = getBoxed(target);

        if (!target.isAssignableFrom(src)) {
            return Integer.MAX_VALUE;
        }

        int distance = 0;
        while (src != target) {
            src = src.getSuperclass();
            distance++;
        }

        return distance;
    }


    int getDistance(Class<?>[] classes) {
        Class<?>[] targets = method.getParameterTypes();
        int res = 0;
        for (int i = 0; i < targets.length; i++) {
            res += getDistance(targets[i], classes[i]);
        }
        return res;
    }

    boolean isCompatible(Class<?>[] classes) {
        Class<?>[] targets = method.getParameterTypes();
        if (classes.length != targets.length) {
            return false;
        }
        for (int i = 0; i < classes.length; i++) {
            Class<?> target = getBoxed(targets[i]), obj = getBoxed(classes[i]);

            if (!target.isAssignableFrom(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calls the method this object contains.
     *
     * @param args arguments for calling method
     * @return the result of calling method
     */
    @Override
    public Object call(Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    Method getMethod() {
        return method;
    }

    @Override
    public Method getMostClosed(Class<?>... classes) {
        return method;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}
