<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
	<div class="easyui-panel" title="纵剪带生产指令查询" style="padding:10px 10px 10px 10px">
		<form id="planSearchForm" class="itemForm" method="post">
		   <table>
		        <tr>
		        	<td width="120" align="right">纵剪带生产指令号:</td>
		            <td width="210" align="right">
		            	<input name="planId" id="planIdM2001" class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,20]'" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">生产指令状态: </td>
		            <td width="210" align="right">
		            	<select name="planStatus" id="planStatusM2001" class="easyui-combobox" panelHeight="auto" data-options="editable:true" style="width:200px;">
				          <option value="">请选择...</option>
				          <c:forEach items="${planStatusList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			          	  </c:forEach>
						</select>
		            </td>	           
		        </tr>
		        <tr>
		        	<td width="120" align="right">用料炉号:</td>
		            <td width="210" align="right">
		            	<input name="planLuno" id="planLunoM2001" class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,32]'" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">制造产品名称:</td>
		            <td width="210" align="right">
		            	<input name="makeName" id="makeNameM2001" class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,20]'" style="width:200px;"></input>
		            </td>
		        </tr>
		         <tr>
		            <td width="120" align="right">用料钢种:</td>
		            <td width="210" align="right">
		            	<select name="prodMaterial" id="prodMaterialM2001" class="easyui-combobox" panelHeight="auto" data-options="editable:true" style="width:200px;">
				          <option value="">请选择...</option>
				          <c:forEach items="${prodMaterialList}" var="a">
			          	   	<option value="${a.enumKey}">${a.enumValue}</option>
			         	 </c:forEach>
						</select>
		            </td>
		       		<td width="120" align="right">用料规格:</td>
		            <td width="210" align="right">
		            	<input name="prodNorm" class="easyui-textbox" type="text" data-options="required:false,validType:'length[0,40]'" style="width:200px;"></input>
		            </td>
		        </tr>
		        <tr> 
		        	<td width="120"  align="right">生产机组:</td>
		            <td width="210"  align="right">
		            	<select name="planJz" id="planJzM2001"  panelHeight="auto" class="easyui-combobox" data-options="editable:true" style="width:200px;">
				          <option value="">请选择...</option>
				          <c:forEach items="${prodUnitsZJList}" var="a">
				          	   	<option value="${a.enumKey}">${a.enumValue}</option>
				          	  </c:forEach>
						</select>
		            </td>
		        </tr>
		        <tr>
		            <td width="120" align="right">开始日期:</td>
		            <td width="210" align="right">
		            	<input name="startDate" id="startDateM2001" class="easyui-datebox" type="text" data-options="required:false" panelHeight="225px" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">结束日期:</td>
		            <td width="210" align="right">
		            	<input name="endDate" id="endDateM2001" class="easyui-datebox" type="text" data-options="required:false" panelHeight="225px" style="width:200px;"></input>
		            </td>
		        </tr>
		        <tr hidden="true">
		            <td width="120" align="right">页码:</td>
		            <td width="210" align="right">
		            	<input name="page" id="pageM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">每页记录数:</td>
		            <td width="210" align="right">
		            	<input name="rows" id="rowsM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		         </tr>
	       		 <tr hidden="true">
	            	 <td width="120" align="right">排序字段:</td>
		            <td width="210" align="right">
		            	<input name="sort" id="sortM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">排序规则:</td>
		            <td width="210" align="right">
		            	<input name="order" id="orderM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		         </tr>
	       		 <tr hidden="true">
		            <td width="120" align="right">类名:</td>
		            <td width="210" align="right">
		            	<input name="className" id="classNameM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		            <td width="120" align="right">方法名:</td>
		            <td width="210" align="right">
		            	<input name="methodName" id="methodNameM2001" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
		            </td>
		        </tr>		        
		    </table>
		</form>
		
		<div style="margin-top:10px" align="center">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFormM2001()">查询</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormM2001()">重置</a>
		</div>
	</div>
	 <table class="easyui-datagrid" id="planList" title="纵剪带生产指令管理"  style="height:620px"
	       data-options="singleSelect:true,rownumbers:true,remoteSort:false,onSortColumn:sortSerachM2001,collapsible:true,pagination:true,url:'/plan/query',method:'post',onBeforeLoad:onBeforeLoadM2001,pageSize:30,toolbar:toolbarM2001">
	    <thead>
	        <tr>
	        	<th data-options="field:'ck',checkbox:true"></th>
	        	<th data-options="field:'planId',width:130,sortable:true">纵剪带生产指令号</th>
	            <th data-options="field:'planStatus',width:100,formatter:BSL.formatPlanStatus,sortable:true">指令状态</th>
	            <th data-options="field:'planJz',width:100,sortable:true,formatter:BSL.formatPlanJz">生产机组</th>
	            <th data-options="field:'prodNorm',width:100,sortable:true">用料规格</th>
	        	<th data-options="field:'prodMaterial',width:100,formatter:BSL.formatProdMaterial,sortable:true">用料钢种</th>	
	        	<th data-options="field:'prodWeight',width:100,sortable:true">用料重量/吨</th>          
	        	<th data-options="field:'prodNum',width:100,sortable:true">用料数量/卷</th>    
	        	<th data-options="field:'makeName',width:120,sortable:true">制造产品名称</th> 
	        	<th data-options="field:'makeType',width:120,formatter:BSL.formatMakeType,sortable:true">制造纵剪带用途</th>    
	            <th data-options="field:'prodOrder',width:120,sortable:true">销售订单号</th>
	            <th data-options="field:'company',width:120,sortable:true">工厂</th>
	            <th data-options="field:'alreadyNum',width:120,sortable:true">已生产数量</th>          
	        	<th data-options="field:'alreadySum',width:120,sortable:true">已生产重量/吨</th>  
	        	<th data-options="field:'outNum',width:100,sortable:true">投料数量</th>  
	        	<th data-options="field:'outWeight',width:100,sortable:true">投料重量</th>                  
	        	<th data-options="field:'ccrate',width:100,sortable:true">成材率</th>  
	            <th data-options="field:'customer',width:200,sortable:true,formatter:BSL.formatCustomer">来料客户</th>
	            <th data-options="field:'planDepartment',width:100,sortable:true,formatter:BSL.formatMakeDept">生产部门</th>
	            <th data-options="field:'crtDate',width:120,formatter:BSL.formatDateTime,sortable:true">日期</th>
	            <th data-options="field:'inputuser',width:80,sortable:true">制单人</th>
	            <th data-options="field:'remark',width:200,sortable:true">备注</th>
	        </tr>
	    </thead>
	</table>
	<div id="planAddWindow" class="easyui-window" title="新增纵剪带生产指令" data-options="modal:true,closed:true,iconCls:'icon_add',href:'/plan/M2001-add'" style="width:780px;height:350px;padding:10px;">
	</div>
	<div id="planEditWindow" class="easyui-window" title="编辑纵剪带生产指令" data-options="modal:true,closed:true,iconCls:'icon-edit',href:'/plan/M2001-edit'" style="width:780px;height:350px;padding:10px;">
	</div>
	<div id="planInfoWindow" class="easyui-window" title="纵剪带生产指令详情" data-options="modal:true,closed:true,iconCls:'icon_add',href:'/plan/M2001-detail'" style="width:1120px;height:670px;padding:10px;">
	</div>
	<div id="planLunoInfoWindowM2001" class="easyui-window" title="用料炉号详情" data-options="modal:true,closed:true,iconCls:'icon_add',href:'/plan/M2001-planLunoDetail'" style="width:780px;height:700px;padding:10px;">
	</div>
