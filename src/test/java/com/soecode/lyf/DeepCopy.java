package com.soecode.lyf;

import org.springframework.beans.BeanUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 熊志星
 * @description
 * @date 2019/9/5
 */
public class DeepCopy {

    public static Object copy(Object obj) {
        Object cloneObj = null;

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            //写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);

            //分配内存,写入原始对象,生成新对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//获取上面的输出字节流
            ois = new ObjectInputStream(bais);

            //返回生成的新对象
            cloneObj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cloneObj;
    }



    public static void main(String[] args) {
        List<String> assetes = new ArrayList<>();
        assetes.add("house");
        assetes.add("car");

        People people = new People("xzx",assetes);
        People people1 = (People) DeepCopy.copy(people);

        People people2 = new People();
        BeanUtils.copyProperties(people,people2);

        System.out.println();

      /*  List<People> peopleList = new ArrayList<>();
        peopleList.add(new People("name"));
        peopleList.add(new People("age"));

        List<People> peopleList1 = (List<People>)DeepCopy.copy(peopleList);


        List<People> peopleList2 = new ArrayList<>();
        System.out.println();*/
    }

    static class People implements Serializable{
        private String name;
        private List<String> assetes;

        public People(String name, List<String> assetes) {
            this.name = name;
            this.assetes = assetes;
        }

        public People() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAssetes() {
            return assetes;
        }

        public void setAssetes(List<String> assetes) {
            this.assetes = assetes;
        }
    }
}