package com.soecode.lyf;

import java.text.MessageFormat;

public class TableGenerate {
    public static final String TABLE_BOOK_TEMPLATE =
            "CREATE TABLE `book_{0}` (\n" +
                    "  `book_id` bigint(20) NOT NULL COMMENT #图书ID#,\n" +
                    "  `name` varchar(100) NOT NULL COMMENT #图书名称#,\n" +
                    "  `number` int(11) NOT NULL COMMENT #馆藏数量#,\n" +
                    "  PRIMARY KEY (`book_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=#图书表#;";

    public static void main(String[] args) {
        for(int i=0;i < 128;i++){
            System.out.println(MessageFormat.format(TABLE_BOOK_TEMPLATE,i));
        }
    }
}