</div>
<script>

	function onBeforeLoadM2001(){
		var queryParams = $('#planList').datagrid('options').queryParams;
		
		queryParams.planId = $('#planIdM2001').val();
		queryParams.planStatus = $('#planStatusM2001').combobox("getValue");
		queryParams.planLuno = $('#planLunoM2001').val();
		queryParams.makeName = $('#makeNameM2001').val();
		queryParams.prodMaterial = $('#prodMaterialM2001').combobox("getValue");
		queryParams.prodNorm = $('#prodNormM2001').val();
		queryParams.planJz = $('#planJzM2001').combobox("getValue");
		queryParams.startDate = $('#startDateM2001').datebox("getValue");
		queryParams.endDate = $('#endDateM2001').datebox("getValue");
	}

	//排序查询
	function sortSerachM2001(sort,order){
		$("#sortM2001").textbox('setValue',sort);
		$("#orderM2001").textbox('setValue',order);
		searchForm();
	}

	//查询按钮
	function searchFormM2001(){
		
		//page页码
		var page = $("#planList").datagrid('options').pageNumber;
		//rows每页记录条数  
        var rows = $("#planList").datagrid('options').pageSize; 
		$("#pageM2001").textbox('setValue',page);
		$("#rowsM2001").textbox('setValue',rows);
		//ajax的post方式提交表单
		//$("#receiptSearchForm").serialize()将表单序列号为key-value形式的字符串
		$.post("/plan/query",$("#planSearchForm").serialize(), function(data){
			if(data.status == 200){
				$("#classNameM2001").textbox('setValue',data.className);
				$("#methodNameM2001").textbox('setValue',data.methodName);
	            $('#planList').datagrid('loadData',  {"total":data.total,"rows":data.rows});
			} else {
				$.messager.alert('提示', data.msg);
			}
		});
		onBeforeLoadM2001();
	}
	
	/* 重置表单 */
	function clearFormM2001(){
		$('#planSearchForm').form('reset');
	}

    function getM2001SelectionsIds(){
    	var receiptList = $("#planList");
    	var sels = receiptList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].planId);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbarM2001 = [{
        text:'详情维护',
        iconCls:'icon-search',
        handler:function(){
        	var ids = getM2001SelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一条记录查看!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','一次最多查看一条记录!');
        		return ;
        	}
        	var dataG = $("#planList").datagrid("getSelections")[0];
  	    	var params = {"planId":ids};
          	$.post("/plan/detail",params, function(data){
      			if(data.status == 200){
      				$("#planInfoWindow").window({
      	        		onLoad :function(){
      	        			//回显数据
      	        			$("#planDetailFormM2001").form("load",dataG);
      	        			//回显详细
      	        			var values = [];  
	      	  				for (var i = 0; i < data.data.length; i++) {
	      	  	                var a = {
	      	  	                	'planInfoDetailId' : data.data[i].planInfoDetailId,
	      		                    'prodNorm' : data.data[i].prodNorm,
	      		                    'prodLevel' : data.data[i].prodLevel,
	      		                    'prodNum' : data.data[i].prodNum,
	      		                    'prodWeight' : data.data[i].prodWeight,
	      		                    'planOutputVolume' : data.data[i].planOutputVolume,
	      		                    'planFinistDate' : data.data[i].planFinistDate,
	      		                    'collectedUnits' : data.data[i].collectedUnits,
	      		                    'crtDate' : data.data[i].crtDate,
	      		                    'prodInputuser' : data.data[i].prodInputuser,
	      		                    'remark' : data.data[i].remark
	      	  	                };
	      	  	                values.push(a);				
	      	  	            }
	      	  	            $('#planDetailList').datagrid('loadData', values);
	      	  			}
      	        	}).window("open");
      			} else {
      				$.messager.alert('提示','详情查询失败：'+data.msg);
      			}
      		});
        }
    },{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	$("#planAddWindow").window({
        		onLoad :function(){
        			var ids = getM2001SelectionsIds();
        			if(ids.length > 0){
        				var data = $("#planList").datagrid("getSelections")[0];
            			data.priceView = BSL.formatPrice(data.price);
            			$("#planAddForm").form("load",data);
        			}
        		}
        	}).window("open");
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getM2001SelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一条记录编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一条记录编辑!');
        		return ;
        	}
        	
        	$("#planEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#planList").datagrid("getSelections")[0];
        			data.priceView = BSL.formatPrice(data.price);
        			$("#planEditForm").form("load",data);
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var data = $("#planList").datagrid("getSelections")[0];
        	var ids = getM2001SelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一条记录删除!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','一次最多删除一条记录!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除本条记录？',function(r){
        	    if (r){
        	    	var params = {"planId":ids};
                	$.post("/plan/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除成功!',undefined,function(){
            					searchFormM2001();
            				});
            			} else {
            				$.messager.alert('提示', data.msg);
            			}
            		});
        	    }
        	});
        }
    },{
        text:'用料炉号',
        iconCls:'icon-man',
        handler:function(){
        	var data = $("#planList").datagrid("getSelections")[0];
        	var ids = getM2001SelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一条记录查看用料炉号!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','一次最多查看一条记录!');
        		return ;
        	}
        	var params = {"planId":ids};
        	$.post("/planLuInfo/list",params, function(data){
				if(data.status == 200){
					$("#planLunoInfoWindowM2001").window({
		        		onLoad :function(){
				           	$("#planLunoInfoFormM2001").form("load",params);
		           			var values = [];  
		  	  				for (var i = 0; i < data.data.length; i++) {
		  	  	                var a = {
		  	  	                	'planId' : data.data[i].planId,
		 		                    'planLuno' : data.data[i].planLuno
		  	  	                };
		  	  	                values.push(a);				
		  	  	            }
		  	  	            $('#planLunoInfoListM2001').datagrid('loadData', values);
		        		}
		        	}).window("open");
				}else{
					$.messager.alert('提示',data.msg);
				}
			});
        }
    },
    {
        text:'PDF打印',
        iconCls:'icon-print',
        handler:function(){
        	var ids = getM2001SelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一条记录PDF打印!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一条记录PDF打印!');
        		return ;
        	}
        	$.messager.confirm('确认','是否需要PDF打印？',function(r){
				if (r){
        	    	var mapParam = new Map();
        			mapParam.set("url","/print/exportPdf");
        			mapParam.set("planId",ids);
        			mapParam.set("tradeType","M2001");
        			mapParam.set("loginUserId",$("#user_id").html());
        			BSL.toNewPagePDF(mapParam); 
        			//window.open("@Url.Action('/pdf')");
        	    }
			});
        }
    },{
        text : '导出EXCEL',
        iconCls : 'icon-save',
        handler : function() {
			//获取后台传递参数className methodName
			var className = $("#planList").datagrid("getData").className;
			var methodName = $("#planList").datagrid("getData").methodName;
			/**
			 * datagrid属性中获取不到className、methodName时，需要从隐藏域中获取；
			 * 主要是查询时，datagrid属性className、methodName会丢失
			 */
			if(className == "" || className==null || className == undefined){
				className = $('#classNameM2001').val();
			}
			if(methodName == "" || methodName==null || methodName == undefined){
				methodName = $('#methodNameM2001').val();
			}
			//获取表头信息
			var header = $("#planList").datagrid("options").columns[0];
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
			mapParam.set("url","/export/exportExcel");
			mapParam.set("fields",fields);
			mapParam.set("titles",titles);
			mapParam.set("className",className);
			mapParam.set("methodName",methodName);
			mapParam.set("excelName","纵剪带生产调度单制单信息");
			mapParam.set("loginUserId",$("#user_id").html());
			/* mapParam.set("rows","1");
			mapParam.set("page","5000"); */
			//查询条件 把所有查询条件带上
			mapParam.set("planId",$('#planIdM2001').val());
			mapParam.set("planStatus",$('#planStatusM2001').combobox("getValue"));
			mapParam.set("planLuno",$('#planLunoM2001').val());
			mapParam.set("makeName",$('#makeNameM2001').val());
			mapParam.set("prodMaterial",$('#prodMaterialM2001').combobox("getValue"));
			mapParam.set("prodNorm",$('#prodNormM2001').val());
			mapParam.set("planJz",$('#planJzM2001').combobox("getValue"));
			mapParam.set("startDate",$('#startDateM2001').datebox("getValue"));
			mapParam.set("endDate",$('#endDateM2001').datebox("getValue"));
			
			BSL.toExcel(mapParam);
        		}
    }];
    
</script>