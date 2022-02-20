package com.kifuan.dymethod;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DynamicMethodTests {
    @Test
    public void testInstanceMethods() {
        List<Integer> list = new ArrayList<>();

        DynamicMethod add = DynamicMethod.getInstance(list, "add");
        add.call(114);
        add.call(514);
        add.call(0, 1919810);
        Assert.assertEquals(list, List.of(1919810, 114, 514));

        DynamicMethod get = DynamicMethod.getInstance(list, "get");
        Assert.assertEquals(1919810, get.call(0));
    }

    @Test
    public void testStaticMethods() {
        DynamicMethod valueOf = DynamicMethod.getInstance(String.class, "valueOf");

        Assert.assertEquals("114514", valueOf.call(114514));
        Assert.assertEquals("191981.0", valueOf.call(191981.0));
        Assert.assertEquals("true", valueOf.call(true));

        // Tries to call a method which is not exist.
        Assert.assertThrows(IllegalStateException.class, () -> valueOf.call(1, 1, 4, 5, 1, 4));

        // Tries to call non-static method without instance
        Assert.assertThrows(IllegalStateException.class, () -> DynamicMethod.getInstance(Object.class, "toString"));
    }

    @Test
    public void testCompatibles() {
        DynamicMethod parseInt = DynamicMethod.getInstance(Integer.class, "parseInt");

        // It can process primitive values.
        Assert.assertTrue(parseInt.hasCompatible(String.class, int.class));
        Assert.assertTrue(parseInt.hasCompatible(String.class, Integer.class));

        Assert.assertTrue(parseInt.hasCompatible(String.class));
        Assert.assertFalse(parseInt.hasCompatible(double.class));

    }
}
