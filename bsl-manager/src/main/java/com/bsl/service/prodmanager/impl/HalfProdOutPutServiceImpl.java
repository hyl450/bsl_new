package com.bsl.service.prodmanager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bsl.common.pojo.BSLException;
import com.bsl.common.utils.BSLResult;
import com.bsl.dao.JedisClient;
import com.bsl.mapper.BslMakePlanInfoDetailMapper;
import com.bsl.mapper.BslMakePlanInfoMapper;
import com.bsl.mapper.BslProductInfoMapper;
import com.bsl.mapper.BslStockChangeDetailMapper;
import com.bsl.mapper.BslZjdUseInfoMapper;
import com.bsl.pojo.BslMakePlanInfo;
import com.bsl.pojo.BslProductInfo;
import com.bsl.pojo.BslProductInfoExample;
import com.bsl.pojo.BslStockChangeDetail;
import com.bsl.reportbean.BslTopTwoZjdInfo;
import com.bsl.pojo.BslProductInfoExample.Criteria;
import com.bsl.select.DictItemOperation;
import com.bsl.select.ErrorCodeInfo;
import com.bsl.select.QueryCriteria;
import com.bsl.service.enums.ParamService;
import com.bsl.service.prodmanager.HalfProdOutPutService;
import com.bsl.service.prodmanager.ProdPlanService;
import com.bsl.service.serach.PlanLunoInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;

/**
 * 半成品出库管理
 * duk-20190319
 */
@Service
public class HalfProdOutPutServiceImpl implements HalfProdOutPutService {

	@Autowired	 
	BslProductInfoMapper bslProductInfoMapper;
	@Autowired	 
	BslMakePlanInfoMapper bslMakePlanInfoMapper;
	@Autowired	 
	BslMakePlanInfoDetailMapper bslMakePlanInfoDetailMapper;
	@Autowired	 
	BslStockChangeDetailMapper bslStockChangeDetailMapper;
	@Autowired
	ProdPlanService prodPlanService;
	@Autowired
	ParamService paramService;
	@Autowired	 
	BslZjdUseInfoMapper bslZjdUseInfoMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private PlanLunoInfoService planLunoInfoService;
	
	@Value("${REDIS_NEXT_STOCKCHANGE_ID}")
	private String REDIS_NEXT_STOCKCHANGE_ID;
	
	/**
	 * 库存变动流水自动生成编号
	 * CH+日期+4位序号
	 * @return
	 */
	public String createStockChangeId() {
		long incr = jedisClient.incr(REDIS_NEXT_STOCKCHANGE_ID);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String rawId = String.format("CH%s%04d", sdf.format(new Date()), incr);
		return rawId;
	}

