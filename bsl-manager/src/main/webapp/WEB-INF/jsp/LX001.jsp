<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="easyui-panel" title="数字字典查询" style="padding:10px 10px 10px 10px">
	<form id="enumSearchForm" class="enumForm">
		<table>
			<tr>
				<td width="120" align="right">数字字典枚举项：</td>
				<td width="210" align="right">
					<select id="enumSelectList" name="enumSelect"  panelHeight="auto" class="easyui-combobox" data-options="editable:true" style="width:200px;">
						<option value="">请选择...</option>
						<c:forEach items="${enumSelectList}" var="a">
							<option value="${a.enumEnglishName}">${a.enumEnglishName}-${a.enumChineseName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr hidden="true">
				<td width="120" align="right">页码:</td>
				<td width="210" align="right">
					<input name="page" id="pageLX000" class="easyui-textbox" type="text" data-options="required:false" panelHeight="225px"  style="width:200px;"></input>
				</td>
				<td width="120" align="right">每页记录数:</td>
				<td width="210" align="right">
					<input name="rows" id="rowsLX000" class="easyui-textbox" type="text" data-options="required:false" panelHeight="225px"  style="width:200px;"></input>
				</td>
			</tr>
			<tr hidden="true">
				<td width="120" align="right">排序字段:</td>
				<td width="210" align="right">
					<input name="sort" id="sortLX000" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
				</td>
				<td width="120" align="right">排序规则:</td>
				<td width="210" align="right">
					<input name="order" id="orderLX000" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
				</td>
			</tr>
			<tr hidden="true">
				<td width="120" align="right">类名:</td>
				<td width="210" align="right">
					<input name="className" id="classNameLX000" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
				</td>
				<td width="120" align="right">方法名:</td>
				<td width="210" align="right">
					<input name="methodName" id="methodNameLX000" class="easyui-textbox" type="text" data-options="required:false" style="width:200px;"></input>
				</td>
			</tr>
		</table>
	</form>

	<div style="margin-top:10px" align="center">
		<a href="javascript:void(0)" class="easyui-linkbutton" id="searchFormLX000" onclick="searchFormLX000()">查询</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLX000()">重置</a>
	</div>
</div>
<table class="easyui-datagrid" id="enumList" title="数字字典管理" style="height:600px"
	   data-options="singleSelect:true,rownumbers:true,fitColumns:true,remoteSort:false,onSortColumn:sortSerachLX000,collapsible:true,pagination:true,url:'/enum/query',method:'post',onBeforeLoad:onBeforeLoadLX000,pageSize:10,toolbar:toolbarLX000">
	<thead>
	<tr>
		<th data-options="field:'ck',checkbox:true"></th>
		<th data-options="field:'enumEnglishName',width:60,sortable:true">枚举英文名</th>
		<th data-options="field:'enumChineseName',width:60">枚举中文名</th>
		<th data-options="field:'enumKey',width:60,sortable:true">枚举</th>
		<th data-options="field:'enumValue',width:60">枚举值</th>
		<th data-options="field:'enumOrder',width:60">枚举展示顺序</th>
	</tr>
	</thead>
</table>
<div id="enumAddWindow" class="easyui-window" title="新增枚举项" data-options="modal:true,closed:true,iconCls:'icon-add',href:'/enum/LX001-add'" style="height:170px;width:780px" >
</div>
<div id="enumEditWindow" class="easyui-window" title="编辑枚举项" data-options="modal:true,closed:true,iconCls:'icon-edit',href:'/enum/LX001-edit'" style="height:210px;width:780px">
</div>
<div id="enumDelWindow" class="easyui-window" title="删除枚举项" data-options="modal:true,closed:true,iconCls:'icon-add',href:'/enum/LX001-del'" style="height:170px;width:780px" >
</div>
<script>
	// $(function(){
	// 	$.post("/enum/init", null, function(data){
	// 		if(data.status == 200){
	// 			$("#classNameLX000").textbox('setValue',data.className);
	// 			$("#methodNameLX000").textbox('setValue',data.methodName);
	// 			$('#enumSelectList').datagrid('loadData', data.rows);
	// 			// $('#enumList').datagrid('loadData', {"total":data.total,"rows":data.rows});
	// 		}else{
	// 			$.messager.alert('提示',data.msg);
	// 		}
	// 	});
	// });
	function onBeforeLoadLX000(){
		var queryParams = $('#enumList').datagrid('options').queryParams;
		queryParams.enumId = $('#enumIdLX000').val();
		queryParams.enumName = $('#enumNameLX000').val();
	}

	//排序查询
	function sortSerachLX000(sort,order){
		$("#sortLX000").textbox('setValue',sort);
		$("#orderLX000").textbox('setValue',order);
		searchFormLX000();
	}

	//提交表单
	function searchFormLX000(){
		// $('#searchFormLX000').linkbutton('disable');
		if ($("#enumSelectList").combobox('getValue') == undefined || $("#enumSelectList").combobox('getValue') == "") {
			$.messager.alert('提示','请选择数字字典枚举项!');
			return;
		}
		//page页码
		var page = $("#enumList").datagrid('options').pageNumber;
		//rows每页记录条数
		var rows = $("#enumList").datagrid('options').pageSize;
		$("#pageLX000").textbox('setValue',page);
		$("#rowsLX000").textbox('setValue',rows);
		//ajax的post方式提交表单
		$.post("/enum/query/" + $("#enumSelectList").combobox('getValue'), $("#enumSearchForm").serialize(), function(data){
			if(data.status == 200){
				$("#classNameLX000").textbox('setValue',data.className);
				$("#methodNameLX000").textbox('setValue',data.methodName);
				$('#enumList').datagrid('loadData', {"total":data.total,"rows":data.rows});
			}else{
				$.messager.alert('提示',data.msg);
			}
			$('#searchFormLX000').linkbutton('enable');
		});
		onBeforeLoadLX000();
	}

	function clearFormLX000(){
		$('#enumSearchForm').form('reset');
	}


	function getLX000SelectionsIds(){
		var enumList = $("#enumList");
		var sels = enumList.datagrid("getSelections");
		var ids = [];
		for(var i in sels){
			ids.push(sels[i].enumId);
		}
		ids = ids.join(",");
		return ids;
	}

	var toolbarLX000 = [{
		text:'新增',
		iconCls:'icon-add',
		handler:function(){
			$("#enumAddWindow").window({
				onLoad :function(){
					$("#enumNameLX000Add").textbox('textbox').focus();
				}
			}).window("open");
		}
	},{
		text:'编辑',
		iconCls:'icon-edit',
		handler:function(){
			var ids = getLX000SelectionsIds();
			if(ids.length == 0){
				$.messager.alert('提示','必须选择一个员工才能编辑!');
				return ;
			}
			if(ids.indexOf(',') > 0){
				$.messager.alert('提示','只能选择一个员工!');
				return ;
			}

			$("#enumEditWindow").window({
				onLoad :function(){
					$("#enumNameLX000Edit").textbox('textbox').focus();
					//回显数据
					var data = $("#enumList").datagrid("getSelections")[0];
					data.priceView = BSL.formatPrice(data.price);
					$("#enumEditForm").form("load",data);
				}
			}).window("open");
		}
	},{
		text:'删除',
		iconCls:'icon-cancel',
		handler:function(){
			var ids = getLX000SelectionsIds();
			if(ids.length == 0){
				$.messager.alert('提示','未选中员工!');
				return ;
			}
			if(ids.indexOf(',') > 0){
				$.messager.alert('提示','只能一次删除一个员工!');
				return ;
			}
			if($("#enum_id").html() == ids){
				$.messager.alert('提示','不能删除自己！');
				return ;
			}

			$.messager.confirm('确认','确定删除ID为 '+ids+' 的员工吗？',function(r){
				if (r){
					var params = {"enumId":ids};
					$.post("/enum/delete",params, function(data){
						if(data.status == 200){
							$.messager.alert('提示','删除员工成功!',undefined,function(){
								$("#enumList").datagrid("reload");
							});
						} else {
							$.messager.alert('提示',data.msg);
						}
					});
				}
			});
		}
	},{
		text:'枚举关联模块',
		iconCls:'icon-ok',
		handler:function(){
			var ids = getLX000SelectionsIds();
			if(ids.length == 0){
				$.messager.alert('提示','必须选择一个员工才能配置角色!');
				return ;
			}
			if(ids.indexOf(',') > 0){
				$.messager.alert('提示','只能选择一个员工配置角色!');
				return ;
			}

			$("#enumDelWindow").window({
				onLoad :function(){
					//回显数据
					var data = $("#enumList").datagrid("getSelections")[0];
					data.priceView = BSL.formatPrice(data.price);
					$("#enumTypeAddForm").form("load",data);
				}
			}).window("open");
		}
	},{
		text : '导出EXCEL',
		iconCls : 'icon-save',
		handler : function() {
			//获取后台传递参数className methodName
			var className = $("#enumList").datagrid("getData").className;
			var methodName = $("#enumList").datagrid("getData").methodName;
			if(className == "" || className==null || className == undefined){
				className = $('#classNameLX000').val();
			}
			if(methodName == "" || methodName==null || methodName == undefined){
				methodName = $('#methodNameLX000').val();
			}
			//获取表头信息
			var header = $("#enumList").datagrid("options").columns[0];
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
			mapParam.set("url","/export/exportExcel");
			mapParam.set("fields",fields);
			mapParam.set("titles",titles);
			mapParam.set("className",className);
			mapParam.set("methodName",methodName);
			mapParam.set("excelName","人员信息");
			mapParam.set("loginenumId",$("#enum_id").html());
			mapParam.set("enumId",$('#enumIdLX000').val());
			mapParam.set("enumName",$('#enumNameLX000').val());

			BSL.toExcel(mapParam);
		}
	}];
</script>