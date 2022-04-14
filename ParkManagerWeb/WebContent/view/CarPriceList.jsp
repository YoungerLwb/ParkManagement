<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>停车收费价格设置</title>
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
	        title:'停车收费价格列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible: false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"CarPriceServlet?method=getCarPriceList&t="+new Date().getTime(),
	        idField:'pid', 
	        singleSelect: true,//是否单选 
	        pagination: true,//分页控件 
	        rownumbers: true,//行号 
	        sortName: 'pid',
	        sortOrder: 'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'pid',title:'ID',width:209,align:'center',sortable: true},    
 		        {field:'price',title:'价格',width:428,align:'center'},
 		       {field:'paddtime',title:'添加时间',width:490,align:'center',formatter:function(value,row,index){
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
            	var pid = selectRow.pid;
            	$.messager.confirm("消息提醒", "将删除该价格），确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "CarPriceServlet?method=DeleteCarPrice",
							data: {pid: pid},
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

	  	//设置添加价格窗口
	    $("#addDialog").dialog({
	    	title: "添加车位",
	    	width: 350,
	    	height: 200,
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
								url: "CarPriceServlet?method=AddCarPrice",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#add_price").textbox('setValue', "");
										//重新刷新页面数据
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
						$("#add_price").textbox('setValue', "");
					
					}
				},
			]
	    });
	  	
	  	
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			price: $('#price').val()
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
	  //设置编辑价格窗口
	    $("#editDialog").dialog({
	    	title: "编辑价格",
	    	width: 350,
	    	height: 150,
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
								url: "CarPriceServlet?method=EditCarPrice",
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","修改成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										//清空原表格数据
										$("#edit_price").textbox('setValue', "");
										
										
							
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
						$("#edit_price").textbox('setValue', "");
						
					}
				},
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				console.log(selectRow);
				//设置值
				$("#edit_price").textbox('setValue', selectRow.price);
				 $("#edit_id").val(selectRow.pid);
				
			}
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
	<!-- 数据列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	</table> 
	
	<!-- 工具栏 -->
	<div id="toolbar">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加价格</a></div>
		<!--  	
			<div style="float: left;" class="datagrid-btn-separator"></div>
		-->
		<div style="float: left; margin-right: 10px;"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>
		<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;" class="datagrid-btn-separator"></div>
	 	<div style="float: left; margin-right: 10px;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
	 	
		<div style="margin-top:3px;">价格：<input id="price" class="easyui-textbox" name="price" />
			<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>
		
	
	</div>
	
	<!-- 添加窗口 -->
	<div id="addDialog" style="padding: 10px">  
    	<form id="addForm" method="post">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>价格:</td>
	    			<td><input id="add_price" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="price" data-options="required:true, missingMessage:'不能为空'" /></td>
	    		</tr>
	    		<tr>
	    			<td>添加时间:</td>
	    			<td><input id="add_addtime" name="addtime" value="<%=now %>" style="width: 200px; height: 30px;" class="easyui-datetimebox"></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 编辑窗口 -->
	<div id="editDialog" style="padding: 10px">  
    	<form id="editForm" method="post">
    		<input type="hidden" id="edit_id" name="pid">
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>修改价格:</td>
	    			<td><input id="edit_price" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="price" data-options="required:true, missingMessage:'不能为空'" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>