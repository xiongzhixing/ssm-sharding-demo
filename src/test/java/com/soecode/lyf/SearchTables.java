package com.soecode.lyf;

import org.apache.commons.lang3.time.DateUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * @author 熊志星
 * @description
 * @date 2019/8/22
 */
public class SearchTables {
    private final static int DATABASES_NUM = 4;
    private final static int TABLES = 250;

    private final static String USER_VIP_RECORD_TEMPLATE = "select * from db_vip_order_{0}.vip_refund_record_{1} union \n";

    public static void main(String[] args) throws ParseException {
        /*StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < DATABASES_NUM;i++){
            for(int j = 0;j < TABLES;j++){
                stringBuilder.append(MessageFormat.format(USER_VIP_RECORD_TEMPLATE,i,j));
            }
        }
        System.out.println(stringBuilder);*/

        /*Date date = DateUtils.addDays(new Date(),10);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse("2019-08-26 10:30:12");
        Date date2 = sdf.parse("2019-08-27 12:30:12");

        System.out.println(DateUtils.isSameDay(date1,date2));*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse("2019-08-25 10:30:12");

        Iterator<Calendar> iterator = DateUtils.iterator(date1,DateUtils.RANGE_MONTH_MONDAY);
        while(iterator.hasNext()){
            System.out.println(sdf.format(iterator.next().getTime()));
        }




    }

}