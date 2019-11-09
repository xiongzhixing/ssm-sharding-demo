package com.soecode.lyf.entity.deal;

import lombok.*;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkInfoDO implements Serializable {
    private static final long serialVersionUID = -5419492767065185103L;
    private Integer id;
    private String name;
    private String url;
}
