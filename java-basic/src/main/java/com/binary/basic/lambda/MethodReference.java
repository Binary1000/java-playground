package com.binary.basic.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Binary on 2020/5/27
 */
public class MethodReference {

    public static void main(String[] args) {
        Integer[] integers = new Integer[] {23, 543, 435, 5345, 12};

        // 除了lambda表达式，还可以直接传入静态方法引用
        Arrays.sort(integers, MethodReference::compare);

        // 也可以直接传入实例方法引用， 因为实例方法有一个隐含的this参数，在调用实例方法时，第一个隐含参数问题传入this，相当于静态方法 public static int compare(String this, String o);
        String[] strings = new String[] {"das", "asd", "ba", "trw"};
        Arrays.sort(strings, String::compareTo);

        List<String> list = new ArrayList<>();
        list.add("ads");
        list.add("3123");
        list.add("312");

        list.forEach(System.out::println);

        Stream.generate(() -> {
           return 1;
        });
    }

    public static int compare(Integer a, Integer b) {
        return a - b;
    }

    public static void test() {

    }

}

class Person {

    String name;
    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person:" + this.name;
    }
}