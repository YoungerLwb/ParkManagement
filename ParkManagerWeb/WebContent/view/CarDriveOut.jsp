<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>车辆列表</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
	<script type="text/javascript">
	$(function() {	
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'车辆列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"CarDriveInAOutServlet?method=getCarDriveInList&t="+new Date().getTime(),
	        idField:'did', // id
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'did',// id
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'did',title:'ID',width:100,align:'center',sortable: true},    
 		        {field:'dplatenum',title:'车牌号',width:245,align:'center'},    
 		        {field:'dname',title:'车主名',width:266,align:'center'},
 		       {field:'driveintime',title:'驶入时间',width:317,align:'center',formatter:function(value,row,index){
               	if(typeof(value) == "object"){
               		var year = value.year + 1900;
               		
               		var month = (value.month + 1)>=10 ? (value.month + 1):"0"+(value.month + 1);
               		var day = value.date>=10 ? value.date:"0"+value.date;
               		var hours = value.hours>=10 ? value.hours:"0"+value.hours;
               		var minutes = value.minutes>=10 ? value.minutes:"0"+value.minutes;
               		var seconds = value.seconds>=10 ? value.seconds:"0"+value.seconds;
               		return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds
               	}
	        	}
	       
	       },
	       {field:'dparkid',title:'车位号',width:200,align:'center'},	         
	 		]], 
	        toolbar: "#toolbar"
	    }); 
	    //设置分页控件 
	    var p = $('#dataList').datagrid('getPager'); 
	    $(p).pagination({ 
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    
	    //修改
	    $("#add").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
		    	$("#addDialog").dialog("open");
            }
	    });
	  //下拉框通用属性
	  	$("#add_oprice").combobox({
	  		width: "200",
	  		height: "30",
	  		valueField: "price",
	  		textField: "price",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});


	$("#add_oprice").combobox({
	url: "CarPriceServlet?method=getCarPriceList&t="+new Date().getTime()+"&from=combox",
	onLoadSuccess: function(){
		
	}
});

/*
	  	$("#add_oprice").combobox({
	  		url: "ParkServlet?method=getParkList&t="+new Date().getTime()+"&from=combox",
	  		onLoadSuccess: function(){
		  		//默认选择第一条数据
				var data = $(this).combobox("getData");;
				$(this).combobox("setValue", data[0].parkid);
	  		}
	  	});
*/	  	

	    $("#addDialog").dialog({
	    	title: "添加车辆收费",
	    	width: 450,
	    	height: 500,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'提交',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							//var parkid = $("#add_dparkid").combobox("getValue");
							$.ajax({
								type: "post",
								url: "CarDriveInAOutServlet?method=AddCarDriveOut&t="+new Date().getTime(),
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","车辆出库成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										
										$("#add_dplatenum").textbox('setValue', "");
										$("#add_dname").textbox('setValue', "");
										
							  			$('#dataList').datagrid("reload");						  			
									}else if(msg == "nomoney"){
										$.messager.alert("消息提醒","卡内余额不足，请充值!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										
										$("#add_dplatenum").textbox('setValue', "");
										$("#add_dname").textbox('setValue', "");
										
							  			$('#dataList').datagrid("reload");

									} else{
										$.messager.alert("消息提醒","更新失败!","warning");
										return;
									}
								}
							});
						}
					}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						//清空表单
						$("#add_dplatenum").textbox('setValue', "");
						$("#add_dname").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){

				var selectRow = $("#dataList").datagrid("getSelected");
				$("#add_obalance").combobox({
					url: "CarUserServlet?method=getBalance&t="+new Date().getTime()+"&oname="+selectRow.dname,
					onLoadSuccess: function(){
			  		//默认选择第一条数据
					 var data = $(this).combobox("getData");
			  		
					$(this).combobox("setValue", data);
					
					}
				});
				
				var data = $("#add_oprice").combobox("getData");
				
				//设置值
				var year = selectRow.driveintime.year + 1900;
				var month = (selectRow.driveintime.month + 1)>=10 ? (selectRow.driveintime.month + 1):"0"+(selectRow.driveintime.month + 1);
               	var day = selectRow.driveintime.date>=10 ? selectRow.driveintime.date:"0"+selectRow.driveintime.date;
               	var hours = selectRow.driveintime.hours>=10 ? selectRow.driveintime.hours:"0"+selectRow.driveintime.hours;
               	var minutes = selectRow.driveintime.minutes>=10 ? selectRow.driveintime.minutes:"0"+selectRow.driveintime.minutes;
               	var seconds = selectRow.driveintime.seconds>=10 ? selectRow.driveintime.seconds:"0"+selectRow.driveintime.seconds;
				var time = month+"/"+day+"/"+year+" "+hours+":"+minutes+":"+seconds;
					
				var driveinTime = new Date(time);
				var driveouttime = $("#add_odriveouttime").datetimebox("getValue");
				var driveoutTime = new Date(driveouttime);
				var difftime = (driveoutTime - driveinTime)/1000;
				var days = parseInt(difftime/86400); 
			   	var hours = parseInt(difftime/3600)-24*days;    
			   	var minutes = parseInt(difftime%3600/60); 
			   	var seconds = parseInt(difftime%60);  
				var hours = parseInt(difftime/3600); 
				
				
				var ihours = parseInt(hours);
				if(ihours == 0){
					hours= "0";
				}
				if(ihours > 12){
					$("#add_oprice").combobox("setValue", data[0].price);
					var price = $("#add_oprice").combobox("getValue");
					
					charge = ihours*parseFloat(price);
				}else{
					$("#add_oprice").combobox("setValue", data[1].price);
					var price = $("#add_oprice").combobox("getValue");
					charge = ihours*parseFloat(price);
					
				}
				
				$("#add_oplatenum").textbox('setValue', selectRow.dplatenum);
				$("#add_oname").textbox('setValue', selectRow.dname);
				$("#add_odriveintime").textbox('setValue', time);
				$("#add_oparkid").textbox('setValue', selectRow.dparkid);
				$("#add_oparktime").textbox('setValue', hours);
				$("#add_ocharge").textbox('setValue', charge.toString());
				
			}
	    });
	 // 搜索按钮监听事件
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			carPlatenum: $('#carPlatenum').val(),
	  			carcname: $('#carcname').val()

	  		});
	  	});
	});
	
	</script>
