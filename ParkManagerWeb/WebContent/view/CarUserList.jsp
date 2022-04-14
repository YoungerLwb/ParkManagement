<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>车主列表</title>
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
	        title:'车主列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"CarUserServlet?method=getCarUserList&t="+new Date().getTime(),
	        idField:'uid', // id
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'uid',// id
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
                {field:'uid',title:'用户序号',width:74,align:'center',sortable: true}, 
 		        {field:'username',title:'用户名',width:126,align:'center'},    
 <c:if test="${userType == 1}">     {field:'upwd',title:'密码',width:136,align:'center'},</c:if>
 		        {field:'uicd',title:'IC卡号',width:129,align:'center'},
 		        {field:'uname',title:'姓名',width:102,align:'center'},
 <c:if test="${userType == 1}"> 	{field:'usex',title:'性别',width:92,align:'center'},
 		        {field:'utel',title:'电话',width:179,align:'center'},
 		        {field:'ubalance',title:'卡内余额',width:81,align:'center'},
 </c:if>
 		        
<c:if test="${userType == 2}"> 	{field:'usex',title:'性别',width:118,align:'center'},
		        {field:'utel',title:'电话',width:239,align:'center'},
		        {field:'ubalance',title:'卡内余额',width:128,align:'center'},	
</c:if>		        
 		        {field:'uphoto',title:'照片',width:211,align:'center',formatter:formatState}
	 		]], 
	        toolbar: "#toolbar"
	    }); 
	function formatState(value,row,index){
		if(value){
			return "<img style='height:50px;width:210px;' src='PhotoServlet?method=getPhoto&uid="+row.uid+"' />";
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
	    //删除
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var uicds = [];
            	$(selectRows).each(function(i, selectRows){
            		uicds[i] = selectRows.uicd;
            	});
            	var uids = [];
            	$(selectRows).each(function(i, selectRows){
            		uids[i] = selectRows.uid;
            	});
            	$.messager.confirm("消息提醒", "将删除与车主相关的所有数据，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "CarUserServlet?method=DeleteCarUser",
							data: {uicds: uicds, uids: uids},
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
	  	//设置添加车主窗口
	    $("#addDialog").dialog({
	    	title: "添加车主",
	    	width: 450,
	    	height: 460,
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
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{							
							var formData = new FormData();
							var username = $("#add_username").val();
							var upwd = $("#add_upwd").val();
							var uicd = $("#add_uicd").val();
							var uname = $("#add_uname").val();
							var usex = $("#add_usex").combobox('getValue');
							var utel = $("#add_utel").val();
							var ubalance = $("#add_ubalance").val();
							var img_file = document.getElementById("add_uphoto");
							var uphoto = img_file.files[0];  

						
							formData.append("username",username);
							formData.append("upwd",upwd);
							formData.append("uicd",uicd);
							formData.append("uname",uname);
							formData.append("usex",usex);
							formData.append("utel",utel);
							formData.append("ubalance",ubalance);
							formData.append("uphoto",uphoto);
							$.ajax({
								type: "post",
								url: "CarUserServlet?method=AddCarUser",
								data: formData,
								contentType:false,
								processData:false,
								cache:false,
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#add_username").textbox('setValue', "");
										$("#add_upwd").textbox('setValue', "");
										$("#add_uicd").textbox('setValue', "");
										$("#add_uname").textbox('setValue', "");
										$("#add_usex").textbox('setValue', "男");
										$("#add_utel").textbox('setValue', "");
										$("#add_ubalance").textbox('setValue', "");
										
										//重新刷新页面数据
										//$('#dataList').datagrid("options").queryParams = {clazzid: clazzid};
							  			$('#dataList').datagrid("reload");		
									} else{
										$.messager.alert("消息提醒","该用户已存在!","warning");
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
						$("#add_username").textbox('setValue', "");
						$("#add_upwd").textbox('setValue', "");
						$("#add_uicd").textbox('setValue', "");
						$("#add_uname").textbox('setValue', "");
						$("#add_usex").textbox('setValue', "男");
						$("#add_utel").textbox('setValue', "");
						$("#add_ubalance").textbox('setValue', "");
						
						
						//重新加载年级
						//$("#add_gradeList").combobox("clear");
						//$("#add_gradeList").combobox("reload");
					}
				},
			]
	    });
	  	
	  	//设置编辑车主窗口
	    $("#editDialog").dialog({
	    	title: "修改车主信息",
	    	width: 600,
	    	height: 400,
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
								url: "CarUserServlet?method=EditCarUser&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","更新成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										
										$("#edit_username").textbox('setValue', "");
										$("#edit_upwd").textbox('setValue', "");
										$("#edit_uicd").textbox('setValue', "");
										$("#edit_uname").textbox('setValue', "");
										$("#edit_usex").textbox('setValue', "男");
										$("#edit_utel").textbox('setValue', "");
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
						$("#edit_username").textbox('setValue', "");
						$("#edit_upwd").textbox('setValue', "");
						$("#edit_uicd").textbox('setValue', "");
						$("#edit_uname").textbox('setValue', "");
						$("#edit_usex").textbox('setValue', "男");
						$("#edit_utel").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值
				
				$("#edit_username").textbox('setValue', selectRow.username);
				$("#edit_upwd").textbox('setValue', selectRow.upwd);
				$("#edit_uicd").textbox('setValue', selectRow.uicd);
				$("#edit_uname").textbox('setValue', selectRow.uname);
				$("#edit_usex").textbox('setValue', selectRow.usex);
				$("#edit_utel").textbox('setValue', selectRow.utel);
				$("#edit_uphoto").attr("src", "PhotoServlet?method=getPhoto&type=2&uid="+selectRow.uid);
				$("#edit_id").val(selectRow.uid);	
				$("#set_photo_id").val(selectRow.uid);
			}
	    });
	 // 搜索按钮监听事件
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			carUserName: $('#carUserName').val()
	  		});
	  	});
	});
	//上传图片按钮事件
	function uploadPhoto(){
		var action = $("#uploadForm").attr('action');
		$("#uploadForm").attr('action',action+'&uid='+$("#set_photo_id").val());
		$("#uploadForm").submit();
		setTimeout(function(){
			var message =  $(window.frames["photo_target"].document).find("#message").text();
			$.messager.alert("消息提醒",message,"info");			
			$("#edit_photo").attr("src", "PhotoServlet?method=getPhoto&uid="+$("#set_photo_id").val());
			$('#dataList').datagrid("reload");
		}, 1500)
	}
	</script>
