package com.bsl.service.rawmaterialsmanager.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsl.common.pojo.BSLException;
import com.bsl.common.utils.BSLResult;
import com.bsl.mapper.BslBsPlanInfoMapper;
import com.bsl.mapper.BslProductInfoMapper;
import com.bsl.pojo.BslBsPlanInfo;
import com.bsl.pojo.BslProductInfo;
import com.bsl.pojo.BslProductInfoExample;
import com.bsl.pojo.BslProductInfoExample.Criteria;
import com.bsl.select.DictItemOperation;
import com.bsl.select.ErrorCodeInfo;
import com.bsl.select.QueryCriteria;
import com.bsl.service.rawmaterialsmanager.RawCompareService;
import com.google.common.base.CaseFormat;

/**
 * 重量比对实现类
 * duk-20190319
 */
@Service
public class RawCompareServiceImpl implements RawCompareService {

	@Autowired	 
	BslProductInfoMapper bslProductInfoMapper;
	@Autowired	 
	BslBsPlanInfoMapper bslBsPlanInfoMapper;
	
	/**
	 * 根据原料入库通知单号查询信息
	 */
	@Override
	public BSLResult getBsInfoService(QueryCriteria queryCriteria) {
		//根据原料入库通知单号查询信息
		if(StringUtils.isBlank(queryCriteria.getProdPlanNo()) && StringUtils.isBlank(queryCriteria.getProdId())){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空,"原料入库通知单号和原料物料编码不能同时为空！");
		}
		BslBsPlanInfo bslBsPlanInfo ;
		if(!StringUtils.isBlank(queryCriteria.getProdPlanNo())){
			bslBsPlanInfo = bslBsPlanInfoMapper.selectByPrimaryKey(queryCriteria.getProdPlanNo());
		}else{
			BslProductInfo bslProductInfo = bslProductInfoMapper.selectByPrimaryKey(queryCriteria.getProdId());
			if(bslProductInfo == null){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"原料物料编码不存在！");
			}
			bslBsPlanInfo = bslBsPlanInfoMapper.selectByPrimaryKey(bslProductInfo.getProdPlanNo());
		}
		if(bslBsPlanInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"原料入库通知单号不存在！");
		}
		if(!DictItemOperation.通知单状态_已完成.equals(bslBsPlanInfo.getBsStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误,"未完成的原料入库通知单号不支持比对！");
		}
		return BSLResult.ok(bslBsPlanInfo);
	}

	/**
	 * 根据原料入库通知单号查询卷板信息
	 */
	@Override
	public BSLResult getProdInfoService(QueryCriteria queryCriteria) {
		
		//创建查询的实例，并赋值
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_卷板);
		criteria.andProdSourceNotEqualTo(DictItemOperation.产品来源_剩余入库);
		if(!StringUtils.isBlank(queryCriteria.getProdPlanNo())){
			criteria.andProdPlanNoEqualTo(queryCriteria.getProdPlanNo());
		}
		if(!StringUtils.isBlank(queryCriteria.getProdId())){
			criteria.andProdIdEqualTo(queryCriteria.getProdId());
		}
		//调用sql查询
		if(StringUtils.isBlank(queryCriteria.getSort()) || StringUtils.isBlank(queryCriteria.getOrder())){
			bslProductInfoExample.setOrderByClause("`prod_plan_no` desc,`prod_id` desc");
		}else{
			String sortSql = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, queryCriteria.getSort());
			if(!StringUtils.isBlank(sortSql)){	
				bslProductInfoExample.setOrderByClause("`"+sortSql+"` "+ queryCriteria.getOrder());
			}
		}
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		return BSLResult.ok(bslProductInfos);
	}

}
