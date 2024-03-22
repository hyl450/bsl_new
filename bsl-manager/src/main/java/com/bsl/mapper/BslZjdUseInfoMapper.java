package com.bsl.mapper;

import com.bsl.pojo.BslZjdUseInfo;
import com.bsl.pojo.BslZjdUseInfoExample;
import com.bsl.pojo.BslZjdUseInfoKey;
import com.bsl.reportbean.BslRuInFo;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BslZjdUseInfoMapper {
    int countByExample(BslZjdUseInfoExample example);

    int deleteByExample(BslZjdUseInfoExample example);

    int deleteByPrimaryKey(BslZjdUseInfoKey key);

    int insert(BslZjdUseInfo record);

    int insertSelective(BslZjdUseInfo record);

    List<BslZjdUseInfo> selectByExample(BslZjdUseInfoExample example);

    BslZjdUseInfo selectByPrimaryKey(BslZjdUseInfoKey key);

    int updateByExampleSelective(@Param("record") BslZjdUseInfo record, @Param("example") BslZjdUseInfoExample example);

    int updateByExample(@Param("record") BslZjdUseInfo record, @Param("example") BslZjdUseInfoExample example);

    int updateByPrimaryKeySelective(BslZjdUseInfo record);

    int updateByPrimaryKey(BslZjdUseInfo record);
    
    BslRuInFo getMakeWeightByZjdId(String prodZjdId);
}