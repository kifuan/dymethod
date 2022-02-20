package com.kifuan.dymethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface ReflectionMethod {
    void setAccessible(boolean flag);

    Object invoke(Object obj, Object... args);

    int getModifiers();

    Class<?>[] getParameterTypes();

    boolean isConstructor();

    static ReflectionMethod getInstance(Method method) {
        return new ReflectionMethod() {
            @Override
            public void setAccessible(boolean flag) {
                method.setAccessible(flag);
            }

            @Override
            public Object invoke(Object obj, Object... args) {
                try {
                    return method.invoke(obj, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int getModifiers() {
                return method.getModifiers();
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return method.getParameterTypes();
            }

            @Override
            public boolean isConstructor() {
                return false;
            }
        };
    }

    static ReflectionMethod getInstance(Constructor<?> ctor) {
        return new ReflectionMethod() {
            @Override
            public void setAccessible(boolean flag) {
                ctor.setAccessible(flag);
            }

            @Override
            public Object invoke(Object obj, Object... args) {
                try {
                    return ctor.newInstance(args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int getModifiers() {
                return ctor.getModifiers();
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return ctor.getParameterTypes();
            }

            @Override
            public boolean isConstructor() {
                return true;
            }
        };
    }
}
