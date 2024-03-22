<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<style>
	tr{height:30px}
</style>
<div style="padding:10px 10px 10px 10px">
	<form id="prodAddForm" class="itemForm" method="post">
	   <table>
	   		<tr> 
	        	 <td width="120"  align="right">生产机组:</td>
		            <td width="210"  align="right">
		            	<select name="prodMakeJz" id="prodMakeJzM3005Add"  panelHeight="auto" class="easyui-combobox" data-options="editable:true,required:true,onChange:onplanJzChangeM3005Add" style="width:200px;">
				          <option value="">请选择...</option>
				          <c:forEach items="${prodUnitsSCList}" var="a">
				          	   	<option value="${a.enumKey}">${a.enumValue}</option>
				          	  </c:forEach>
						</select>
		            </td>
	        </tr>
	        <tr>
	            <td width="120"  align="right">产品生产指令:</td>
	            <td width="210"  align="right">
	            	<input name="prodPlanNo" id="prodPlanNoM3005Add" type="text" readonly="readonly" class="easyui-textbox" data-options="required:true" style="width:200px;"/>
	            </td>            
	            <td width="120" align="right">产品名称:</td>
	            <td width="210" align="right">
	            	<input name="prodName" id="prodNameM3005Add" class="easyui-textbox" type="text" readonly="readonly" data-options="required:true,validType:'length[0,40]'" style="width:200px;"></input>
	            </td>      
	        </tr>
	        <tr>	
	            <td width="120" align="right">产品钢种:</td>
	            <td width="210" align="right">
	            	<select name="prodMaterial" id="prodMaterialM3005Add" class="easyui-combobox" panelHeight="auto" readonly="readonly" data-options="editable:true" style="width:200px;">
			            <option value="">请选择...</option>
			           <c:forEach items="${prodMaterialList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			          </c:forEach>
					</select>
				</td>
	            <td width="120" align="right">产品规格:</td>
	             <td width="210" align="right">
	            	<input name="prodNorm" id="prodNormM3005Add" class="easyui-textbox" type="text" readonly="readonly" data-options="required:true,validType:'length[0,40]'" style="width:200px;"></input>
	            </td>
	        </tr>
	        <tr>     
	            <td width="120" align="right">在库盘号1:</td>
	             <td width="210" align="right">
	            	<input name="prodParentNo1" id="prodParentNo1M3005Add" class="easyui-textbox" type="text" readonly="readonly" data-options="required:true" style="width:200px;"></input>
				</td>
				<td width="120" align="right">盘号1剩余重量:</td>
	            <td width="210" align="right">
	            	<input name="prodRelWeightParent1" id="prodRelWeightParent1M3005Add"  readonly="readonly"  class="easyui-numberbox" type="text" data-options="required:false,min:0,precision:3,validType:'length[0,10]'"  style="width:200px;"></input>
	            </td>
	        </tr>
	       <tr>     
	            <td width="120" align="right">在库盘号2:</td>
	             <td width="210" align="right">
	            	<input name="prodParentNo2" id="prodParentNo2M3005Add" class="easyui-textbox" type="text" readonly="readonly" data-options="required:false" style="width:200px;"></input>
				</td>
				<td width="120" align="right">盘号2剩余重量:</td>
	            <td width="210" align="right">
	            	<input name="prodRelWeightParent2" id="prodRelWeightParent2M3005Add"  readonly="readonly"  class="easyui-numberbox" type="text" data-options="required:false,min:0,precision:3,validType:'length[0,10]'"  style="width:200px;"></input>
	            </td>
	        </tr>
	        <tr>
	            <td width="120" align="right">产品定尺(米):</td>
	            <td width="210" align="right">
	            	<input name="prodLength" class="easyui-numberbox" type="text" data-options="required:true,min:0,precision:3,validType:'length[0,10]'" style="width:200px;"></input>
	            </td>
	            <td width="120" align="right">产品总重量/吨:</td>
	            <td width="210" align="right">
	            	<input name="prodRelWeight" id="prodRelWeightM3005" class="easyui-numberbox" type="text" data-options="required:true,min:0,precision:3,validType:'length[0,10]'"  style="width:200px;"></input>
	            </td>
	            <td width="120" align="left">
	        		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="getWeightCheckM3005()" style="width:70px;">称重</a>
	        	</td>
	        </tr>
	        <tr>
	        	<td width="120" align="right">单包支数:</td>
	            <td width="210" align="right">
	            	<input name="prodNum" class="easyui-numberbox" type="text" data-options="required:true,min:0,precision:0,validType:'length[0,10]'" style="width:200px;"></input>
	            </td>
	        	<td width="120" align="right">入库包数:</td>
	        	<td width="210" align="right">
	            	<input name="sumNum" id="sumNumM3005" class="easyui-numberbox" type="text" data-options="required:true,min:1,precision:0,validType:'length[0,10]'" value="1" style="width:200px;"></input>
	            </td>
	        </tr>
	        <tr>	
	           <td width="120" align="right">生产班次:</td>
	            <td width="210" align="right">
	            	<select name="prodBc" class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:true" style="width:200px;">
			           <option value="">请选择...</option>
			          <c:forEach items="${prodBcList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			          </c:forEach>
					</select>
	            </td> 
	            <td width="120" align="right">入库仓库/区:</td>
	            <td width="210" align="right">
	            	<select name="prodRuc" id="prodRucM3005Add" class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:true" style="width:200px;">
			           <option value="">请选择...</option>
			          <c:forEach items="${prodRucList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			          </c:forEach>
					</select>
	            </td> 
	        </tr>
	        <tr>	
	           <td width="120" align="right">质量等级:</td>
	            <td width="210" align="right">
	            	<select name="prodLevel" class="easyui-combobox" panelHeight="auto" data-options="editable:true" style="width:200px;">
			         <option value="">请选择...</option>
			          <c:forEach items="${prodLevelList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			          </c:forEach>
					</select>
	            </td>
	        	<td width="120" align="right">备注:</td>
	            <td width="210" align="right">
	            	<input name="remark" class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,120]'" style="width:200px;" maxLength="10"></input>
	            </td> 
	        </tr>
	        <tr hidden="true">
	            <td width="120" align="right">录入人:</td>
	            <td width="210" align="right">
	            	<input name="prodInputuser" id="prodInputuserM3005Add" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
	            </td>
		    </tr>	
	    </table>
	</form>
	<div style="margin-top:10px" align="center">
	    <a href="javascript:void(0)" class="easyui-linkbutton" id="M3005Add" onclick="submitM3005AddForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeM3005AddForm()">关闭</a>
	</div>
