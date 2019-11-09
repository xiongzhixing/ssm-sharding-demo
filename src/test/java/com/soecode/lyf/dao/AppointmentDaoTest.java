package com.soecode.lyf.dao;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.dao.deal.AppointmentDao;
import com.soecode.lyf.entity.deal.AppointmentDO;
import com.soecode.lyf.entity.deal.LinkInfoDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.soecode.lyf.BaseTest;

import java.util.Arrays;
import java.util.List;

public class AppointmentDaoTest extends BaseTest {
    @Autowired
    private AppointmentDao appointmentDao;
    
    @Test
    public void insert() {
        AppointmentDO appointmentDO = new AppointmentDO();
        appointmentDO.setBookId(1l);
        appointmentDO.setStudentId(90l);
        appointmentDO.setLinkInfoDOList(Arrays.asList(
                LinkInfoDO.builder()
                        .id(1)
                        .name("name")
                        .url("http://www.baidu.com")
                        .build(),
                LinkInfoDO.builder()
                        .id(2)
                        .name("name")
                        .url("http://www.jiangxi.com")
                        .build()
        ));

        this.appointmentDao.insert(appointmentDO);
    }

    @Test
    public void insertSelective() {
        return ;
    }

    @Test
    public void selectAll() {
        System.out.println(JSON.toJSONString(this.appointmentDao.selectAll()));
        return;
    }
}
