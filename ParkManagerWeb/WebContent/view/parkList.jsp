<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>车位列表</title>
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
	        title:'车位列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible: false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"ParkServlet?method=getParkList&t="+new Date().getTime(),
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
 		        {field:'parkid',title:'车位号',width:285,align:'center'},
 		       {field:'status',title:'状态',width:321,align:'center'},
 		        {field:'area',title:'所属区域',width:373,align:'center'
 		    	/*
 		    	   formatter: function(value,row,index){
						if (row.grade){
							return row.grade.name;
						} else {
							return value;
						}
					}*/
 		        },
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
	    
	  	
	  	//设置添加车位窗口
	    $("#addDialog").dialog({
	    	title: "添加车位",
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
							//var gradeid = $("#add_gradeList").combobox("getValue");
							$.ajax({
								type: "post",
								url: "ParkServlet?method=AddPark",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#add_parkid").textbox('setValue', "");
										$("#status").textbox('setValue', "");
										$("#area").textbox('setValue', "A区");
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
						$("#add_parkid").textbox('setValue', "");
						//重新加载年级
						$("#status").textbox('setValue', "");
						$("#area").textbox('setValue', "A区");
					}
				},
			]
	    });
	  	
	  	// 搜索按钮监听事件，将parkId传值给parkServlet
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			parkId: $('#parkId').val()
	  		});
	  	});
	  	//修改按钮监听事件
	  	$("#edit-btn").click(function(){
	  		var selectRow = $("#dataList").datagrid("getSelected");
        	if(selectRow == null){
            	$.messager.alert("消息提醒", "请选择数据进行修改!", "warning");
            	return;
            }
        	$("#editDialog").dialog("open");
	  	});
	  //设置编辑车位窗口
	    $("#editDialog").dialog({
	    	title: "编辑车位",
	    	width: 350,
	    	height: 250,
	    	iconCls: "icon-edit",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'确定修改',
					plain: true,
					iconCls:'icon-edit',
					handler:function(){
						var validate = $("#editForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							//var gradeid = $("#add_gradeList").combobox("getValue");
							$.ajax({
								type: "post",
								url: "ParkServlet?method=EditPark",
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","修改成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										//清空原表格数据
										$("#edit_parkid").textbox('setValue', "");
										$("#edit_status").textbox('setValue', "");
										$("#edit_area").textbox('setValue',"A区");
										
										//重新刷新页面数据
							  			//$('#gradeList').combobox("setValue", gradeid);
							  			$('#dataList').datagrid("reload");
										
									} else{
										$.messager.alert("消息提醒","修改失败!","warning");
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
						$("#edit_parkid").textbox('setValue', "");
						//重新加载车位
						$("#edit_status").textbox('setValue', "");
						$("#edit_area").textbox('setValue',"");
					}
				},
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				console.log(selectRow);
				//设置值
				$("#edit_parkid").textbox('setValue', selectRow.parkid);
				$("#edit_status").textbox('setValue', selectRow.status);
				$("#edit_area").textbox('setValue',selectRow.area);
				 $("#edit_id").val(selectRow.id);
				
			}
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
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
		
		<!--  	
			<div style="float: left;" class="datagrid-btn-separator"></div>
		-->
		<div style="float: left; margin-right: 10px;"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>
		
	 	<div style="float: left; margin-right: 10px;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
	 	</c:if>
		<div style="margin-top:3px;">车位号：<input id="parkId" class="easyui-textbox" name="parkId" />
			<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>
		
	
	</div>
	
	<!-- 添加窗口 -->
	<div id="addDialog" style="padding: 10px">  
    	<form id="addForm" method="post">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>车位号:</td>
	    			<td><input id="add_parkid" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="parkid" validType="number" data-options="required:true, missingMessage:'不能为空'" /></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td><input id="status" name="status" value="空闲" style="width: 200px; height: 30px;" class="easyui-textbox" type="text"></td>
	    		</tr>
	    		<tr>
	    			<td>所属区域:</td>
	    			<!--  
	    			<td><input id="area" name="area" style="width: 200px; height: 30px;" class="easyui-textbox" type="text"></td>
	    			-->
	    			<td><select id="area" class="easyui-combobox" data-options="editable: false, panelHeight: 50, width: 60, height: 30" name="area"><option value="A区">A区</option><option value="B区">B区</option><option value="C区">C区</option></select></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 编辑窗口 -->
	<div id="editDialog" style="padding: 10px">  
    	<form id="editForm" method="post">
    		<input type="hidden" id="edit_id" name="id">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>车位号:</td>
	    			<td><input id="edit_parkid" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="parkid" validType="number" data-options="required:true, missingMessage:'不能为空'" /></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td><input id="edit_status" name="status" value="空闲" style="width: 200px; height: 30px;" class="easyui-textbox" type="text"></td>
	    		</tr>
	    		<tr>
	    			<td>所属区域:</td>
	    			<td><select id="edit_area" class="easyui-combobox" data-options="editable: false, panelHeight: 50, width: 60, height: 30" name="area"><option value="A区">A区</option><option value="B区">B区</option><option value="C区">C区</option></select></td>
	    			
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>