</head>
<body>
<%
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = df.format(d);
%>
	<!-- 学生列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">选择驶出车辆</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="margin-top:3px;">&nbsp;&nbsp;车牌号：<input id="carPlatenum" class="easyui-textbox" name="carPlatenum" />车主姓名：<input id="carcname" class="easyui-textbox" name="carcname" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>	
	</div>
	<!-- 添加车辆驶入窗口 -->
	<div id="addDialog" style="padding: 10px">	 
    	<form id="addForm" method="post">
			<table cellpadding="8" >		
	    		<tr>
	    			<td>车牌号:</td>
	    			<td>
	    				<input id="add_oplatenum"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="oplatenum" readonly="readonly" />
	   				</td>
	    		</tr>
	    		
	    		<tr>
	    			<td>车主:</td>
	    			<td><input id="add_oname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="oname" readonly="readonly" /></td>
	    		</tr>
	    		<tr>
	    			<td>驶入时间:</td>
	    			<td><input id="add_odriveintime" style="width: 200px; height: 30px;" class="easyui-datetimebox" readonly="readonly" name="odriveintime" /></td>
	    		</tr>
	    		<tr>
	    			<td>驶出时间:</td>
	    			<td><input id="add_odriveouttime" style="width: 200px; height: 30px;" class="easyui-datetimebox" value="<%=now %>" readonly="readonly" name="odriveouttime" /></td>
	    		</tr>
	    		<tr>
	    			<td>停车时长:</td>
	    			<td><input id="add_oparktime" style="width: 200px; height: 30px;" class="easyui-textbox" readonly="readonly" name="oparktime" /></td>
	    		</tr>
	    		<tr>
	    			<td>车位号:</td>
	    			<td><input id="add_oparkid" style="width: 200px; height: 30px;" class="easyui-textbox" name="oparkid" /></td>
	    		</tr>
	    		<tr>
	    			<td>卡内余额:</td>
	    			<td><input id="add_obalance" style="width: 200px; height: 30px;" class="easyui-textbox" readonly="readonly" name="obalance" /></td>
	    		</tr>
	    		<tr>
	    			<td>费用:</td>
	    			<td><input id="add_ocharge" style="width: 200px; height: 30px;" class="easyui-textbox" readonly="readonly" name="ocharge" /></td>
	    		</tr>
	    		<tr>
	    			<td>单价(元/小时):</td>
	    			<td><input id="add_oprice" style="width: 200px; height: 30px;" class="easyui-textbox" name="oprice" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div> 
</body>
</html>