</div>
<script type="text/javascript">
	
	//联动称重机获取实际重量
	function getWeightCheckM3005(){
		var params = {};
		$.post("/rxtx/weighing",params, function(data){
			if(data.status == 200){
				$("#prodRelWeightM3005").numberbox('setValue',data.data);
			}else{
				$.messager.alert('提示','错误码：'+data.status+',错误信息：'+data.msg);
			}
		});
	}
	
	//选择盘号回显已入库包数（不用）
	function onProdParentNoChangeM3005Add(){
		var pardParentNo = $("#prodParentNoM3005Add").textbox('getValue');
		var params = {"prodId":pardParentNo};
		$.post("/prodManager/getProdRuNums",params, function(data){
			if(data.status == 200){
				$("#prodRelWeightParentM3005Add").textbox('setValue',data.data.prodRelWeight+'');
				$("#prodRuNumM3005Add").textbox('setValue',data.data.prodRuNum+'');
				$("#prodRuWeightM3005Add").textbox('setValue',data.data.prodRuWeight+'');
			}else{
				$.messager.alert('提示','错误码：'+data.status+',错误信息：'+data.msg);
			}
		});
	}
	
	//提交表单
	function submitM3005AddForm(){
		
		//记录录入人员信息
		var inputUser = $("#user_id").html();
		$("#prodInputuserM3005Add").textbox('setValue',inputUser);
		var sumNum = $("#sumNumM3005").numberbox('getValue');
		//有效性验证
		if(!$('#prodAddForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return;
		}
		
		//ajax的post方式提交表单
		//$("#itemAddForm").serialize()将表单序列号为key-value形式的字符串
		$.messager.confirm('确认','是否确认入库？',function(r){
			if (r){
				$('#M3005Add').linkbutton('disable'); 
				$.post("/prodManager/addProd",$("#prodAddForm").serialize(), function(data){
					if(data.status == 200){
						var prodId = data.data;
						$.messager.alert('提示','产品入库成功!起始单号为：'+prodId,'info',function(){
							if(sumNum<=1){
								$.messager.confirm('确认','是否需要打印PDF标签文件？',function(r){
									if (r){
					        	    	var mapParam = new Map();
					        			mapParam.set("url","/import/importPdf");
					        			mapParam.set("prodId",prodId);
					        			mapParam.set("tradeType","M3005");
					        			BSL.toNewPagePDF(mapParam);
					        	    }
								});
							}
							$("#prodAddWindow").window('close');
							searchM3005Form();
							//$("#receiptList").datagrid("reload");
						});
					}else{
						$.messager.alert('提示','错误码：'+data.status+',错误信息：'+data.msg);
					}
					$('#M3005Add').linkbutton('enable');
				});
			}else{
				$("#prodAddWindow").window('close');
			}
		});
	}
	
	function closeM3005AddForm(){
		$("#prodAddWindow").window('close');
	}
	
	//根据生产机组回显正在生产的指令等信息
	function onplanJzChangeM3005Add(){
		var jz = $("#prodMakeJzM3005Add").combobox("getValue");
		var params = {
			'planJz':jz
		};
		//判断该机组已出库的纵剪带是不是已达最大值
		$.post("/prodManager/isMaxOut", params, function(dataIsMax) {
			if (dataIsMax.status == 200) {
				if(dataIsMax.data == '1'){
					$.messager.confirm('确认','该机组正在出库的纵剪带数量已达阈值，请判断继续入库，还是先进行销库操作。是否继续入库？',function(r){
						if (r){
							if(jz == "3"){
								$("#prodRucM3005Add").combobox('setValue','2');
							}else if(jz == "4"){
								$("#prodRucM3005Add").combobox('setValue','3');
							}
							$.post("/prodManager/getPlanExeInfo", params, function(data) {
								if (data.status == 200) {
									$("#prodPlanNoM3005Add").textbox('setValue',data.data.planId);
									$("#prodNameM3005Add").textbox('setValue',data.data.makeName);
									$("#prodNormM3005Add").textbox('setValue',data.data.makeProdNorm);
									$("#prodMaterialM3005Add").combobox('setValue',data.data.prodMaterial);
									//给父级盘号1、2赋值
									params = {
										'planId':data.data.planId
									};
									$.post("/prodManager/getParentZjxInfo", params, function(data2) {
										if (data2.status == 200) {
											if(data2.data.prodRelWeightParent1 == 0){
												$.messager.alert('提示', "纵剪带出库记录查询异常，请关闭该界面刷新重试");
												return false;
											}
											$("#prodParentNo1M3005Add").textbox('setValue',data2.data.prodParentNo1);
											$("#prodParentNo2M3005Add").textbox('setValue',data2.data.prodParentNo2);
											$("#prodRelWeightParent1M3005Add").numberbox('setValue',data2.data.prodRelWeightParent1);
											$("#prodRelWeightParent2M3005Add").numberbox('setValue',data2.data.prodRelWeightParent2);
										} else {
											$.messager.alert('提示', data.msg);
											return false;
										}
										
									});
									
								} else {
									$.messager.alert('提示', data.msg);
									$('#prodPlanNoM3005Add').textbox('setValue','');
									$('#prodNameM3005Add').textbox('setValue','');
									$('#prodNormM3005Add').textbox('setValue','');
									$('#prodMaterialM3005Add').combobox('setValue','');
									$("#prodParentNo1M3005Add").textbox('setValue','');
									$("#prodParentNo2M3005Add").textbox('setValue','');
									$("#prodRelWeightParent1M3005Add").numberbox('setValue','');
									$("#prodRelWeightParent2M3005Add").numberbox('setValue','');
									return false;
								}
							});
						}else{
							$("#prodAddWindow").window('close');
						}
					});
				}else{
					if(jz == "3"){
						$("#prodRucM3005Add").combobox('setValue','2');
					}else if(jz == "4"){
						$("#prodRucM3005Add").combobox('setValue','3');
					}
					$.post("/prodManager/getPlanExeInfo", params, function(data) {
						if (data.status == 200) {
							$("#prodPlanNoM3005Add").textbox('setValue',data.data.planId);
							$("#prodNameM3005Add").textbox('setValue',data.data.makeName);
							$("#prodNormM3005Add").textbox('setValue',data.data.makeProdNorm);
							$("#prodMaterialM3005Add").combobox('setValue',data.data.prodMaterial);
							//给父级盘号1、2赋值
							params = {
								'planId':data.data.planId
							};
							$.post("/prodManager/getParentZjxInfo", params, function(data2) {
								if (data2.status == 200) {
									if(data2.data.prodRelWeightParent1 == 0){
										$.messager.alert('提示', "纵剪带出库记录查询异常，请关闭该界面刷新重试");
										return false;
									}
									$("#prodParentNo1M3005Add").textbox('setValue',data2.data.prodParentNo1);
									$("#prodParentNo2M3005Add").textbox('setValue',data2.data.prodParentNo2);
									$("#prodRelWeightParent1M3005Add").numberbox('setValue',data2.data.prodRelWeightParent1);
									$("#prodRelWeightParent2M3005Add").numberbox('setValue',data2.data.prodRelWeightParent2);
								} else {
									$.messager.alert('提示', data.msg);
									return false;
								}
								
							});
							
						} else {
							$.messager.alert('提示', data.msg);
							$('#prodPlanNoM3005Add').textbox('setValue','');
							$('#prodNameM3005Add').textbox('setValue','');
							$('#prodNormM3005Add').textbox('setValue','');
							$('#prodMaterialM3005Add').combobox('setValue','');
							$("#prodParentNo1M3005Add").textbox('setValue','');
							$("#prodParentNo2M3005Add").textbox('setValue','');
							$("#prodRelWeightParent1M3005Add").numberbox('setValue','');
							$("#prodRelWeightParent2M3005Add").numberbox('setValue','');
							return false;
						}
					});
				}
			}else{
				$.messager.alert('提示', dataIsMax.msg);
				return false;
			}
		}); 	
		
	}
	
</script>
