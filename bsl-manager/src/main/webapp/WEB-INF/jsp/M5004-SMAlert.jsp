<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<style>
	tr{height:30px}
</style>
<div style="padding:10px 10px 10px 10px">
	<table cellpadding="5">
     	 <tr>
        	<td width="150"  align="right">扫码倒计时:</td>
            <td width="210"  align="left">
            	<div id="showSMAlertM5004">600秒</div>
            </td>       
        </tr>
         <tr>
        	<td width="150"  align="right">产品编码:</td>
            <td width="210"  align="right">
            	<input name="prodId" id="prodIdSM5004" class="easyui-textbox" type="text" data-options="required:false,multiline:true" style="width:300px;height:400px;"></input>
            </td>  
             <td width="60"  align="left">
            	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeSMAlertM5004()">确认</a>
            </td>        
        </tr>
	 </table>
</div>
<script type="text/javascript">

	var startSec = 600;
	var timeM5004 = setInterval(function timeChange(){
		startSec = startSec - 1;
		//console.log(startSec);
		showSMAlertM5004.innerHTML = startSec + "秒"; 
		if(startSec<=0){
			clearInterval(timeM5004);
		}
	}, 1000);
	
	function closeSMAlertM5004(){
		$("#saleProdOutputSMAlertWindow").window('close');
	}
	
	
</script>
