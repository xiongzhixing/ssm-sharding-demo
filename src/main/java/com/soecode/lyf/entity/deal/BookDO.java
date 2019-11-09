package com.soecode.lyf.entity.deal;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class BookDO implements Serializable{
    private static final long serialVersionUID = 1654164416408460552L;
    private Long bookId;
    private String name;
    private Integer number;
}