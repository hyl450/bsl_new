<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
	<div class="easyui-panel" title="产品用料组成信息查询" style="padding:10px 10px 10px 10px">
		<form id="prodUseInfoM3007Form" class="itemForm" method="post">
		   <table>
		        <tr>
		        	<td width="120" align="right">生产指令号:</td>
		            <td width="210" align="right">
		            	<input name="prodPlanNo" id="prodPlanNoM3007"  class="easyui-textbox" type="text" data-options="required:true,validType:'length[0,20]'" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">产品编码:</td>
		            <td width="210" align="right">
		            	<input name="prodId" id="prodIdM3007"  class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,20]'" style="width:200px;"></input>
		            </td>
		        </tr>
		        <tr>
		            <td width="120" align="right">用料产品编码:</td>
		            <td width="210" align="right">
		            	<input name="prodParentNo" id="prodParentNoM3007"  class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,20]'" style="width:200px;"></input>
		            </td>
		        </tr>
		        <tr hidden="true">
		            <td width="120" align="right">页码:</td>
		            <td width="210" align="right">
		            	<input name="page" id="pageM3007" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">每页记录数:</td>
		            <td width="210" align="right">
		            	<input name="rows" id="rowsM3007" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		        </tr>
		        <tr hidden="true">
		              <td width="120" align="right">类名:</td>
		            <td width="210" align="right">
		            	<input name="className" id="classNameM3007" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">方法名:</td>
		            <td width="210" align="right">
		            	<input name="methodName" id="methodNameM3007" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		        </tr>	
		        <tr hidden="true">
		        	<td width="120" align="right">录入人:</td>
		            <td width="210" align="right">
		            	<input name="prodInputuser" id="prodInputuserM3007" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		        </tr>		        
		    </table>
		</form>
		
		<div style="margin-top:10px" align="center">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchM3007Form()">查询</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearM3007Form()">重置</a>
		</div>
	</div>
	 <table class="easyui-datagrid" id="prodUseInfoM3007List" title="产品用料组成信息"  style="height:600px"
	       data-options="singleSelect:true,rownumbers:true,collapsible:true,pagination:true,url:'/prodManager/queryUseInfo',method:'post',onBeforeLoad:onBeforeLoadM3007,pageSize:30,toolbar:toolbarM3007">
	    <thead>
	        <tr>
	        	<th data-options="field:'ck',checkbox:true"></th>
	            <th data-options="field:'prodId',width:120">产品编码</th>
	        	<th data-options="field:'prodPlanNo',width:100">生产指令号</th>
	        	<th data-options="field:'prodName',width:100">产品名称</th>
	        	<th data-options="field:'prodType',width:100,formatter:BSL.formatProdType,sortable:true">产品类别</th>
	            <th data-options="field:'prodRelWeight',width:100">复磅重量/吨</th>
	            <th data-options="field:'prodPrintWeight',width:100">入库重量/吨</th>
	            <th data-options="field:'prodUseNo',width:120">用料产品编码</th>
	            <th data-options="field:'prodUseWeight',width:120">用料重量/吨</th>
	            <th data-options="field:'prodUseZb',width:120">占比</th>
	        	<th data-options="field:'prodLuno',width:100">产品炉(批)号</th>
	        	<th data-options="field:'prodNorm',width:100">产品规格</th>
	        	<th data-options="field:'prodMaterial',width:100,formatter:BSL.formatProdMaterial">产品钢种</th>
	            <th data-options="field:'crtDate',width:150,formatter:BSL.formatFullDateTime">入库日期</th>
	            <th data-options="field:'remark',width:226">备注</th>
	        </tr>
	    </thead>
	</table>
</div>
<script>

	function onBeforeLoadM3007(){
		var queryParams = $('#prodUseInfoM3007List').datagrid('options').queryParams;
		queryParams.prodPlanNo = $('#prodPlanNoM3007').val();
		queryParams.prodId = $('#prodIdM3007').val();
		queryParams.prodParentNo = $('#prodParentNoM3007').val();
	}
	
	//查询按钮
	function searchM3007Form(){
		var prodPlanNo = $('#prodPlanNoM3007').val();
		if(prodPlanNo == null || prodPlanNo == ''){
			$.messager.alert('提示','产品生产指令号不能为空');
			return;
		}
		//page页码
		var page = $("#prodUseInfoM3007List").datagrid('options').pageNumber;
		//rows每页记录条数  
        var rows = $("#prodUseInfoM3007List").datagrid('options').pageSize; 
		$("#pageM3007").textbox('setValue',page);
		$("#rowsM3007").textbox('setValue',rows);
		//ajax的post方式提交表单
		//$("#receiptSearchForm").serialize()将表单序列号为key-value形式的字符串
		$.post("/prodManager/queryUseInfo",$("#prodUseInfoM3007Form").serialize(), function(data){
			if(data.status == 200){				
	            $('#prodUseInfoM3007List').datagrid('loadData', {"total":data.total,"rows":data.rows});
	            $("#classNameM3007").textbox('setValue',data.className);
				$("#methodNameM3007").textbox('setValue',data.methodName);
			}else{
				$.messager.alert('提示',data.msg);
			}
		});
		onBeforeLoadM3007();
	}
	
	/* 重置表单 */
	function clearM3007Form(){
		$('#prodUseInfoM3007Form').form('reset');
	}
   
	var toolbarM3007 = [{
		    	text : '导出EXCEL',
		        iconCls : 'icon-save',
		        handler : function() {
		        	//获取后台传递参数className methodName
					var className = $("#prodUseInfoM3007List").datagrid("getData").className;
					var methodName = $("#prodUseInfoM3007List").datagrid("getData").methodName;
					/**
					 * datagrid属性中获取不到className、methodName时，需要从隐藏域中获取；
					 * 主要是查询时，datagrid属性className、methodName会丢失
					 */
					if(className == "" || className==null || className == undefined){
						className = $('#classNameM3007').val();
					}
					if(methodName == "" || methodName==null || methodName == undefined){
						methodName = $('#methodNameM3007').val();
					}
					//获取表头信息
					var header = $("#prodUseInfoM3007List").datagrid("options").columns[0];
					var fields = "";
					var titles = "";
					for(var i=1;i<header.length;i++){
						var field = header[i].field;
						var title = header[i].title;
						var hiddenFlag = header[i].hidden;
						if(!hiddenFlag){
							var dh = i == (header.length-1) ? "":",";
							fields = fields + field + dh;
							titles = titles + title + dh;
						}
					}
					var mapParam = new Map();
					//必传
					mapParam.set("url","/export/exportExcelExtra");
					mapParam.set("fields",fields);
					mapParam.set("titles",titles);
					mapParam.set("className",className);
					mapParam.set("methodName",methodName);
					mapParam.set("excelName","产品用料组成信息");
					mapParam.set("loginUserId",$("#user_id").html());
					mapParam.set("rows","10000");
					mapParam.set("page","1");
					//查询条件 把所有查询条件带上
					mapParam.set("prodPlanNo",$('#prodPlanNoM3007').val());
					mapParam.set("prodId",$('#prodIdM3007').val());
					mapParam.set("prodParentNo",$('#prodParentNoM3007').val());
					
					BSL.toExcel(mapParam);
		        }
		    }];
</script>