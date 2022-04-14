<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
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
	        title:'预约列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"AppointServlet?method=getAppointList&t="+new Date().getTime(),
	        idField:'aid', // id
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'aid',// id
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'aid',title:'ID',width:100,align:'center',sortable: true},    
 		        {field:'ausername',title:'用户名',width:146,align:'center'},    
 		        {field:'aname',title:'车主名',width:141,align:'center'},
 		       {field:'atel',title:'联系电话',width:165,align:'center'},		       
 		       {field:'appointtime',title:'预约时间',width:200,align:'center',formatter:function(value,row,index){
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
	       {field:'astatus',title:'审核状态',width:176,align:'center',formatter:function(value,row,index){
	    	   switch(row.astatus){
	    	   case 0:{
	    		   return '等待审核';
	    	   }
	    	   case 1:{
	    		   return '审核通过';
	    	   }
	    	   case -1:{
	    		   return '审核不通过';
	    	   }
	    	   }
	       }},
	       {field:'addtime',title:'添加时间',width:200,align:'center',formatter:function(value,row,index){
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
	    //修改
	    $("#edit-btn").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
		    	$("#editDialog").dialog("open");
            }
	    });
	    // 审核按钮监听事件
	    $("#check").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
            	if(selectRows[0].astatus != 0){
            		$.messager.alert("消息提醒", "该信息已审核，请勿重复审核!", "warning");
            		return ;
            	}
		    	$("#checkDialog").dialog("open");
            }
	    });
	    
	    //删除
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	
            	var aids = [];
            	$(selectRows).each(function(i, selectRows){
            		aids[i] = selectRows.aid;
            	});
            	$.messager.confirm("消息提醒", "将删除预约信息，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "AppointServlet?method=DeleteAppoint",
							data: {aids: aids},
							success: function(msg){
								if(msg == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
									$("#dataList").datagrid("uncheckAll");
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
	    
	    $("#addDialog").dialog({
	    	title: "添加预约信息",
	    	width: 350,
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
								url: "AppointServlet?method=AddAppoint",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										
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
	  	
	  	
	    $("#editDialog").dialog({
	    	title: "修改预约信息",
	    	width: 650,
	    	height: 460,
	    	iconCls: "icon-edit",
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
						var validate = $("#editForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							$.ajax({
								type: "post",
								url: "AppointServlet?method=EditAppoint&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","更新成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										
										$("#edit_ausername").textbox('setValue', "");
										$("#edit_aname").textbox('setValue', "");
										$("#edit_atel").textbox('setValue', "");
							  			$('#dataList').datagrid("reload");

										
										//$("#gradeList").combobox('setValue', gradeid);
							  			//setTimeout(function(){
										//	$("#clazzList").combobox('setValue', clazzid);
										//}, 100);
							  			
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
						$("#edit_ausername").textbox('setValue', "");
						$("#edit_aname").textbox('setValue', "");
						$("#edit_atel").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值				
				var year = selectRow.appointtime.year + 1900;
				var month = (selectRow.appointtime.month + 1)>=10 ? (selectRow.appointtime.month + 1):"0"+(selectRow.appointtime.month + 1);
               	var day = selectRow.appointtime.date>=10 ? selectRow.appointtime.date:"0"+selectRow.appointtime.date;
               	var hours = selectRow.appointtime.hours>=10 ? selectRow.appointtime.hours:"0"+selectRow.appointtime.hours;
               	var minutes = selectRow.appointtime.minutes>=10 ? selectRow.appointtime.minutes:"0"+selectRow.appointtime.minutes;
               	var seconds = selectRow.appointtime.seconds>=10 ? selectRow.appointtime.seconds:"0"+selectRow.appointtime.seconds;
				var time = month+"/"+day+"/"+year+" "+hours+":"+minutes+":"+seconds;
				$("#edit_ausername").textbox('setValue', selectRow.ausername);
				$("#edit_atel").textbox('setValue', selectRow.atel);
				$("#edit_aname").textbox('setValue', selectRow.aname);
				$("#edit_aid").val(selectRow.aid);
				$("#edit_appointtime").textbox('setValue', time);	
			}
	    });
	    
	    $("#checkDialog").dialog({
	    	title: "审核预约信息",
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
					text:'提交',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#checkForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							var aid = $("#dataList").datagrid("getSelected").aid;
							var status = $("#check_aname").combobox("getValue");
							var data = {aid:aid,status:status};
							$.ajax({
								type: "post",
								url: "AppointServlet?method=CheckAppoint&t="+new Date().getTime(),
								data: data,
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","审核成功!","info");
										//关闭窗口
										$("#checkDialog").dialog("close");
										
							  			$('#dataList').datagrid("reload");
							  			$('#dataList').datagrid("uncheckall");
							  			
									} else{
										$.messager.alert("消息提醒","审核失败!","warning");
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
						$("#check_aname").combobox('clear');
					}
				}
			],
			onBeforeOpen: function(){
				/*var selectRow = $("#dataList").datagrid("getSelected");
				$("#edit_caid").val(selectRow.aid);*/
			}
	    });
	 // 搜索按钮监听事件
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			a_username: $('#a_username').val(),
	  			caraname: $('#caraname').val()

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
		<c:if test="${userType == 2}">	 
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
		
		<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
		</c:if>
		<c:if test="${userType == 1}">	 
		
		<div style="float: left;"><a id="check" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">审核</a></div>
		</c:if>
		<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;" class="datagrid-btn-separator"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>		
		
		<div style="margin-top:3px;">&nbsp;&nbsp;用户名：<input id="a_username" class="easyui-textbox" name="a_username" />车主姓名：<input id="caraname" class="easyui-textbox" name="caraname" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>	
		
		
		
	</div>
	<c:if test="${userType == 2}">	
	<!-- 添加预约窗口 -->
	<div id="addDialog" style="padding: 10px">  		
	   <form id="addForm" method="post">
	    	<table cellpadding="8" >	    		
	    		<tr>
	    			<td>用户名:</td>
	    			<td>
	    				<input id="add_ausername"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="ausername" value="${user.username}" readonly="readonly" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>车主名:</td>
	    			<td><input id="add_aname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="aname" value="${user.uname}" readonly="readonly" /> </td>
	    		</tr>
	    		<tr>
	    			<td>电话</td>
	    			<td>
					<input id="add_atel" style="width: 200px; height: 30px;" class="easyui-textbox"  name="atel"value="${user.utel}" readonly="readonly" /> </td>
	    		</tr>
	    		<tr>
	    			<td>预约时间:</td>
	    			<td><input id="add_appointtime" style="width: 200px; height: 30px;" class="easyui-datetimebox" name="appointtime" /></td>
	    		</tr>
	    		<tr>
	    			<td>添加时间:</td>
	    			<td><input id="add_addtime" style="width: 200px; height: 30px;" class="easyui-datetimebox" value="<%=now %>" readonly="readonly" name="addtime" /></td>
	    		</tr>	
	    	</table>
	    </form>
	</div>
	</c:if>
	<!-- 修改学生窗口 -->
	<div id="editDialog" style="padding: 10px">  
    	<form id="editForm" method="post">
			<input type="hidden" id="edit_aid" name="eaid"  />	    
			<table cellpadding="8" >		
	    		<tr>
	    			<td>用户名:</td>
	    			<td>
	    				<input id="edit_ausername"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="ausername" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>车主名:</td>
	    			<td><input id="edit_aname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="aname" /></td>
	    		</tr>
	    		<tr>
	    			<td>电话</td>
	    			<td>
					<input id="edit_atel" style="width: 200px; height: 30px;" class="easyui-textbox"  name="atel" /></td>
	    		</tr>
	    		<tr>
	    			<td>预约时间:</td>
	    			<td><input id="edit_appointtime" style="width: 200px; height: 30px;" class="easyui-datetimebox" name="appointtime" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
		<!-- 审核窗口 -->
	<div id="checkDialog" style="padding: 10px">  
    	<form id="checkForm" method="post">
			<input type="hidden" id="edit_caid" name="caid"  />	    
			<table cellpadding="8" >		
	    		<tr>
	    			<td>车主:</td>
	    			<td colspan="3">
	    				<select id="check_aname" class="easyui-combobox" name="status" style="width: 200px; height: 30px;" data-options="required:true, missingMessage:'请选择状态'" >
	    					<option value="1">审核通过</option>
	    					<option value="-1">审核不通过</option>
	    				</select>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	</div>	 

</body>
</html>