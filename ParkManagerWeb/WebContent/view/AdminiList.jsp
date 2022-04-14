<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>管理员列表</title>
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
	        title:'管理员列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible: false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"SystemServlet?method=getAdminiList&t="+new Date().getTime(),
	        idField:'id', 
	        singleSelect: true,//是否单选 
	        pagination: true,//分页控件 
	        rownumbers: true,//行号 
	        sortName: 'id',
	        sortOrder: 'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'id',title:'ID',width:150,align:'center',sortable: true},    
 		        {field:'username',title:'用户名',width:476,align:'center'},
 		       {field:'status',title:'权限',width:503,align:'center',formatter:function(value,row,index){
 		    	   switch(row.status){
 		    	   case 1:{
 		    		   return '管理员';
 		    	   }
 		    	   }
 		       }},
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
	    //设置工具类按钮
	    $("#add").click(function(){
	    	$("#addDialog").dialog("open");
	    });
	    //删除
	    $("#delete").click(function(){
	    	var selectRow = $("#dataList").datagrid("getSelected");
	    	
        	if(selectRow == null){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var parkid = selectRow.id;//此处parkid其实是id
            	$.messager.confirm("消息提醒", "将删除车位信息（如果存在车辆则不能删除），确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "ParkServlet?method=DeletePark",
							data: {parkid: parkid},
							success: function(msg){
								if(msg == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
								} else{
									$.messager.alert("消息提醒","删除失败!","warning");
									return;
								}
							}
						});
            		}
            	});
            }
	    });
	  	//设置添加管理员窗口
	    $("#addDialog").dialog({
	    	title: "添加管理员",
	    	width: 350,
	    	height: 250,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'添加',
					plain: true,
					iconCls:'icon-add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							$.ajax({
								type: "post",
								url: "SystemServlet?method=AddAdmini",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#username").textbox('setValue', "");
										$("#password").textbox('setValue', "");
										//重新刷新页面数据
							  			//$('#gradeList').combobox("setValue", gradeid);
							  			$('#dataList').datagrid("reload");
										
									} else{
										$.messager.alert("消息提醒","添加失败!","warning");
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
						$("#username").textbox('setValue', "");
						//重新加载年级
						$("#password").textbox('setValue', "");						
					}
				},
			]
	    });
	  	
	  	// 搜索按钮监听事件，将exname传值给systemServlet
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			exname: $('#exname').val()
	  		});
	  	});
	});
	</script>
</head>
<body>
	<!-- 数据列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	</table> 
	
	<!-- 工具栏 -->
	<div id="toolbar">
		
		<c:if test="${userType == 1}">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加管理员</a></div>
		<div style="float: left;" class="datagrid-btn-separator"></div>
<!--  		
	<div style="float: left; margin-right: 10px;"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>
-->
	 	</c:if>
		<div style="margin-top:2px;">用户名：<input id="exname" class="easyui-textbox" name="exname" />
			<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>
		
	
	</div>
	
	<!-- 添加窗口 -->
	<div id="addDialog" style="padding: 10px">  
    	<form id="addForm" method="post">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>用户名:</td>
	    			<td><input id="username" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="username" data-options="required:true, missingMessage:'不能为空'" /></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="password" name="password"  style="width: 200px; height: 30px;" class="easyui-textbox" type="password"></td>
	    		</tr>
	    		<tr>
	    			<td>权限:</td>
	    			<td><select id="status" class="easyui-combobox" data-options="editable: false, panelHeight: 50, width: 70, height: 30" name="status" readonly="readonly"><option value="1">管理员</option></select></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>