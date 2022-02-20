package com.kifuan.dymethod;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class DynamicOverloadedMethods implements DynamicMethod {
    private final List<DynamicSingleMethod> methods;

    DynamicOverloadedMethods(List<DynamicSingleMethod> methods) {
        this.methods = methods;
    }

    private Optional<DynamicSingleMethod> getMostClosedSingleMethod(Class<?>... classes) {
        return methods.stream().filter(m -> m.isCompatible(classes))
                .min(Comparator.comparingInt(m -> m.getDistance(classes)));
    }

    /**
     * Calls the method whose distance is lowest for given objects.
     *
     * @param args the arguments for calling method.
     * @return the result of calling method.
     */
    @Override
    public Object call(Object... args) {
        Class<?>[] classes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

        Optional<DynamicSingleMethod> method = getMostClosedSingleMethod(classes);
        return method.orElseThrow(() -> new IllegalStateException("no such method")).call(args);
    }

    @Override
    public Method getMostClosed(Class<?>... classes) {
        Optional<DynamicSingleMethod> m = getMostClosedSingleMethod(classes);
        if (m.isEmpty()) {
            return null;
        }
        return m.get().getMethod();
    }

    @Override
    public String toString() {
        return "DynamicOverloadedMethods {\n\t"
                + methods.stream().map(DynamicSingleMethod::toString)
                    .collect(Collectors.joining("\n\t")) +
                "\n}";
    }
}
