package com.soecode.lyf.dao.deal;


import com.soecode.lyf.entity.deal.AppointmentDO;

import java.util.List;

public interface AppointmentDao {
    int insert(AppointmentDO record);
    int insertSelective(AppointmentDO record);
    List<AppointmentDO> selectAll();
}