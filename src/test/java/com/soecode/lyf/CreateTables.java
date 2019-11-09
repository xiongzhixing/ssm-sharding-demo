package com.soecode.lyf;

import java.text.MessageFormat;

public class CreateTables {
    private final static String table_template = "CREATE TABLE ssm{0}.book_{1} (\n"
                                                 + "  `book_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT \"图书ID\",\n"
                                                 + "  `name` varchar(100) NOT NULL COMMENT \"图书名称\",\n"
                                                 + "  `number` int(11) NOT NULL COMMENT \"馆藏数量\",\n"
                                                 + "  PRIMARY KEY (`book_id`)\n"
                                                 + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=\"图书表\";\n";

    private final static String flush_db = "delete from book_{0};\n";

    public static void main(String[] args) {
        /*for(int i= 0;i < 4;i++){
            StringBuilder stringBuilder = new StringBuilder();
            for(int j= 0;j < 128;j++){
                String table = MessageFormat.format(table_template,i,j);
                stringBuilder.append(table);
            }
            System.out.println(stringBuilder.toString());

            System.out.println("===========================================\n\n\n");
        }*/
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0;i < 128;i++){
            stringBuilder.append(MessageFormat.format(flush_db,i));
        }
        System.out.println(stringBuilder.toString());
    }
}
