package com.soecode.lyf.handler;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.entity.deal.LinkInfoDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinkInfoHandler implements TypeHandler<List<LinkInfoDO>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<LinkInfoDO> linkInfoDOS, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(Optional.ofNullable(linkInfoDOS).orElse(new ArrayList<>(1))));
    }

    @Override
    public List<LinkInfoDO> getResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return JSON.parseArray(StringUtils.isBlank(result) ? "[]" : result ,LinkInfoDO.class);
    }

    @Override
    public List<LinkInfoDO> getResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return JSON.parseArray(StringUtils.isBlank(result) ? "[]" : result,LinkInfoDO.class);
    }

    @Override
    public List<LinkInfoDO> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return JSON.parseArray(StringUtils.isBlank(result) ? "[]":result,LinkInfoDO.class);
    }
}
