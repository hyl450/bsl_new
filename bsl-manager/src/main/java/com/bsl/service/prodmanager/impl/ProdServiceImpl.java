package com.bsl.service.prodmanager.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bsl.common.pojo.BSLException;
import com.bsl.common.pojo.EasyUIDataGridResult;
import com.bsl.common.utils.BSLResult;
import com.bsl.common.utils.StringUtil;
import com.bsl.dao.JedisClient;
import com.bsl.mapper.BslMakePlanInfoMapper;
import com.bsl.mapper.BslProductInfoMapper;
import com.bsl.mapper.BslStockChangeDetailMapper;
import com.bsl.mapper.BslZjdUseInfoMapper;
import com.bsl.pojo.BslMakePlanInfo;
import com.bsl.pojo.BslMakePlanInfoDetail;
import com.bsl.pojo.BslProductInfo;
import com.bsl.pojo.BslProductInfoExample;
import com.bsl.pojo.BslProductInfoExample.Criteria;
import com.bsl.pojo.BslStockChangeDetail;
import com.bsl.pojo.BslZjdUseInfo;
import com.bsl.reportbean.BslProdMakeUseInfo;
import com.bsl.reportbean.BslProductInfoCollect;
import com.bsl.reportbean.BslRuInFo;
import com.bsl.reportbean.BslTopTwoZjdInfo;
import com.bsl.select.DictItemOperation;
import com.bsl.select.ErrorCodeInfo;
import com.bsl.select.QueryCriteria;
import com.bsl.select.QueryExample;
import com.bsl.service.plan.MakePlanService;
import com.bsl.service.prodmanager.HalfProdOutPutService;
import com.bsl.service.prodmanager.ProdPlanService;
import com.bsl.service.prodmanager.ProdService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;

/**
 * 产品入库实现类
 * duk-20190319
 */
@Service
public class ProdServiceImpl implements ProdService {

	@Autowired	 
	BslProductInfoMapper bslProductInfoMapper;
	@Autowired	 
	BslMakePlanInfoMapper bslMakePlanInfoMapper;
	@Autowired	 
	BslStockChangeDetailMapper bslStockChangeDetailMapper;
	@Autowired	 
	BslZjdUseInfoMapper bslZjdUseInfoMapper;
	@Autowired
	private ProdPlanService prodPlanService;
	@Autowired
	private HalfProdOutPutService halfProdOutPutService;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private MakePlanService makePlanService;
	
	@Value("REDIS_NEXT_PROD_ID")
	private String REDIS_NEXT_PROD_ID;
	
	@Value("REDIS_NEXT_PROD_W_ID")
	private String REDIS_NEXT_PROD_W_ID;
	
	@Value("${REDIS_NEXT_STOCKCHANGE_ID}")
	private String REDIS_NEXT_STOCKCHANGE_ID;
	
	@Value("${REDIS_NEXT_CAR_ID}")
	private String REDIS_NEXT_CAR_ID;
	

