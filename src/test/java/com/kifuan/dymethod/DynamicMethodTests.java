package com.kifuan.dymethod;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DynamicMethodTests {
    @Test
    public void testInstanceMethods() {
        DynamicMethod ctor = DynamicMethod.getConstructors(ArrayList.class);
        @SuppressWarnings("unchecked")
        List<Integer> list = (List<Integer>) ctor.call();

        DynamicMethod add = DynamicMethod.getInstanceMethods(list, "add");
        add.call(114);
        add.call(514);
        add.call(0, 1919810);
        Assert.assertEquals(list, List.of(1919810, 114, 514));

        DynamicMethod get = DynamicMethod.getInstanceMethods(list, "get");
        Assert.assertEquals(1919810, get.call(0));
    }

    @Test
    public void testStaticMethods() {
        DynamicMethod valueOf = DynamicMethod.getStaticMethods(String.class, "valueOf");

        Assert.assertEquals("114514", valueOf.call(114514));
        Assert.assertEquals("191981.0", valueOf.call(191981.0));
        Assert.assertEquals("true", valueOf.call(true));

        // Tries to call a method which is not exist.
        Assert.assertThrows(IllegalStateException.class, () -> valueOf.call(1, 1, 4, 5, 1, 4));

        // Tries to call non-static method without instance
        Assert.assertThrows(IllegalStateException.class, () -> DynamicMethod.getStaticMethods(Object.class, "toString"));
    }

    @Test
    public void testCompatibles() {
        DynamicMethod parseInt = DynamicMethod.getStaticMethods(Integer.class, "parseInt");

        // It can process primitive values.
        Assert.assertTrue(parseInt.hasCompatible(String.class, int.class));
        Assert.assertTrue(parseInt.hasCompatible(String.class, Integer.class));

        Assert.assertTrue(parseInt.hasCompatible(String.class));
        Assert.assertFalse(parseInt.hasCompatible(double.class));

    }

    @Test
    public void testConstructors() {
        DynamicMethod ctor = DynamicMethod.getConstructors(ArrayList.class);
        @SuppressWarnings("unchecked")
        List<Integer> list = (List<Integer>) ctor.call();

        list.add(114);
        list.add(514);
        list.add(1919810);

        Assert.assertEquals(List.of(114, 514, 1919810), list);
    }
}