	/**
	 * 根据条件查询所有可以出库的半成品库存信息
	 */
	@Override
	public BSLResult getHalfProdPlanService(QueryCriteria queryCriteria) {
		//获取正在执行的产品生产指令
		BslMakePlanInfo prodPlanInfoExe = prodPlanService.getProdPlanInfoExe(queryCriteria.getPlanJz());
		if(prodPlanInfoExe == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到正在执行的生产指令，无法出库！");
		}
		queryCriteria.setProdMaterial(prodPlanInfoExe.getProdMaterial());
		queryCriteria.setProdNorm(prodPlanInfoExe.getProdNorm());
		queryCriteria.setProdOutPlan(prodPlanInfoExe.getPlanId());
		if (!StringUtils.isBlank(queryCriteria.getProdId())) {
			queryCriteria.setProdId("%"+ queryCriteria.getProdId()+"%");
		}else{
			queryCriteria.setProdId(null);
		}
		if (!StringUtils.isBlank(queryCriteria.getProdPlanNo())) {
			queryCriteria.setProdPlanNo("%"+ queryCriteria.getProdPlanNo()+"%");
		}else{
			queryCriteria.setProdPlanNo(null);
		}
		if (!StringUtils.isBlank(queryCriteria.getProdStatus())) {
			queryCriteria.setProdStatus(queryCriteria.getProdStatus());
		}else{
			queryCriteria.setProdStatus(null);
		}
		//分页处理
		PageHelper.startPage(Integer.parseInt(queryCriteria.getPage()), Integer.parseInt(queryCriteria.getRows()));
		//调用sql查询
		if(StringUtils.isBlank(queryCriteria.getSort()) || StringUtils.isBlank(queryCriteria.getOrder())){
			queryCriteria.setOrderByClause("`prod_id` asc,`prod_plan_no` desc");
		}else{
			String sortSql = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, queryCriteria.getSort());
			if(!StringUtils.isBlank(sortSql)){
				queryCriteria.setOrderByClause("`"+sortSql+"` "+ queryCriteria.getOrder());
			}
		}
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.getProdCanOutProds(queryCriteria);
		PageInfo<BslProductInfo> pageInfo = new PageInfo<BslProductInfo>(bslProductInfos);
		return BSLResult.ok(pageInfo.getTotal(),bslProductInfos);
		
	}

	/**
	 * 根据实体类获取产品信息
	 */
	@Override
	public BSLResult getHalfProdPlanByMakePlanInfoService(BslProductInfoExample bslProductInfoExample) {
		List<BslProductInfo> selectByExample = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		 return BSLResult.ok(selectByExample);
	}

	/**
	 * 出库/未用退回/完成
	 */
	@Override
	public BSLResult addHalfProdPlanInfo(String prodId,String planJz,String prodInputuser,Integer flag){
		
		//获取原半成品信息
		BslProductInfo bslProductInfoOld = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(bslProductInfoOld == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据对应盘号查询记录为空！");
		}
		
		String statusOld = bslProductInfoOld.getProdStatus();//原状态
		String luno = bslProductInfoOld.getProdLuno();//炉号
		String norm= bslProductInfoOld.getProdNorm();//规格
		String meterial = bslProductInfoOld.getProdMaterial();//钢种
		String prodUserType = bslProductInfoOld.getProdUserType();//纵剪带用途
		Float relWeight = bslProductInfoOld.getProdRelWeight();//重量
		
		//校验方式
		if(flag == 1){
			/**
			 * 出库
			 */
			//获取正在执行的产品生产指令
			BslMakePlanInfo prodPlanInfoExe = prodPlanService.getProdPlanInfoExe(planJz);
			if(prodPlanInfoExe == null){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有正在执行的生产指令！");
			}
			
			String exePlanId = prodPlanInfoExe.getPlanId();
			//先校验原状态是不是已入库
			if(!DictItemOperation.产品状态_已入库.equals(statusOld)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "不是在库的产品不能出库！");
			}
			//校验原半成品用途
			if(DictItemOperation.纵剪带用途_外销.equals(prodUserType)){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "纵剪带用途为外销，不能制造出库！");
			}
			//校验出库的半成品信息
			if(!prodPlanInfoExe.getProdMaterial().equals(meterial)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "原料钢种与正在执行的生产指令规定钢种不符，无法出库！");
			}
			if(!prodPlanInfoExe.getProdNorm().equals(norm)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "原料规格与正在执行的生产指令规定规格不符，无法出库！");
			}