</head>
<body>
	
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
		<c:if test="${userType == 1}">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
		</c:if>
		<c:if test="${userType == 1}">
			<div style="float: left;" class="datagrid-btn-separator"></div>
		</c:if>
		<div style="float: left;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<c:if test="${userType == 1}">
		<div style="float: left;" class="datagrid-btn-separator"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>		
		</c:if>
		
		<div style="margin-top:2px;">&nbsp;&nbsp;用户名：<input id="carUserName" class="easyui-textbox" name="carUserName" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		
		
		</div>	
	</div>
	
	<!-- 添加车主窗口 -->
	
	<div id="addDialog" style="padding: 10px">  
	    		<!-- StudentServlet?method=SetPhoto -->
	   <form id="addForm" method="post" enctype="multipart/form-data" action="CarUserServlet?method=addCarUser" >
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>选择上传的图片:</td>
	    			<td>
					<input id="add_uphoto" type="file"  name="uphoto" data-options="prompt:'选择照片'" style="width:200px;"></td>
	    		</tr>
	    		<tr>
	    			<td>用户名:</td>
	    			<td>
	    				<input id="add_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请输入用户名'" />
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="add_upwd" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="upwd" data-options="required:true, missingMessage:'请输入登录密码'" /></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>IC卡号:</td>
	    			<td><input id="add_uicd" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="uicd"  /></td>
	    		</tr>
	    		<tr>
	    			<td>姓名:</td>
	    			<td><input id="add_uname" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="uname" /></td>
	    		</tr>
	    		<tr>
	    			<td>性别:</td>
	    			<td><select id="add_usex" class="easyui-combobox" data-options="editable: false, panelHeight: 50, width: 60, height: 30" name="usex"><option value="男">男</option><option value="女">女</option></select></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>电话:</td>
	    			<td><input id="add_utel" style="width: 200px; height: 30px;" class="easyui-textbox" name="utel" validType="mobile"/></td>
	    		</tr>
	    		<tr>
	    			<td>卡内余额:</td>
	    			<td><input id="add_ubalance" style="width: 200px; height: 30px;" class="easyui-textbox" name="ubalance" validType="number"/></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 修改车主信息窗口 -->
	<div id="editDialog" style="padding: 10px">
		<div style="float: right; margin: 20px 20px 0 0; width: 200px; border: 1px solid #EBF3FF">
	    	<img id="edit_uphoto" alt="照片" style="max-width: 200px; max-height: 400px;" title="照片" src="PhotoServlet?method=getPhoto" />
	    	<form id="uploadForm" method="post" enctype="multipart/form-data" action="PhotoServlet?method=SetPhoto" target="photo_target">
	    		<!-- StudentServlet?method=SetPhoto -->
	    		<input type="hidden" id="set_photo_id" name="uid"  />	    
		    	<input class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
		    	<input id="upload-photo-btn" onClick="uploadPhoto()" class="easyui-linkbutton" style="width: 50px; height: 24px;" type="button" value="上传"/>
		    </form>
	    </div>   
    	<form id="editForm" method="post">
			<input type="hidden" id="edit_id" name="euid"  />	    
			<table cellpadding="8" >
	    		<tr>
	    		
	    			<td>用户名:</td>
	    			<td>
	    				<input id="edit_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="eusername" readonly="readonly" data-options="required:true, missingMessage:'请输入用户名'" />
	    			</td>
	    		</tr>
	    		<c:if test="${userType == 1}">
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="edit_upwd" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="eupwd" data-options="required:true, missingMessage:'请输入登录密码'" /></td>
	    		</tr>
	    		</c:if>
	    		<tr>
	    			<td>IC卡号:</td>
	    			<td><input id="edit_uicd" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="euicd" readonly="readonly"  /></td>
	    		</tr>
	    		<tr>
	    			<td>姓名:</td>
	    			<td><input id="edit_uname" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="euname" /></td>
	    		</tr>
	    		<tr>
	    			<td>性别:</td>
	    			<td><select id="edit_usex" class="easyui-combobox" data-options="editable: false, panelHeight: 50, width: 60, height: 30" name="eusex"><option value="男">男</option><option value="女">女</option></select></td>
	    		</tr>
	    		
	    		<tr>
	    			<td>电话:</td>
	    			<td><input id="edit_utel" style="width: 200px; height: 30px;" class="easyui-textbox" name="eutel" validType="mobile"/></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
		<!-- 提交表单处理iframe框架 -->
	<iframe id="photo_target" name="photo_target"></iframe>  
</body>
</html>