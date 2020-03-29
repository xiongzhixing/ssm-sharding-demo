package com.soecode.lyf;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/3/28 0028
 **/
public class Test {
    public static void main(String[] args) {
        while(true){
            System.out.println("test");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}