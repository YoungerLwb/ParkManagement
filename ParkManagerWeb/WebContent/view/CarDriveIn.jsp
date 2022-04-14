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
	        url:"CarInfoServlet?method=getCarInfoList&t="+new Date().getTime(),
	        idField:'cid', // id
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'cid',// id
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'cid',title:'ID',width:100,align:'center',sortable: true},    
 		        {field:'cplatenum',title:'车牌号',width:268,align:'center'},    
 		        {field:'cname',title:'车主名',width:261,align:'center'},
 		        {field:'cbrand',title:'车辆品牌',width:292,align:'center'},	        
 		       {field:'cphoto',title:'车辆照片',width:208,align:'center',formatter:formatState},
	 		]], 
	        toolbar: "#toolbar"
	    }); 
	function formatState(value,row,index){
		if(value){
			return "<img style='height:50px;width:207px' src='PhotoServlet?method=getPhoto&cid="+row.cid+"' />";
		}
	}
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
	  	$("#add_dparkid").combobox({
	  		width: "200",
	  		height: "30",
	  		valueField: "parkid",
	  		textField: "parkid",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});
	  	$("#add_dparkid").combobox({
	  		url: "ParkServlet?method=getParkList&t="+new Date().getTime()+"&from=combox",
	  		onLoadSuccess: function(){
		  		//默认选择第一条数据
				var data = $(this).combobox("getData");;
				$(this).combobox("setValue", data[0].parkid);
	  		}
	  	});
	  	//添加车辆驶入窗口
	    $("#addDialog").dialog({
	    	title: "添加车辆驶入信息",
	    	width: 350,
	    	height: 300,
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
							var parkid = $("#add_dparkid").combobox("getValue");
							$.ajax({
								type: "post",
								url: "CarDriveInAOutServlet?method=AddCarDriveIn&t="+new Date().getTime(),
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","车辆入库成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										
										$("#add_dplatenum").textbox('setValue', "");
										$("#add_dname").textbox('setValue', "");
										
							  			$('#dataList').datagrid("reload");

										
										//$("#gradeList").combobox('setValue', gradeid);
							  			//setTimeout(function(){
										//	$("#clazzList").combobox('setValue', clazzid);
										//}, 100);
							  			
									} else{
										$.messager.alert("消息提醒","该车以停放或停车位已被占!","warning");
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
				//设置值
				var parkid = $("#add_dparkid").textbox("getValue");
				
				$("#add_dplatenum").textbox('setValue', selectRow.cplatenum);
				$("#add_dname").textbox('setValue', selectRow.cname);

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
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">选择驶入车辆</a></div>
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
	    				<input id="add_dplatenum"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="dplatenum" readonly="readonly" />
	   				</td>
	    		</tr>
	    		
	    		<tr>
	    			<td>车主:</td>
	    			<td><input id="add_dname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="dname" readonly="readonly" /></td>
	    		</tr>
	    		<tr>
	    			<td>驶入时间:</td>
	    			<td><input id="add_driveintime" style="width: 200px; height: 30px;" class="easyui-datetimebox" value="<%=now %>" name="driveintime" /></td>
	    		</tr>
	    		<tr>
	    			<td>车位号:</td>
	    			<td><input id="add_dparkid" style="width: 200px; height: 30px;" class="easyui-textbox" name="dparkid" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div> 
</body>
</html>