//			if(!prodPlanInfoExe.getPlanLuno().equals(luno)){
//				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "原料炉号与正在执行的生产指令规定炉号不符，无法出库！");
//			}
			//校验数量（获取因为这条指令出库的产品数量）
			if(prodPlanInfoExe.getProdNum() != null && prodPlanInfoExe.getProdNum()!=0){
				BslProductInfoExample exampleSelectNum = new BslProductInfoExample();
				Criteria criteriaNum = exampleSelectNum.createCriteria();
				criteriaNum.andProdOutPlanEqualTo(exePlanId);
				List<BslProductInfo> bslProductInfoOutNums = bslProductInfoMapper.selectByExample(exampleSelectNum);
				if(bslProductInfoOutNums!=null && bslProductInfoOutNums.size()>0){
					if(bslProductInfoOutNums.size()>=prodPlanInfoExe.getProdNum()){
						throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "因该指令出库的纵剪带数量已达限定值，不允许出库！");
					}
				}
			}
			//校验重量（获取因为这条指令出库的产品重量）
			if(prodPlanInfoExe.getProdWeight() != null && prodPlanInfoExe.getProdWeight()!=0){
				BslProductInfoExample exampleSelect = new BslProductInfoExample();
				Criteria criteria = exampleSelect.createCriteria();
				criteria.andProdOutPlanEqualTo(exePlanId);
				List<BslProductInfo> bslProductInfoOuts = bslProductInfoMapper.selectByExample(exampleSelect);
				if(bslProductInfoOuts!=null && bslProductInfoOuts.size()>0){
					//因为该指令出库的总重量
					Float sumWeight = 0f;
					for (BslProductInfo bslProductInfo : bslProductInfoOuts) {
						sumWeight += bslProductInfo.getProdRelWeight();
					}
					Float chaoNum = (relWeight + sumWeight - prodPlanInfoExe.getProdWeight())/prodPlanInfoExe.getProdWeight();
					if(chaoNum > 0.1){
						throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "该指令已出库用料重量加上该半成品重量超出限定的10%，不允许出库！");
					}
				}
			}
			
			//校验因为该指令正在出库的纵剪带数量，超出数量不允许出库
			//获取该机组最大出库数量
			int maxOutNum = getMaxOutNum(planJz);
			if(maxOutNum == 0){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "获取该机组最大出库纵剪带数量失败！");
			}
			//判断当前累计出库纵剪带数量，若达最大值，需要先销库
			BslProductInfoExample exampleSelect = new BslProductInfoExample();
			Criteria criteria = exampleSelect.createCriteria();
			criteria.andProdOutPlanEqualTo(exePlanId);
			criteria.andProdStatusEqualTo(DictItemOperation.产品状态_已出库);
			List<BslProductInfo> bslProductInfoOuts = bslProductInfoMapper.selectByExample(exampleSelect);
			if(bslProductInfoOuts!=null && bslProductInfoOuts.size()>=maxOutNum){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "该机组同时出库纵剪带数量已达最大值，请先将之前出库的纵剪带销库！");
			}
			
			//校验完成开始出库
			BslProductInfo bslProductInfoUpdate = new BslProductInfo();
			bslProductInfoUpdate.setProdId(prodId);
			bslProductInfoUpdate.setProdStatus(DictItemOperation.产品状态_已出库);
			bslProductInfoUpdate.setProdOutDate(new Date());
			bslProductInfoUpdate.setProdOutPlan(exePlanId);
			int resultUpdate = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfoUpdate);
			if(resultUpdate<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultUpdate==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"出库失败");
			}
			
			//插入成功之后记录插入流水
			BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(prodId);//产品编号
			bslStockChangeDetail.setPlanSerno(exePlanId);//对应的生产指令单号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_制造出库);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_半成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfoOld.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(prodInputuser);//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//出库完成增加炉号
			//先判断有没有此炉号
			int luNum = planLunoInfoService.getPlanLuno(exePlanId, luno);
			if(luNum <= 0){
				//说明还没有这种炉号，开始新增
				planLunoInfoService.insertPlanLuno(exePlanId, luno);
			}
			
		}else if(flag == 2){
			/**
			 * 未用退回
			 */
			
			//库存状态为已出库
			if(!DictItemOperation.产品状态_已出库.equals(statusOld)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "必须是已出库的半成品才能退回！");
			}
			//开始退回
			BslProductInfo bslProductInfoUpdate = new BslProductInfo();
			bslProductInfoUpdate.setProdId(prodId);
			bslProductInfoUpdate.setProdStatus(DictItemOperation.产品状态_已入库);
			//bslProductInfoUpdate.setRemark("未用退回");
			bslProductInfoUpdate.setProdOutPlan("");//出库指令号设置为空
			bslProductInfoUpdate.setProdOutDate(null);
			bslProductInfoUpdate.setUpdDate(new Date());
			int resultUpdate = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfoUpdate);
			if(resultUpdate<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultUpdate==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"未用退回失败");
			}
			
			//退回成功之后记录插入流水
			BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(prodId);//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfoOld.getProdOutPlan());//对应的生产指令单号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_未用退回);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_半成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfoOld.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(prodInputuser);//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//退回完成删除炉号
			//先判断有没有大于1的类似产品,
			int luNum = planLunoInfoService.getPlanLuno(bslProductInfoOld.getProdOutPlan(), luno);
			if(luNum <= 1){
				//不大于1说明只有这一个，可以删除
				planLunoInfoService.deletePlanLuno(bslProductInfoOld.getProdOutPlan(), luno);
			}
			
		}else if(flag == 3){
			/**
			 * 完成
			 */
			
			//库存状态为已出库
			if(!DictItemOperation.产品状态_已出库.equals(statusOld)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "必须是已出库的半成品才能完成！");
			}
			
			//判断要完成的是不是最先出库的纵剪带
			//获取最先出库的纵剪带编号
			String prodIdFirst = getTheFirstOutProd(bslProductInfoOld.getProdOutPlan());
			if(!prodId.equals(prodIdFirst)){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "必须按照纵剪带先后出库顺序销库！");
			}
			
			//完成
			BslProductInfo bslProductInfoUpdate = new BslProductInfo();
			bslProductInfoUpdate.setProdId(prodId);
			bslProductInfoUpdate.setProdStatus(DictItemOperation.产品状态_已完成);
			//bslProductInfoUpdate.setRemark("完成");
			bslProductInfoUpdate.setUpdDate(new Date());
			int resultUpdate = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfoUpdate);
			if(resultUpdate<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultUpdate==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"完成失败");
			}
			
			/**
			 * 新增完成记录
			 */
			//退回成功之后记录插入流水
			BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(prodId);//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfoOld.getProdOutPlan());//对应的生产指令单号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_完成);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_半成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfoOld.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(prodInputuser);//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
		}
		return BSLResult.ok(prodId);
	}

	/**
	 * 根据盘号查询半成品信息
	 */
	@Override
	public BslProductInfo getHalfProdPlanByPK(String prodId) {
		return bslProductInfoMapper.selectByPrimaryKey(prodId);
	}

	/**
	 * 查询所有制造出库中的半成品信息
	 */
	@Override
	public List<BslProductInfo> getHalfProdExe(String exePlanId) {
		BslProductInfoExample example = new BslProductInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_半成品);
		criteria.andProdStatusEqualTo(DictItemOperation.产品状态_已出库);
		criteria.andProdOutPlanEqualTo(exePlanId);
		List<BslProductInfo> list = bslProductInfoMapper.selectByExample(example);
		return list;
	}

	/**
	 * 校验半成品状态
	 * 已入库重量+本次重量>纵剪带重量*1.5 返回1
	 * 已入库重量+本次重量>=纵剪带重量的97% 返回2
	 * 已入库重量+本次重量<纵剪带重量的97% 返回3
	 */
	@Override
	public int updateHalfProdStatus(String prodId,Float inWeightThis) {
		BslProductInfo bslProductInfo = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(bslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"未查到该纵剪带信息！");
		}
		//获取纵剪带重量
		Float halfProdWeight = bslProductInfo.getProdRelWeight();
		//获取该半成品下入库的待处理品信息
		Float inWeight = 0f;
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_待处理品);
		criteria.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		criteria.andProdParentNoEqualTo(prodId);
		List<BslProductInfo> prods = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		if(prods != null && prods.size() > 0){
			for (BslProductInfo prod : prods) {
				inWeight += prod.getProdRelWeight();
			}
		}
		//获取该半成品下入库的成品信息（非待处理品入库）
		BslProductInfoExample bslProductInfoExample2 = new BslProductInfoExample();
		Criteria criteria2 = bslProductInfoExample2.createCriteria();
		criteria2.andProdTypeEqualTo(DictItemOperation.产品类型_成品);
		criteria2.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		criteria2.andProdParentNoEqualTo(prodId);
		List<BslProductInfo> prods2 = bslProductInfoMapper.selectByExample(bslProductInfoExample2);
		if(prods2 != null && prods2.size() > 0){
			for (BslProductInfo prod : prods2) {
				inWeight += prod.getProdRelWeight();
			}
		}
		//获取已入库重量inWeight + 本次入库重量
		Float sumWeight = inWeight + inWeightThis;
		//开始判断
		//获取产品入库重量上浮参数
		double upParam = 1.5;
		String upParamStr = paramService.getValueByParamKey(DictItemOperation.参数_纵剪带制造重量上浮参数);
		if(!StringUtils.isBlank(upParamStr)){
			upParam = Double.valueOf(upParamStr);
		}
		if(sumWeight > halfProdWeight*upParam){
			return 1;
		}else if(sumWeight/halfProdWeight >= 0.97){
			return 2;
		}else{
			return 3;
		}
	}
	
	//根据机组获取最大出库纵剪带数量
	private int getMaxOutNum(String planJz){
		String paramJz;
		if (DictItemOperation.产品机组_480机组.equals(planJz)) {
			paramJz = DictItemOperation.参数_480机组同时出库纵剪带数量最大值;
		}else if (DictItemOperation.产品机组_800机组.equals(planJz)) {
			paramJz = DictItemOperation.参数_800机组同时出库纵剪带数量最大值;
		}else{
			paramJz = "";
		}
		String maxOutNumStr = paramService.getValueByParamKey(paramJz);
		if(StringUtils.isBlank(maxOutNumStr)){
			return 0;
		}
		return Integer.valueOf(maxOutNumStr);
	}
	
	//根据该指令出库中的纵剪带，最先出库的一个纵剪带编号
	private String getTheFirstOutProd(String planId){
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.getTheFirstOutProd(planId);
		if(bslProductInfos != null && bslProductInfos.size()>0){
			return bslProductInfos.get(0).getProdId();
		}
		return null;
	}

	//判断机组出库纵剪带数量是否达到最大值
	@Override
	public BSLResult getIsMaxOutNum(String planJz) {
		//获取该机组纵剪带出库的最大限定值
		int maxOutNum = getMaxOutNum(planJz);
		//获取该机组正在出库的纵剪带数量
		//获取正在执行的产品生产指令
		BslMakePlanInfo prodPlanInfoExe = prodPlanService.getProdPlanInfoExe(planJz);
		if(prodPlanInfoExe == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有正在执行的生产指令！");
		}
		//获取该指令正在出库的纵剪带数量
		BslProductInfoExample exampleSelect = new BslProductInfoExample();
		Criteria criteria = exampleSelect.createCriteria();
		criteria.andProdOutPlanEqualTo(prodPlanInfoExe.getPlanId());
		criteria.andProdStatusEqualTo(DictItemOperation.产品状态_已出库);
		List<BslProductInfo> bslProductInfoOuts = bslProductInfoMapper.selectByExample(exampleSelect);
		if(bslProductInfoOuts!=null && bslProductInfoOuts.size()>=maxOutNum){
			return BSLResult.ok("1");
		}
		
		return BSLResult.ok("0");
	}

	/**
	 * 查询某指令出库的前两个纵剪带盘号及可用重量
	 */
	@Override
	public BSLResult getParentZjxInfo(String planId) {
		//获取该指令的生产机组
		BslMakePlanInfo bslMakePlanInfo = bslMakePlanInfoMapper.selectByPrimaryKey(planId);
		if(bslMakePlanInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到指令信息，生产指令号："+planId);
		}
		String params = "";
		if(DictItemOperation.产品机组_480机组.equals(bslMakePlanInfo.getPlanJz())){
			params = DictItemOperation.参数_480纵剪带默认废料率;
		}else if(DictItemOperation.产品机组_800机组.equals(bslMakePlanInfo.getPlanJz())){
			params = DictItemOperation.参数_800纵剪带默认废料率;
		}
		//先查询纵剪带默认废料率
		float flv = 0f;
		String flvStr = paramService.getValueByParamKey(params);
		if(!StringUtils.isBlank(flvStr)){
			flv =  Float.valueOf(flvStr)/100;
		}
		
		String prodParentNo1 = "";
		String prodParentNo2 = "";
		float prodRelWeightParent1 = 0f;
		float prodRelWeightParent2 = 0f;
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.getTopTwoOutProd(planId);
		if(bslProductInfos != null){
			if(bslProductInfos.size() >= 1){
				String prodId = bslProductInfos.get(0).getProdId();
				float prodRelWeight = bslProductInfos.get(0).getProdRelWeight();
				//获取第一个出库的纵剪带剩余可用重量
				float useWeight = bslZjdUseInfoMapper.getMakeWeightByZjdId(prodId).getProdRuWeight();
				prodParentNo1 = prodId;
				float canUseWeight1 = prodRelWeight*(1-flv);
				canUseWeight1 = DictItemOperation.round3(canUseWeight1);
				if(canUseWeight1 > useWeight){
					prodRelWeightParent1 = prodRelWeight*(1-flv) - useWeight;
					prodRelWeightParent1 = DictItemOperation.round3(prodRelWeightParent1);
				}else{
					//否则该纵剪带已经完成，置成完成
					BslProductInfo bslProductInfo = new BslProductInfo();
					bslProductInfo.setProdId(bslProductInfos.get(0).getProdId());
					bslProductInfo.setProdStatus(DictItemOperation.产品状态_已完成);
					bslProductInfo.setUpdDate(new Date());
					bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfo);
				}
			}
			if(bslProductInfos.size() >= 2){
				String prodId = bslProductInfos.get(1).getProdId();
				float prodRelWeight = bslProductInfos.get(1).getProdRelWeight();
				//获取第一个出库的纵剪带剩余可用重量
				float useWeight = bslZjdUseInfoMapper.getMakeWeightByZjdId(prodId).getProdRuWeight();
				prodParentNo2 = prodId;
				float canUseWeight2 = prodRelWeight*(1-flv);
				canUseWeight2 = DictItemOperation.round3(canUseWeight2);
				if(canUseWeight2 > useWeight){
					prodRelWeightParent2 = prodRelWeight*(1-flv) - useWeight;
					prodRelWeightParent2 =  DictItemOperation.round3(prodRelWeightParent2);
				}else{
					//否则该纵剪带已经完成，置成完成
					BslProductInfo bslProductInfo = new BslProductInfo();
					bslProductInfo.setProdId(bslProductInfos.get(1).getProdId());
					bslProductInfo.setProdStatus(DictItemOperation.产品状态_已完成);
					bslProductInfo.setUpdDate(new Date());
					bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfo);
				}
			}
		}
		BslTopTwoZjdInfo bslTopTwoZjdInfo = new BslTopTwoZjdInfo();
		bslTopTwoZjdInfo.setProdParentNo1(prodParentNo1);
		bslTopTwoZjdInfo.setProdParentNo2(prodParentNo2);
		bslTopTwoZjdInfo.setProdRelWeightParent1(prodRelWeightParent1);
		bslTopTwoZjdInfo.setProdRelWeightParent2(prodRelWeightParent2);
		return BSLResult.ok(bslTopTwoZjdInfo);
	}

	/**
	 * 判断入库重量是否足够让新产品入库
	 */
	@Override
	public boolean checkHalfProdWeight(String prodId, Float inWeightThis) {
		BslProductInfo bslProductInfo = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(bslProductInfo == null){
			return false;
		}
		float parentWeight = bslProductInfo.getProdRelWeight();
		float useWeight = bslZjdUseInfoMapper.getMakeWeightByZjdId(prodId).getProdRuWeight();
		if(parentWeight < (useWeight + inWeightThis)){
			return false;
		}else{
			return true;
		}
	}
	

}
