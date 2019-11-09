package com.soecode.lyf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDeepCopy {
    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People("123"));
        peopleList.add(new People("456"));


        List<People> targetPeopleList = new ArrayList<>(peopleList);

        List<Integer> intList = new ArrayList<>();
        intList.add(new Integer(1));
        intList.add(new Integer(2));

        List<Integer> targetIntList = new ArrayList<>(intList);

        System.out.println();
    }
    static class People{
        private String name;

        public People(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
