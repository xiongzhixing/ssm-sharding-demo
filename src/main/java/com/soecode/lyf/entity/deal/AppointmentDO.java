package com.soecode.lyf.entity.deal;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(callSuper = true)
public class AppointmentDO implements Serializable{
    private static final long serialVersionUID = 1041505300496983686L;
    private Long bookId;
    private Long studentId;
    private List<LinkInfoDO> linkInfoDOList;
}