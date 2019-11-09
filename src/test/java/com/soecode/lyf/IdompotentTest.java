package com.soecode.lyf;

/**
 * @author 熊志星
 * @description
 * @date 2019/9/5
 */
public class IdompotentTest {
    private final static int UPDATE_OWN_ORDER = 1;
    private final static int SYNC_BASE_ORDER = 2;
    private final static int SYNC_THRID_ORDER = 4;

    public static void main(String[] args) {
        int step = 5;

        if((step & UPDATE_OWN_ORDER) == 0){
            System.out.println("更新自身订单状态");
            step = step | UPDATE_OWN_ORDER;
        }

        if((step & SYNC_BASE_ORDER) == 0){
            System.out.println("更新基础订单状态");
            step = step | SYNC_BASE_ORDER;
        }

        if((step & SYNC_THRID_ORDER) == 0){
            System.out.println("更新基础订单状态");
            step = step | SYNC_THRID_ORDER;
        }

        System.out.println("---------------step：" + step);
    }

}