<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<style>
	tr{height:30px}
</style>
<div style="padding:10px 10px 10px 10px">
	<form id="enumAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td width="120"  align="right">枚举英文名:</td>
	            <td width="210"  align="right">
	            	<input class="easyui-textbox" type="text" name="enumName" id="enumNameLX001Add" data-options="required:true" style="width: 200px;"></input>
	            </td>
	             <td width="120"  align="right">枚举:</td>
	            <td width="210"  align="right"><input type="text" class="easyui-textbox textbox" id="num_phone_add" name="enumTel"  data-options="prompt:'请输入正确的手机号码' , validType:'telNum'" style="width:200px;"></td>
	        </tr>
	        <tr>
	            <td width="120" align="right">备注:</td>
	           <td width="210"  align="right"><input class="easyui-textbox" name="remark" data-options="required:false" style="text; width: 200px;"></input></td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px" align="center">
	    <a href="javascript:void(0)" class="easyui-linkbutton" id="LX001Add" onclick="submitFormLX001Add()">提   交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormLX001Add()">重   置</a>
	</div>
</div>
<script type="text/javascript">
    //做电话正则:如果说输入的电话号码不正确则给出提示,正确则不显示提示(添加，修改)
    $.extend($.fn.validatebox.defaults.rules, {
        telNum:{ //既验证手机号，又验证座机号
            validator: function(value, param){
                return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[3456789]\d{9})$)/.test(value);
            },
            message: '请输入正确的电话号码'
        }
    });
	
	//提交表单
	function submitFormLX001Add(){
		$('#LX001Add').linkbutton('disable');
		//有效性验证
		if(!$('#enumAddForm').form('validate')){
			$('#LX001Add').linkbutton('enable');
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}
		//ajax的post方式提交表单
		//$("#itemAddForm").serialize()将表单序列号为key-value形式的字符串
		$.post("/enum/save",$("#enumAddForm").serialize(), function(data){
			if(data.status == 200){
				$.messager.alert('提示','新增员工成功!员工编号为：'+data.data+'。请同步配置人员角色。','info',function(){
					$("#enumAddWindow").window('close');
					searchFormLX001();
				});
			} else {
				$.messager.alert('提示', data.msg);
			}
			$('#LX001Add').linkbutton('enable');
		});
	}
	
	function clearFormLX001Add(){
		$('#enumAddForm').form('reset');
	}
</script>
