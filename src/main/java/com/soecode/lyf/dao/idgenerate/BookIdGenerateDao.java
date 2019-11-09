package com.soecode.lyf.dao.idgenerate;

import com.soecode.lyf.entity.idgenarate.BookIdGenerate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BookIdGenerateDao {
    int replace(@Param("params")Map<String, Object> map);

    List<BookIdGenerate> selectAll(@Param("params")Map<String, Object> map);
}
