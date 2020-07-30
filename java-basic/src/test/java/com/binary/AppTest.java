package com.binary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        List<String> list = new ArrayList<>();
        list.add("asdas");
        list.add("13");
        list.add("3123");
        System.out.println(list.size());
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            iterator.remove();
        }
        System.out.println(list.size());
    }
}