	/**
	 * 初始化查询所有2-产品信息 
	 */
	@Override
	public EasyUIDataGridResult getProdService(Integer page, Integer rows) {
		//查询条件状态 类型是2-产品
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_成品);
		criteria.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		//分页处理
		PageHelper.startPage(page,rows);
		//调用sql查询
		bslProductInfoExample.setOrderByClause("`crt_date` desc,`prod_id` desc,`prod_plan_no`");
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(bslProductInfos);
		//取记录总条数
		PageInfo<BslProductInfo> pageInfo = new PageInfo<>(bslProductInfos);
		result.setTotal(pageInfo.getTotal());
		result.setClassName("prodServiceImpl");
		result.setMethodName("getProdService");
		return result;
	}

	/**
	 *根据条件查询2-产品信息 
	 */
	@Override
	public BSLResult getProdService(QueryCriteria queryCriteria) {
		//创建查询的实例，并赋值
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_成品);
		criteria.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		//产品编号
		if (!StringUtils.isBlank(queryCriteria.getProdId())) {
			criteria.andProdIdLike(StringUtil.likeStr(queryCriteria.getProdId()));
		}
		//成品生产批号
		if (!StringUtils.isBlank(queryCriteria.getProdPlanNo())) {
			criteria.andProdPlanNoLike(StringUtil.likeStr(queryCriteria.getProdPlanNo()));
		}
		//炉号
		if (!StringUtils.isBlank(queryCriteria.getProdLuno())) {
			criteria.andProdLunoLike(StringUtil.likeStr(queryCriteria.getProdLuno()));
		}
		//父级盘号
		if (!StringUtils.isBlank(queryCriteria.getProdParentNo())) {
			criteria.andProdParentNoLike(StringUtil.likeStr(queryCriteria.getProdParentNo()));
		}
		//出库指令号
		if (!StringUtils.isBlank(queryCriteria.getProdOutPlan())) {
			criteria.andProdOutPlanLike(StringUtil.likeStr(queryCriteria.getProdOutPlan()));
		}
		//产品规格
		if(!StringUtils.isBlank(queryCriteria.getProdNorm())){
			criteria.andProdNormLike("%"+queryCriteria.getProdNorm()+"%");
		}
		//钢种
		if (!StringUtils.isBlank(queryCriteria.getProdMaterial())) {
			criteria.andProdMaterialEqualTo(queryCriteria.getProdMaterial());
		}
		//产品状态
		if (!StringUtils.isBlank(queryCriteria.getProdStatus())) {
			criteria.andProdStatusEqualTo(queryCriteria.getProdStatus());
		}
		//班次
		if (!StringUtils.isBlank(queryCriteria.getProdBc())) {
			criteria.andProdBcEqualTo(queryCriteria.getProdBc());
		}
		//生产机组
		if (!StringUtils.isBlank(queryCriteria.getProdMakeJz())) {
			criteria.andProdMakeJzEqualTo(queryCriteria.getProdMakeJz());
		}
		
		//开始日期结束日期
		//起始日期 结束日期
		Date dateStart = new Date();
		Date dateEnd = new Date();
		if(!StringUtils.isBlank(queryCriteria.getStartDate())){
			try {
				dateStart = DictItemOperation.日期转换实例.parse(queryCriteria.getStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			try {
				dateStart = DictItemOperation.日期转换实例.parse("2018-01-01");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!StringUtils.isBlank(queryCriteria.getEndDate())){
			try {
				dateEnd = DictItemOperation.日期转换实例.parse(queryCriteria.getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			dateEnd = new Date();
		}
		criteria.andCrtDateBetween(dateStart,dateEnd);
		
		//分页处理
		PageHelper.startPage(Integer.parseInt(queryCriteria.getPage()), Integer.parseInt(queryCriteria.getRows()));
		//调用sql查询
		if(StringUtils.isBlank(queryCriteria.getSort()) || StringUtils.isBlank(queryCriteria.getOrder())){
			bslProductInfoExample.setOrderByClause("`crt_date` desc,`prod_id` desc,`prod_plan_no`");
		}else{
			String sortSql = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, queryCriteria.getSort());
			if(!StringUtils.isBlank(sortSql)){	
				bslProductInfoExample.setOrderByClause("`"+sortSql+"` "+ queryCriteria.getOrder());
			}
		}
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		PageInfo<BslProductInfo> pageInfo = new PageInfo<BslProductInfo>(bslProductInfos);
		return BSLResult.ok(bslProductInfos,"prodServiceImpl","getProdService",pageInfo.getTotal(),bslProductInfos);
	}

	/**
	 * 入库
	 */
	@Override
	public BSLResult addCfmProdInfo(BslProductInfo bslProductInfo,int sumNum,BslTopTwoZjdInfo bslTopTwoZjdInfo) {
		//二次校验
		//获取正在执行的产品生产指令
		BslMakePlanInfo makePlanInfoExe = prodPlanService.getProdPlanInfoExe(bslProductInfo.getProdMakeJz());
		if(makePlanInfoExe == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到正在执行的生产指令信息，无法入库");
		}
		//校验指令号
		if(!makePlanInfoExe.getPlanId().equals(bslProductInfo.getProdPlanNo())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "生产指令号必须是执行中的指令");
		}
		//校验名称
		if(!makePlanInfoExe.getMakeName().equals(bslProductInfo.getProdName())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品名称必须是生产指令指定名称");
		}
		//校验炉号
//		if(!makePlanInfoExe.getPlanLuno().equals(bslProductInfo.getProdLuno())){
//			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品炉号必须是生产指令对应炉号");
//		}
		//校验钢种
		if(!makePlanInfoExe.getProdMaterial().equals(bslProductInfo.getProdMaterial())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种必须是生产指令指定钢种");
		}
		//校验规格
		if(!makePlanInfoExe.getMakeProdNorm().equals(bslProductInfo.getProdNorm())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格必须是生产指令指定规格");
		}
		//校验定尺必须是指定定尺
		List<BslMakePlanInfoDetail> makePlanInfoDetail = makePlanService.getMakePlanInfoDetail(makePlanInfoExe.getPlanId());
		boolean isAssNorm = false;
		for (BslMakePlanInfoDetail bslMakePlanInfoDetail : makePlanInfoDetail) {
			if(bslProductInfo.getProdLength().equals(bslMakePlanInfoDetail.getProdLength())){
				isAssNorm = true;
				break;
			}
		}
		if(isAssNorm == false){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品定尺必须是该指令下详细计划指定的某种定尺");
		}
		
		//校验父级盘号
		//根据指令号重新获取最先出库的两个纵剪带及其剩余重量
		BslTopTwoZjdInfo bslTopTwoAct = (BslTopTwoZjdInfo) halfProdOutPutService.getParentZjxInfo(bslProductInfo.getProdPlanNo()).getData();
		if(bslTopTwoAct == null){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "获取该指令最先出库的纵剪带信息失败");
		}
		if(!bslTopTwoAct.equals(bslTopTwoZjdInfo)){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "获取该指令最先出库的纵剪带信息发生变更，请关闭该界面重试");
		}
		
		//获取两盘纵剪带及其可用重量
		//获取第一盘信息
		String zjd1 = bslTopTwoZjdInfo.getProdParentNo1();
		float zjdWeight1 = bslTopTwoZjdInfo.getProdRelWeightParent1();
		BslProductInfo parentProd1 = bslProductInfoMapper.selectByPrimaryKey(zjd1);

		//获取第二盘信息
		String zjd2 = bslTopTwoZjdInfo.getProdParentNo2();
		float zjdWeight2 = 0f;
		BslProductInfo parentProd2 = null;
		if(!StringUtils.isBlank(zjd2)){
			zjdWeight2 = bslTopTwoZjdInfo.getProdRelWeightParent2();
			parentProd2 = bslProductInfoMapper.selectByPrimaryKey(zjd2);
		}
		
		//校验总重量
		//本次入库重量要小于等于两个纵剪带重量之和
		float inWeight = bslProductInfo.getProdRelWeight();
		float zjdWeightAll = DictItemOperation.round3(zjdWeight1 + zjdWeight2);
		if(inWeight > zjdWeightAll){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "本次入库重量大于前两盘出库纵剪带可用重量之和，无法入库！");
		}
		
		//判断入库产品数,平分重量
		Float relWeight = bslProductInfo.getProdRelWeight()/sumNum;
		//每个产品入库的重量
		relWeight = DictItemOperation.round3(relWeight);
		bslProductInfo.setProdRelWeight(relWeight);
		
		//判断第一个纵剪带能制造几包产品
		double numUseTwoDouble = Math.floor(Double.valueOf(zjdWeight1/relWeight));
		int numUseTwo = new Double(numUseTwoDouble).intValue();
		
		//记录入库流水
		BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
		//记录纵剪带制造信息
		BslZjdUseInfo bslZjdUseInfo = new BslZjdUseInfo();
		String returnProdId = "";
		String prodId;
		
		if(numUseTwo >= sumNum){
			//只需用第一盘
			for (int i = 1; i <= sumNum; i++) {
				//校验完成，开始入库
				prodId = createProdId();
				bslProductInfo.setProdId(prodId);//生成编号
				bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
				bslProductInfo.setProdLuno(parentProd1.getProdLuno());//炉号为父级炉号
				bslProductInfo.setProdParentNo(parentProd1.getProdId());
				bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
				bslProductInfo.setCrtDate(new Date());//创建日期当天
				bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
				bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
				bslProductInfo.setProdCompany(parentProd1.getProdCompany());//厂家同原来一致
				bslProductInfo.setProdCustomer(parentProd1.getProdCustomer());
				int result = bslProductInfoMapper.insert(bslProductInfo);
				if(result<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(result==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
				}
				
				//插入成功之后记录插入流水
				bslStockChangeDetail = new BslStockChangeDetail();
				bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
				bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
				bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
				bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
				bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
				bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
				bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
				bslStockChangeDetail.setCrtDate(new Date());
				int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
				if(resultStock<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(resultStock==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
				}
				
				//记录纵剪带制造信息
				bslZjdUseInfo.setProdId(prodId);
				bslZjdUseInfo.setProdZjdId(parentProd1.getProdId());
				bslZjdUseInfo.setProdUseWeight(bslProductInfo.getProdRelWeight());
				bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
				bslZjdUseInfo.setProdUseBl(1f);
				bslZjdUseInfoMapper.insert(bslZjdUseInfo);
				
				//记录返回起始编号
				if(i==1){
					returnProdId = prodId;
				}
			}
		}else{
			//第一盘第二盘都要用到
			//开始入库第一盘制造的
			for (int i = 1; i <= numUseTwo; i++) {
				//校验完成，开始入库
				prodId = createProdId();
				bslProductInfo.setProdId(prodId);//生成编号
				bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
				bslProductInfo.setProdLuno(parentProd1.getProdLuno());//炉号为父级炉号
				bslProductInfo.setProdParentNo(parentProd1.getProdId());
				bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
				bslProductInfo.setCrtDate(new Date());//创建日期当天
				bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
				bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
				bslProductInfo.setProdCompany(parentProd1.getProdCompany());//厂家同原来一致
				bslProductInfo.setProdCustomer(parentProd1.getProdCustomer());
				int result = bslProductInfoMapper.insert(bslProductInfo);
				if(result<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(result==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
				}
				
				//插入成功之后记录插入流水
				bslStockChangeDetail = new BslStockChangeDetail();
				bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
				bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
				bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
				bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
				bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
				bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
				bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
				bslStockChangeDetail.setCrtDate(new Date());
				int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
				if(resultStock<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(resultStock==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
				}
				
				//记录纵剪带制造信息
				bslZjdUseInfo.setProdId(prodId);
				bslZjdUseInfo.setProdZjdId(parentProd1.getProdId());
				bslZjdUseInfo.setProdUseWeight(bslProductInfo.getProdRelWeight());
				bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
				bslZjdUseInfo.setProdUseBl(1f);
				bslZjdUseInfoMapper.insert(bslZjdUseInfo);
				
				//记录返回起始编号
				if(i==1){
					returnProdId = prodId;
				}
			}
			
			//开始入库第一盘和第二盘一起制造的（只有一个）
			//判断第一盘可以使用的重量(其剩余重量 = 原本可用重量 - 制造包数*每包重量)
			float useWeight1 = zjdWeight1 - bslProductInfo.getProdRelWeight()*numUseTwo;
			useWeight1 = DictItemOperation.round3(useWeight1);
			//判断第二盘使用的重量(等于产品重量减去第一盘剩余重量)
			float useWeight2 = bslProductInfo.getProdRelWeight() - useWeight1;
			useWeight2 = DictItemOperation.round3(useWeight2);
			//哪个重量用的多，父级盘号就是谁
			BslProductInfo bslProdUse = null;
			if(useWeight1 >= useWeight2){
				bslProdUse = parentProd1;
			}else{
				bslProdUse = parentProd2;
			}
			
			prodId = createProdId();
			bslProductInfo.setProdId(prodId);//生成编号
			bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
			bslProductInfo.setProdLuno(bslProdUse.getProdLuno());//炉号为父级炉号
			bslProductInfo.setProdParentNo(bslProdUse.getProdId());
			bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
			bslProductInfo.setCrtDate(new Date());//创建日期当天
			bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
			bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
			bslProductInfo.setProdCompany(bslProdUse.getProdCompany());//厂家同原来一致
			bslProductInfo.setProdCustomer(bslProdUse.getProdCustomer());
			int resultHH = bslProductInfoMapper.insert(bslProductInfo);
			if(resultHH<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultHH==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
			}
			
			//插入成功之后记录插入流水
			bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStockHH = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStockHH<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStockHH==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//记录纵剪带制造信息
			bslZjdUseInfo.setProdId(prodId);
			bslZjdUseInfo.setProdZjdId(parentProd1.getProdId());
			bslZjdUseInfo.setProdUseWeight(useWeight1);
			bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
			float bv1 = ((float)Math.round(useWeight1/bslProductInfo.getProdRelWeight()*100))/100;
			bslZjdUseInfo.setProdUseBl(bv1);
			bslZjdUseInfoMapper.insert(bslZjdUseInfo);
			
			bslZjdUseInfo.setProdId(prodId);
			bslZjdUseInfo.setProdZjdId(parentProd2.getProdId());
			bslZjdUseInfo.setProdUseWeight(useWeight2);
			bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
			float bv2 = ((float)Math.round(useWeight2/bslProductInfo.getProdRelWeight()*100))/100;
			bslZjdUseInfo.setProdUseBl(bv2);
			bslZjdUseInfoMapper.insert(bslZjdUseInfo);
			
			//记录返回起始编号(没有第一盘独立造的产品)
			if(numUseTwo < 1){
				returnProdId = prodId;
			}
			
			//开始入库第二盘制造的
			for (int i = numUseTwo+2; i <= sumNum; i++) {
				//校验完成，开始入库
				prodId = createProdId();
				bslProductInfo.setProdId(prodId);//生成编号
				bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
				bslProductInfo.setProdLuno(parentProd2.getProdLuno());//炉号为父级炉号
				bslProductInfo.setProdParentNo(parentProd2.getProdId());
				bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
				bslProductInfo.setCrtDate(new Date());//创建日期当天
				bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
				bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
				bslProductInfo.setProdCompany(parentProd2.getProdCompany());//厂家同原来一致
				bslProductInfo.setProdCustomer(parentProd2.getProdCustomer());
				int result = bslProductInfoMapper.insert(bslProductInfo);
				if(result<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(result==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
				}
				
				//插入成功之后记录插入流水
				bslStockChangeDetail = new BslStockChangeDetail();
				bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
				bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
				bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
				bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
				bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
				bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
				bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
				bslStockChangeDetail.setCrtDate(new Date());
				int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
				if(resultStock<0){
					throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
				}else if(resultStock==0){
					throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
				}
				
				//记录纵剪带制造信息
				bslZjdUseInfo.setProdId(prodId);
				bslZjdUseInfo.setProdZjdId(parentProd2.getProdId());
				bslZjdUseInfo.setProdUseWeight(bslProductInfo.getProdRelWeight());
				bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
				bslZjdUseInfo.setProdUseBl(1f);
				bslZjdUseInfoMapper.insert(bslZjdUseInfo);
				
			}
			
		}
		
		//入库完成之后判断是否需要更新纵剪带状态
		//累计入库重量大于纵剪带1的重量，把纵剪带1置成完成
		if(inWeight >= zjdWeight1){
			BslProductInfo bslProductInfoTmp = new BslProductInfo();
			bslProductInfoTmp.setProdId(parentProd1.getProdId());
			bslProductInfoTmp.setProdStatus(DictItemOperation.产品状态_已完成);
			bslProductInfoTmp.setUpdDate(new Date());
			bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfoTmp);
		}
		//累计入库重量大于等于纵剪带1+纵剪带2的重量，把纵剪带2置成完成
		if(inWeight >= zjdWeightAll){
			BslProductInfo bslProductInfoTmp = new BslProductInfo();
			bslProductInfoTmp.setProdId(parentProd2.getProdId());
			bslProductInfoTmp.setProdStatus(DictItemOperation.产品状态_已完成);
			bslProductInfoTmp.setUpdDate(new Date());
			bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfoTmp);
		}
		
		prodPlanService.updateProdRuNumAndSums(makePlanInfoExe.getPlanId());
		return BSLResult.ok(returnProdId);
	}
	
	/**
	 * 补录入库
	 */
	@Override
	public BSLResult addCfmProdInfoBuLu(BslProductInfo bslProductInfo, int sumNum) {
		//二次校验
		//获取正在执行的产品生产指令
		BslMakePlanInfo makePlanInfo = prodPlanService.getProdPlanInfo(bslProductInfo.getProdPlanNo());
		if(makePlanInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到生产指令信息，无法入库");
		}
		//校验名称
		if(!makePlanInfo.getMakeName().equals(bslProductInfo.getProdName())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品名称必须是生产指令指定名称");
		}
		//校验炉号
//				if(!makePlanInfoExe.getPlanLuno().equals(bslProductInfo.getProdLuno())){
//					throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品炉号必须是生产指令对应炉号");
//				}
		//校验钢种
		if(!makePlanInfo.getProdMaterial().equals(bslProductInfo.getProdMaterial())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种必须是生产指令指定钢种");
		}
		//校验规格
		if(!makePlanInfo.getMakeProdNorm().equals(bslProductInfo.getProdNorm())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格必须是生产指令指定规格");
		}
		//校验定尺必须是指定定尺
		List<BslMakePlanInfoDetail> makePlanInfoDetail = makePlanService.getMakePlanInfoDetail(makePlanInfo.getPlanId());
		boolean isAssNorm = false;
		for (BslMakePlanInfoDetail bslMakePlanInfoDetail : makePlanInfoDetail) {
			if(bslProductInfo.getProdLength().equals(bslMakePlanInfoDetail.getProdLength())){
				isAssNorm = true;
				break;
			}
		}
		if(isAssNorm == false){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品定尺必须是该指令下详细计划指定的某种定尺");
		}
		
		//校验父级盘号
		BslProductInfo parentProd = bslProductInfoMapper.selectByPrimaryKey(bslProductInfo.getProdParentNo());
		if(parentProd == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到指定的父级纵剪带信息");
		}
		if(!parentProd.getProdOutPlan().equals(makePlanInfo.getPlanId())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "父级纵剪带必须是因该指令出库的纵剪带");
		}
		//校验总重量
		//记录本次入库重量
		Float inWeight = bslProductInfo.getProdRelWeight();
		boolean ifCheckPass = halfProdOutPutService.checkHalfProdWeight(bslProductInfo.getProdParentNo(),inWeight);
		if(!ifCheckPass){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "该父级纵剪带已生产重量加上本次需入库重量累计超出父级纵剪带本身重量，无法入库！");
		}
		
		//判断入库盘数,平分重量
		Float relWeight = bslProductInfo.getProdRelWeight()/sumNum;
		relWeight = ((float)Math.round(relWeight*1000))/1000;
		bslProductInfo.setProdRelWeight(relWeight);
		
		//记录入库流水
		BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
		//记录纵剪带制造信息
		BslZjdUseInfo bslZjdUseInfo = new BslZjdUseInfo();
		String returnProdId = "";
		String prodId;
		
		for (int i = 0; i < sumNum; i++) {
			//校验完成，开始入库
			prodId = createProdId();
			bslProductInfo.setProdId(prodId);//生成编号
			bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
			bslProductInfo.setProdLuno(parentProd.getProdLuno());//炉号为父级炉号
			bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
			bslProductInfo.setCrtDate(new Date());//创建日期当天
			bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
			bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
			bslProductInfo.setProdCompany(parentProd.getProdCompany());//厂家同原来一致
			bslProductInfo.setProdCustomer(parentProd.getProdCustomer());
			
			int result = bslProductInfoMapper.insert(bslProductInfo);
			if(result<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(result==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
			}
			
			//插入成功之后记录插入流水
			bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//入库完成之后增加
			//记录纵剪带制造信息
			bslZjdUseInfo.setProdId(prodId);
			bslZjdUseInfo.setProdZjdId(parentProd.getProdId());
			bslZjdUseInfo.setProdUseWeight(bslProductInfo.getProdRelWeight());
			bslZjdUseInfo.setProdPlanId(bslProductInfo.getProdPlanNo());
			bslZjdUseInfo.setProdUseBl(1f);
			bslZjdUseInfoMapper.insert(bslZjdUseInfo);
			
			//记录返回起始编号
			if(i==0){
				returnProdId = prodId;
			}
		}
		
		prodPlanService.updateProdRuNumAndSums(makePlanInfo.getPlanId());
		return BSLResult.ok(returnProdId);
	}

	/**
	 * 已入库产品信息修改
	 */
	@Override
	public BSLResult updateProdInfo(BslProductInfo bslProductInfo) {
		//校验管理员
		if(!DictItemOperation.管理员.equals(bslProductInfo.getProdInputuser())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有管理员才允许修改产品");
		}
		//获取原产品信息进行校验
		BslProductInfo oldBslProductInfo = bslProductInfoMapper.selectByPrimaryKey(bslProductInfo.getProdId());
		if(oldBslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据件号查询记录为空");
		}
		//校验状态，只有是1-入库中的状态才能修改
		/*if(!DictItemOperation.产品状态_已入库.equals(oldBslProductInfo.getProdStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有在库的产品才能修改");
		}*/
		//校验不允许修改的内容
		//校验指令号
		if(!oldBslProductInfo.getProdPlanNo().equals(bslProductInfo.getProdPlanNo())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品指令号不允许修改");
		}
		//校验名称
		if(!oldBslProductInfo.getProdName().equals(bslProductInfo.getProdName())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品名称不允许修改");
		}
		//校验炉号
//		if(!oldBslProductInfo.getProdLuno().equals(bslProductInfo.getProdLuno())){
//			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品炉号不允许修改");
//		}
		//校验钢种
		if(!oldBslProductInfo.getProdMaterial().equals(bslProductInfo.getProdMaterial())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种不允许修改");
		}
		//校验父级卷板号
		/*if(!oldBslProductInfo.getProdParentNo().equals(bslProductInfo.getProdParentNo())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品父级盘号不允许修改");
		}*/
		//校验规格
		if(!oldBslProductInfo.getProdNorm().equals(bslProductInfo.getProdNorm())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格不允许修改");
		}
		//校验定尺必须是指定定尺
		List<BslMakePlanInfoDetail> makePlanInfoDetail = makePlanService.getMakePlanInfoDetail(oldBslProductInfo.getProdPlanNo());
		boolean isAssNorm = false;
		for (BslMakePlanInfoDetail bslMakePlanInfoDetail : makePlanInfoDetail) {
			if(bslProductInfo.getProdLength().equals(bslMakePlanInfoDetail.getProdLength())){
				isAssNorm = true;
				break;
			}
		}
		if(isAssNorm == false){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品定尺必须是该指令下详细计划指定的某种定尺");
		}
		//校验父级盘号
		if(!oldBslProductInfo.getProdParentNo().equals(bslProductInfo.getProdParentNo())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品父级盘号不允许修改！");
		}
		
		//校验完成开始修改
		bslProductInfo.setUpdDate(new Date());
		bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
		int result = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfo);
		if(result<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(result==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"根据条件没有符合的修改记录");
		}
		prodPlanService.updateProdRuNumAndSums(bslProductInfo.getProdPlanNo());
		return BSLResult.ok(bslProductInfo.getProdId());
	}
	
	/**
	 * 产品自动生成编号
	 * XCP+日期+3位序号
	 * @return
	 */
	public String createProdId() {
		long incr = jedisClient.incr(REDIS_NEXT_PROD_ID);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String prodId = String.format("XCP%s%03d", sdf.format(new Date()), incr);
		return prodId;
	}
	
	/**
	 * 外协厂产品自动生成编号
	 * XCPW+日期+3位序号
	 * @return
	 */
	public String createProdIdWx() {
		long incr = jedisClient.incr(REDIS_NEXT_PROD_W_ID);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String prodId = String.format("XCPW%s%03d", sdf.format(new Date()), incr);
		return prodId;
	}
	
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

	@Override
	public List<BslProductInfo> getProdList(BslProductInfoExample productInfoExample) {
		return bslProductInfoMapper.selectByExample(productInfoExample);
	}

	/**
	 * 删除
	 */
	@Override
	public BSLResult delete(String prodId,String user) {
		//校验人员
		if(!DictItemOperation.管理员.equals(user)){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有管理员才允许删除产品");
		}
		//获取原产品信息进行校验
		BslProductInfo oldBslProductInfo = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(oldBslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据产品编号查询记录为空");
		}
		//校验状态，只有是1-入库中的状态才能修改
		if(!DictItemOperation.产品状态_已入库.equals(oldBslProductInfo.getProdStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有在库的产品才允许删除");
		}
		//开始删除
		int result = bslProductInfoMapper.deleteByPrimaryKey(prodId);
		if(result<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(result==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"根据条件没有符合的修改记录");
		}
		
		//记录删除信息
		BslStockChangeDetail bslStockChangeDetailRaw = new BslStockChangeDetail();
		bslStockChangeDetailRaw.setTransSerno(createStockChangeId());//流水
		bslStockChangeDetailRaw.setProdId(oldBslProductInfo.getProdId());//产品编号
		bslStockChangeDetailRaw.setPlanSerno(oldBslProductInfo.getProdPlanNo());//对应的原料入库单号
		bslStockChangeDetailRaw.setTransCode(DictItemOperation.库存变动交易码_删除);//交易码
		bslStockChangeDetailRaw.setProdType(DictItemOperation.产品类型_成品);//产品类型
		bslStockChangeDetailRaw.setRubbishWeight(oldBslProductInfo.getProdRelWeight());//重量
		bslStockChangeDetailRaw.setInputuser(oldBslProductInfo.getProdInputuser());//录入人
		bslStockChangeDetailRaw.setCrtDate(new Date());
		int resultStockRaw = bslStockChangeDetailMapper.insert(bslStockChangeDetailRaw);
		if(resultStockRaw<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(resultStockRaw==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
		}
		
		//如果删除的产品指令号是正在执行的指令，更新产品的父级纵剪带状态
		//获取正在执行的产品生产指令
		BslMakePlanInfo makePlanInfoExe = prodPlanService.getProdPlanInfoExe(oldBslProductInfo.getProdMakeJz());
		if(makePlanInfoExe != null){
			if(makePlanInfoExe.getPlanId().equals(oldBslProductInfo.getProdPlanNo())){
				BslProductInfo parentProd = bslProductInfoMapper.selectByPrimaryKey(oldBslProductInfo.getProdParentNo());
				parentProd.setProdStatus(DictItemOperation.产品状态_已出库);
				parentProd.setUpdDate(new Date());
				bslProductInfoMapper.updateByPrimaryKeySelective(parentProd);
			}
		}
		prodPlanService.updateProdRuNumAndSums(oldBslProductInfo.getProdPlanNo());
		return BSLResult.ok(prodId);
	}

	@Override
	public List<BslProductInfoCollect> querySaleOutBill(QueryExample queryExample) {
		return bslProductInfoMapper.querySaleOutBill(queryExample);
	}
	
	@Override
	public List<BslProductInfoCollect> querySaleOutBillWaste(QueryExample queryExample) {
		if(queryExample.getProdIds() != null){
			List<String> prodIds = new ArrayList<String>();
			for (String prodId : queryExample.getProdIds()) {
				prodIds.add(prodId);
			}
			queryExample.setProdIdsList(prodIds);
		}
		return bslProductInfoMapper.querySaleOutBillWaste(queryExample);
	}

	@Override
	public List<BslProductInfoCollect> querySaleOutByProds(QueryExample queryExample) {
		return bslProductInfoMapper.querySaleOutByProds(queryExample);
	}

	/**
	 * 根据盘号获取该盘已经入库的包数
	 */
	@Override
	public BSLResult getProdRuNums(String prodId) {
		//校验父级盘号
		BslProductInfo parentProd = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(parentProd == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到指定的父级纵剪带信息");
		}
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		List<String> list = new ArrayList<String>();
		list.add(DictItemOperation.产品类型_成品);
		list.add(DictItemOperation.产品类型_待处理品);
		criteria.andProdTypeIn(list);
		criteria.andProdParentNoEqualTo(prodId);
		criteria.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		List<BslProductInfo> prods = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		BslRuInFo bslRuInFo = new BslRuInFo();
		bslRuInFo.setProdId(prodId);
		bslRuInFo.setProdRelWeight(parentProd.getProdRelWeight());
		if(prods == null){
			bslRuInFo.setProdRuNum(0);
			bslRuInFo.setProdRuWeight(0f);
		}else{
			Float prodRuWeight = 0f;
			for (BslProductInfo bslProductInfo : prods) {
				prodRuWeight += bslProductInfo.getProdRelWeight();
			}
			bslRuInFo.setProdRuNum(prods.size());
			bslRuInFo.setProdRuWeight(prodRuWeight);
		}
		return BSLResult.ok(bslRuInFo);
	}
	
	/**
	 * 发货车号流水自动生成编号
	 * C+日期+车号+NO+序号
	 * @return
	 */
	public String createCarSernoId(String carNo) {
		long incr = jedisClient.incr(REDIS_NEXT_CAR_ID);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
		String no = sdf.format(new Date()) + sdf2.format(new Date()) + carNo;
		String rawId = String.format("C%sNO%02d", no, incr);
		return rawId;
	}

	/**
	 * 更新产品的发货车号
	 */
	@Override
	public BSLResult updateProdCarNo(List<String> prods,String prodCarNo) {
		BslProductInfo bslProductInfo = new BslProductInfo();
		bslProductInfo.setProdOutCarno(createCarSernoId(prodCarNo));
		for (String prodId : prods) {
			bslProductInfo.setProdId(prodId);
			int result = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfo);
			if(result<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(result==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"更新产品发货车号成功");
			}
		}
		return BSLResult.ok(bslProductInfo.getProdOutCarno());
	}

	/**
	 * 查询外协厂产品信息
	 */
	@Override
	public BSLResult getWxProdService(QueryCriteria queryCriteria) {
		//创建查询的实例，并赋值
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_成品);
		criteria.andProdDclFlagNotEqualTo(DictItemOperation.产品外协厂标志_本厂);
		//产品编号
		if (!StringUtils.isBlank(queryCriteria.getProdId())) {
			criteria.andProdIdLike(StringUtil.likeStr(queryCriteria.getProdId()));
		}
		//成品生产批号
		if (!StringUtils.isBlank(queryCriteria.getProdPlanNo())) {
			criteria.andProdPlanNoLike(StringUtil.likeStr(queryCriteria.getProdPlanNo()));
		}
		//炉号
		if (!StringUtils.isBlank(queryCriteria.getProdLuno())) {
			criteria.andProdLunoLike(StringUtil.likeStr(queryCriteria.getProdLuno()));
		}
		//父级待处理品编号
		if (!StringUtils.isBlank(queryCriteria.getProdOriId())) {
			criteria.andProdOriIdLike(StringUtil.likeStr(queryCriteria.getProdOriId()));
		}
		//出库指令号
		if (!StringUtils.isBlank(queryCriteria.getProdOutPlan())) {
			criteria.andProdOutPlanLike(StringUtil.likeStr(queryCriteria.getProdOutPlan()));
		}
		//产品规格
		if(!StringUtils.isBlank(queryCriteria.getProdNorm())){
			criteria.andProdNormLike("%"+queryCriteria.getProdNorm()+"%");
		}
		//钢种
		if (!StringUtils.isBlank(queryCriteria.getProdMaterial())) {
			criteria.andProdMaterialEqualTo(queryCriteria.getProdMaterial());
		}
		//产品状态
		if (!StringUtils.isBlank(queryCriteria.getProdStatus())) {
			criteria.andProdStatusEqualTo(queryCriteria.getProdStatus());
		}
		//班次
		if (!StringUtils.isBlank(queryCriteria.getProdBc())) {
			criteria.andProdBcEqualTo(queryCriteria.getProdBc());
		}
		//转换单号
		if (!StringUtils.isBlank(queryCriteria.getProdFhck())) {
			criteria.andProdFhckLike(queryCriteria.getProdFhck());
		}
		//外协厂标志
		if (!StringUtils.isBlank(queryCriteria.getProdDclFlag())) {
			criteria.andProdDclFlagEqualTo(queryCriteria.getProdDclFlag());
		}
		
		//开始日期结束日期
		//起始日期 结束日期
		Date dateStart = new Date();
		Date dateEnd = new Date();
		if(!StringUtils.isBlank(queryCriteria.getStartDate())){
			try {
				dateStart = DictItemOperation.日期转换实例.parse(queryCriteria.getStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			try {
				dateStart = DictItemOperation.日期转换实例.parse("2018-01-01");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(!StringUtils.isBlank(queryCriteria.getEndDate())){
			try {
				dateEnd = DictItemOperation.日期转换实例.parse(queryCriteria.getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			dateEnd = new Date();
		}
		criteria.andCrtDateBetween(dateStart,dateEnd);
		
		//分页处理
		PageHelper.startPage(Integer.parseInt(queryCriteria.getPage()), Integer.parseInt(queryCriteria.getRows()));
		//调用sql查询
		if(StringUtils.isBlank(queryCriteria.getSort()) || StringUtils.isBlank(queryCriteria.getOrder())){
			bslProductInfoExample.setOrderByClause("`crt_date` desc,`prod_id` desc,`prod_plan_no`");
		}else{
			String sortSql = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, queryCriteria.getSort());
			if(!StringUtils.isBlank(sortSql)){	
				bslProductInfoExample.setOrderByClause("`"+sortSql+"` "+ queryCriteria.getOrder());
			}
		}
		List<BslProductInfo> bslProductInfos = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		PageInfo<BslProductInfo> pageInfo = new PageInfo<BslProductInfo>(bslProductInfos);
		return BSLResult.ok(bslProductInfos,"prodServiceImpl","getWxProdService",pageInfo.getTotal(),bslProductInfos);
	}
	
	/**
	 * 产品入库-待处理品处理
	 */
	@Override
	public BSLResult addProdFromDclinfo(BslProductInfo bslProductInfo, int sumNum) {
		//二次校验
		//获取待处理品信息
		String prodCompany = "";
		String prodCustomer = "";
		BslProductInfo bslProductInfoDcl = bslProductInfoMapper.selectByPrimaryKey(bslProductInfo.getProdOriId());
		if(bslProductInfoDcl != null){
			//校验炉号
			if(!bslProductInfo.getProdLuno().equals(bslProductInfoDcl.getProdLuno())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品炉号必须与父级待处理品炉号一致");
			}
			//校验钢种
			if(!bslProductInfo.getProdMaterial().equals(bslProductInfoDcl.getProdMaterial())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种必须与父级待处理品钢种一致");
			}
			//校验规格
			/*if(!bslProductInfo.getProdNorm().equals(bslProductInfoDcl.getProdNorm())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格必须是生产指令指定规格");
			}*/
			prodCompany = bslProductInfoDcl.getProdCompany();
			prodCustomer = bslProductInfoDcl.getProdCustomer();
		}
		
		//判断入库盘数,平分重量
		Float relWeight = bslProductInfo.getProdRelWeight()/sumNum;
		relWeight = ((float)Math.round(relWeight*1000))/1000;
		bslProductInfo.setProdRelWeight(relWeight);
		
		//记录入库流水
		BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
		String returnProdId = "";
		String prodId;
		
		for (int i = 0; i < sumNum; i++) {
			//校验完成，开始入库
			prodId = createProdId();
			bslProductInfo.setProdId(prodId);//生成编号
			bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
			bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
			bslProductInfo.setCrtDate(new Date());//创建日期当天
			bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
			bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_本厂);
			bslProductInfo.setProdCompany(prodCompany);//厂家同原来一致
			bslProductInfo.setProdCustomer(prodCustomer);
			
			int result = bslProductInfoMapper.insert(bslProductInfo);
			if(result<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(result==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
			}
			
			//插入成功之后记录插入流水
			bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//记录返回起始编号
			if(i==0){
				returnProdId = prodId;
			}
		}
		return BSLResult.ok(returnProdId);
	}


	/**
	 * 外协产品入库
	 */
	@Override
	public BSLResult addProdWxinfoB(BslProductInfo bslProductInfo, int sumNum) {
		//二次校验
		//获取待处理品信息
		String prodCompany = "";
		String prodCustomer = "";
		BslProductInfo bslProductInfoDcl = bslProductInfoMapper.selectByPrimaryKey(bslProductInfo.getProdOriId());
		if(bslProductInfoDcl != null){
			//校验炉号
			if(!bslProductInfo.getProdLuno().equals(bslProductInfoDcl.getProdLuno())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品炉号必须与父级待处理品炉号一致");
			}
			//校验钢种
			if(!bslProductInfo.getProdMaterial().equals(bslProductInfoDcl.getProdMaterial())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种必须与父级待处理品钢种一致");
			}
			//校验规格
			/*if(!bslProductInfo.getProdNorm().equals(bslProductInfoDcl.getProdNorm())){
				throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格必须是生产指令指定规格");
			}*/
			prodCompany = bslProductInfoDcl.getProdCompany();
			prodCustomer = bslProductInfoDcl.getProdCustomer();
		}
		
		//判断入库盘数,平分重量
		Float relWeight = bslProductInfo.getProdRelWeight()/sumNum;
		relWeight = ((float)Math.round(relWeight*1000))/1000;
		bslProductInfo.setProdRelWeight(relWeight);
		
		//记录入库流水
		BslStockChangeDetail bslStockChangeDetail = new BslStockChangeDetail();
		String returnProdId = "";
		String prodId;
		
		for (int i = 0; i < sumNum; i++) {
			//校验完成，开始入库
			prodId = createProdIdWx();
			bslProductInfo.setProdId(prodId);//生成编号
			bslProductInfo.setProdType(DictItemOperation.产品类型_成品);
			bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
			bslProductInfo.setCrtDate(new Date());//创建日期当天
			bslProductInfo.setProdStatus(DictItemOperation.产品状态_已入库);
			bslProductInfo.setProdDclFlag(DictItemOperation.产品外协厂标志_加工);
			bslProductInfo.setProdCompany(prodCompany);//厂家同原来一致
			bslProductInfo.setProdCustomer(prodCustomer);
			
			int result = bslProductInfoMapper.insert(bslProductInfo);
			if(result<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(result==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"入库失败");
			}
			
			//插入成功之后记录插入流水
			bslStockChangeDetail = new BslStockChangeDetail();
			bslStockChangeDetail.setTransSerno(createStockChangeId());//流水
			bslStockChangeDetail.setProdId(bslProductInfo.getProdId());//产品编号
			bslStockChangeDetail.setPlanSerno(bslProductInfo.getProdPlanNo());//对应的生产指令号
			bslStockChangeDetail.setTransCode(DictItemOperation.库存变动交易码_入库);//交易码
			bslStockChangeDetail.setProdType(DictItemOperation.产品类型_成品);//产品类型
			bslStockChangeDetail.setRubbishWeight(bslProductInfo.getProdRelWeight());//重量
			bslStockChangeDetail.setInputuser(bslProductInfo.getProdCheckuser());//录入人
			bslStockChangeDetail.setCrtDate(new Date());
			int resultStock = bslStockChangeDetailMapper.insert(bslStockChangeDetail);
			if(resultStock<0){
				throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
			}else if(resultStock==0){
				throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
			}
			
			//记录返回起始编号
			if(i==0){
				returnProdId = prodId;
			}
		}
		return BSLResult.ok(returnProdId);
	}

	/**
	 * 外协厂产品修改
	 */
	@Override
	public BSLResult updateProdWxInfo(BslProductInfo bslProductInfo) {
		//校验管理员
		if(!DictItemOperation.管理员.equals(bslProductInfo.getProdInputuser())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有管理员才允许修改产品");
		}
		//获取原产品信息进行校验
		BslProductInfo oldBslProductInfo = bslProductInfoMapper.selectByPrimaryKey(bslProductInfo.getProdId());
		if(oldBslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据件号查询记录为空");
		}
		//校验状态，只有是1-入库中的状态才能修改
		/*if(!DictItemOperation.产品状态_已入库.equals(oldBslProductInfo.getProdStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有在库的产品才能修改");
		}*/
		//校验不允许修改的内容
		//校验钢种
		/*if(!oldBslProductInfo.getProdMaterial().equals(bslProductInfo.getProdMaterial())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品钢种不允许修改");
		}*/
		//校验规格
		/*if(!oldBslProductInfo.getProdNorm().equals(bslProductInfo.getProdNorm())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "产品规格不允许修改");
		}*/
		
		//校验完成开始修改
		bslProductInfo.setUpdDate(new Date());
		bslProductInfo.setProdPrintWeight(bslProductInfo.getProdRelWeight());//打印重量为实际重量
		int result = bslProductInfoMapper.updateByPrimaryKeySelective(bslProductInfo);
		if(result<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(result==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"根据条件没有符合的修改记录");
		}
		return BSLResult.ok(bslProductInfo.getProdId());
	}

	/**
	 * 外协厂产品删除
	 */
	@Override
	public BSLResult deleteWxProd(String prodId, String user) {
		//校验人员
		if(!DictItemOperation.管理员.equals(user)){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有管理员才允许删除产品");
		}
		//获取原产品信息进行校验
		BslProductInfo oldBslProductInfo = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(oldBslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据产品编号查询记录为空");
		}
		//校验状态，只有是1-入库中的状态才能修改
		if(!DictItemOperation.产品状态_已入库.equals(oldBslProductInfo.getProdStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有在库的产品才允许删除");
		}
		//开始删除
		int result = bslProductInfoMapper.deleteByPrimaryKey(prodId);
		if(result<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(result==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"根据条件没有符合的修改记录");
		}
		
		//记录删除信息
		BslStockChangeDetail bslStockChangeDetailRaw = new BslStockChangeDetail();
		bslStockChangeDetailRaw.setTransSerno(createStockChangeId());//流水
		bslStockChangeDetailRaw.setProdId(oldBslProductInfo.getProdId());//产品编号
		bslStockChangeDetailRaw.setPlanSerno(oldBslProductInfo.getProdPlanNo());//对应的原料入库单号
		bslStockChangeDetailRaw.setTransCode(DictItemOperation.库存变动交易码_删除);//交易码
		bslStockChangeDetailRaw.setProdType(DictItemOperation.产品类型_成品);//产品类型
		bslStockChangeDetailRaw.setRubbishWeight(oldBslProductInfo.getProdRelWeight());//重量
		bslStockChangeDetailRaw.setInputuser(oldBslProductInfo.getProdInputuser());//录入人
		bslStockChangeDetailRaw.setCrtDate(new Date());
		int resultStockRaw = bslStockChangeDetailMapper.insert(bslStockChangeDetailRaw);
		if(resultStockRaw<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(resultStockRaw==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
		}
		
		return BSLResult.ok(prodId);
	}

	/**
	 * 入库产品拆分
	 */
	@Override
	public BSLResult updateProdInfoCut(QueryCriteria queryCriteria) {
		String prodId = queryCriteria.getProdId();
		if(StringUtils.isBlank(prodId)){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "产品编号不能为空！");
		}
		//获取原产品信息进行校验
		BslProductInfo oldBslProductInfo = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(oldBslProductInfo == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "根据产品编号查询记录为空！");
		}
		//校验产品状态是在库
		if(!DictItemOperation.产品状态_已入库.equals(oldBslProductInfo.getProdStatus())){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "只有在库产品允许拆分！");
		}
		//开始拆分
		if(queryCriteria.getProdNum1() == null || Integer.valueOf(queryCriteria.getProdNum1()) == 0){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包1支数不能为空");
		}
		if(queryCriteria.getProdNum2() == null || Integer.valueOf(queryCriteria.getProdNum2()) == 0){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包2支数不能为空");
		}
		if(queryCriteria.getProdRelWeight1() == null || Float.valueOf(queryCriteria.getProdRelWeight1()) == 0){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包1重量不能为空");
		}
		if(queryCriteria.getProdRelWeight2() == null || Float.valueOf(queryCriteria.getProdRelWeight2()) == 0){
			throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包2重量不能为空");
		}
		int prodNum1 = Integer.valueOf(queryCriteria.getProdNum1());//支数1
		Float prodWeight1 = Float.valueOf(queryCriteria.getProdRelWeight1());//重量1
		
		int prodNum2 = Integer.valueOf(queryCriteria.getProdNum2());//支数2
		Float prodWeight2 = Float.valueOf(queryCriteria.getProdRelWeight2());//重量2
		
		int prodNum3 = 0;
		Float prodWeight3 = 0f;
		
		int prodNum4 = 0;
		Float prodWeight4 = 0f;
		
		if(!StringUtils.isBlank(queryCriteria.getProdNum3()) && Integer.valueOf(queryCriteria.getProdNum3()) != 0){
			prodNum3 = Integer.valueOf(queryCriteria.getProdNum3());
			if(StringUtils.isBlank(queryCriteria.getProdRelWeight3()) || Float.valueOf(queryCriteria.getProdRelWeight3()) == 0){
				throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包3有支数时重量不能为空");
			}else{
				prodWeight3 = Float.valueOf(queryCriteria.getProdRelWeight3());
			}
		}
		
		if(!StringUtils.isBlank(queryCriteria.getProdNum4()) && Integer.valueOf(queryCriteria.getProdNum4()) != 0){
			prodNum4 = Integer.valueOf(queryCriteria.getProdNum4());
			if(StringUtils.isBlank(queryCriteria.getProdRelWeight4()) || Float.valueOf(queryCriteria.getProdRelWeight4()) == 0){
				throw new BSLException(ErrorCodeInfo.错误类型_参数为空, "包4有支数时重量不能为空");
			}else{
				prodWeight4 = Float.valueOf(queryCriteria.getProdRelWeight3());
			}
		}
		
		//校验总支数、总重量
		if(oldBslProductInfo.getProdNum() != (prodNum1+prodNum2+prodNum3+prodNum4)){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "拆分后总支数应等于原产品支数");
		}
		if(oldBslProductInfo.getProdRelWeight() < (prodWeight1+prodWeight2+prodWeight3+prodWeight4)){
			throw new BSLException(ErrorCodeInfo.错误类型_状态校验错误, "拆分后总重量不能大于原产品重量");
		}
		
		//开始拆分
		oldBslProductInfo.setCrtDate(new Date());
		oldBslProductInfo.setProdInputuser(queryCriteria.getProdInputuser());
		//第一包入库
		oldBslProductInfo.setProdId(prodId+"-1");
		oldBslProductInfo.setProdRelWeight(prodWeight1);
		oldBslProductInfo.setProdPrintWeight(prodWeight1);
		oldBslProductInfo.setProdNum(prodNum1);
		bslProductInfoMapper.insert(oldBslProductInfo);
		//第二包入库
		oldBslProductInfo.setProdId(prodId+"-2");
		oldBslProductInfo.setProdRelWeight(prodWeight2);
		oldBslProductInfo.setProdPrintWeight(prodWeight2);
		oldBslProductInfo.setProdNum(prodNum2);
		bslProductInfoMapper.insert(oldBslProductInfo);
		//第三包入库
		if(prodNum3 > 0){
			oldBslProductInfo.setProdId(prodId+"-3");
			oldBslProductInfo.setProdRelWeight(prodWeight3);
			oldBslProductInfo.setProdPrintWeight(prodWeight3);
			oldBslProductInfo.setProdNum(prodNum3);
			bslProductInfoMapper.insert(oldBslProductInfo);
		}
		//第四包入库
		if(prodNum4 > 0){
			oldBslProductInfo.setProdId(prodId+"-4");
			oldBslProductInfo.setProdRelWeight(prodWeight4);
			oldBslProductInfo.setProdPrintWeight(prodWeight4);
			oldBslProductInfo.setProdNum(prodNum4);
			bslProductInfoMapper.insert(oldBslProductInfo);
		}
		//删除原产品
		bslProductInfoMapper.deleteByPrimaryKey(prodId);
		
		//记录删除信息
		BslStockChangeDetail bslStockChangeDetailRaw = new BslStockChangeDetail();
		bslStockChangeDetailRaw.setTransSerno(createStockChangeId());//流水
		bslStockChangeDetailRaw.setProdId(prodId);//产品编号
		bslStockChangeDetailRaw.setTransCode(DictItemOperation.库存变动交易码_删除);//交易码
		bslStockChangeDetailRaw.setProdType(DictItemOperation.产品类型_成品);//产品类型
		bslStockChangeDetailRaw.setRubbishWeight(oldBslProductInfo.getProdRelWeight());//重量
		bslStockChangeDetailRaw.setInputuser(queryCriteria.getProdInputuser());//录入人
		bslStockChangeDetailRaw.setRemark("拆分删除");
		bslStockChangeDetailRaw.setCrtDate(new Date());
		int resultStockRaw = bslStockChangeDetailMapper.insert(bslStockChangeDetailRaw);
		if(resultStockRaw<0){
			throw new BSLException(ErrorCodeInfo.错误类型_数据库错误,"sql执行异常！");
		}else if(resultStockRaw==0){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录,"新增库存变动表失败");
		}
		
		return BSLResult.ok(prodId+"-1");
	}

	/**
	 * 查询某指令制造的产品用料组成信息
	 */
	@Override
	public BSLResult getProdMakeUseInfo(QueryCriteria queryCriteria) {
		if(!StringUtils.isBlank(queryCriteria.getProdId())){
			queryCriteria.setProdId(StringUtil.likeStr(queryCriteria.getProdId()));
		}else{
			queryCriteria.setProdId(null);
		}
		if(!StringUtils.isBlank(queryCriteria.getProdParentNo())){
			queryCriteria.setProdParentNo(StringUtil.likeStr(queryCriteria.getProdParentNo()));
		}else{
			queryCriteria.setProdParentNo(null);
		}
		//分页处理
		PageHelper.startPage(Integer.parseInt(queryCriteria.getPage()), Integer.parseInt(queryCriteria.getRows()));
		List<BslProdMakeUseInfo> lists = bslProductInfoMapper.getProdMakeUseInfo(queryCriteria);
		PageInfo<BslProdMakeUseInfo> pageInfo = new PageInfo<BslProdMakeUseInfo>(lists);
		return BSLResult.ok(lists,"prodServiceImpl","getProdMakeUseInfo",pageInfo.getTotal(),lists);
		
	}

	/**
	 * 根据盘号获取已入库待处理品包数
	 */
	@Override
	public BSLResult getProdDclRuNums(String prodId) {
		//校验父级盘号
		BslProductInfo parentProd = bslProductInfoMapper.selectByPrimaryKey(prodId);
		if(parentProd == null){
			throw new BSLException(ErrorCodeInfo.错误类型_查询无记录, "没有查询到指定的父级纵剪带信息");
		}
		BslProductInfoExample bslProductInfoExample = new BslProductInfoExample();
		Criteria criteria = bslProductInfoExample.createCriteria();
		criteria.andProdTypeEqualTo(DictItemOperation.产品类型_待处理品);
		criteria.andProdParentNoEqualTo(prodId);
		criteria.andProdDclFlagEqualTo(DictItemOperation.产品外协厂标志_本厂);
		List<BslProductInfo> prods = bslProductInfoMapper.selectByExample(bslProductInfoExample);
		BslRuInFo bslRuInFo = new BslRuInFo();
		bslRuInFo.setProdId(prodId);
		bslRuInFo.setProdRelWeight(parentProd.getProdRelWeight());
		if(prods == null){
			bslRuInFo.setProdRuNum(0);
			bslRuInFo.setProdRuWeight(0f);
		}else{
			Float prodRuWeight = 0f;
			for (BslProductInfo bslProductInfo : prods) {
				prodRuWeight += bslProductInfo.getProdRelWeight();
			}
			bslRuInFo.setProdRuNum(prods.size());
			bslRuInFo.setProdRuWeight(prodRuWeight);
		}
		return BSLResult.ok(bslRuInFo);
	}

}
