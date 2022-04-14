<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>车库列表</title>
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
 		        {field:'dplatenum',title:'车牌号',width:240,align:'center'},    
 		        {field:'dname',title:'车主名',width:247,align:'center'},
 		       {field:'driveintime',title:'驶入时间',width:273,align:'center',formatter:function(value,row,index){
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
 		        {field:'dparkid',title:'车位号',width:267,align:'center'},	        
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

	<!-- 车辆驶入列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
		<div style="margin-top:3px;">&nbsp;&nbsp;车牌号：<input id="carPlatenum" class="easyui-textbox" name="carPlatenum" />车主姓名：<input id="carcname" class="easyui-textbox" name="carcname" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>	
	</div>
	
	
</body>
</html>