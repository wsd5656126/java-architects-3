package com.wusd.map;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {
    }

    public void testArrayListMap() {
        ArrayListMap<Integer, Integer> map = new ArrayListMap<>();
        for (int i = 0; i < 10000; i++) {
            map.put(i, i);
        }
        for (int i = 0; i < 10000; i++) {
            System.out.println("key:" + i + ", value:" + map.get(i));
        }
    }
}
