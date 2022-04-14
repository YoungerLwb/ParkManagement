<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	        singleSelect:true,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'uid',// id
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50,align:'center'},
 		        {field:'uid',title:'ID',width:74,align:'center',sortable: true},    
 		        {field:'username',title:'用户名',width:126,align:'center'},    
 		        {field:'upwd',title:'密码',width:136,align:'center'},
 		        {field:'uicd',title:'IC卡号',width:129,align:'center'},
 		        {field:'uname',title:'姓名',width:102,align:'center'},
 		        {field:'usex',title:'性别',width:92,align:'center'},
 		        {field:'utel',title:'电话',width:179,align:'center'},
 		        {field:'ubalance',title:'卡内余额',width:81,align:'center'},	
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

	  	//设置充值金额窗口
	    $("#editDialog").dialog({
	    	title: "充值金额",
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
					text:'充值',
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
								url: "CarUserServlet?method=addBalanceCarUser&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","充值成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");			
										$("#edit_ubalance").textbox('setValue', "");
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
						$("#edit_ubalance").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值				
				$("#edit_id").val(selectRow.uid);	
				$("#edit_ban").val(selectRow.ubalance);	

			}
	    });
	 // 搜索按钮监听事件
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('load',{
	  			carUserName: $('#carUserName').val()
	  		});
	  	});
	});

	</script>
</head>
<body>
	<!-- 车主列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">充值</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="margin-top:3px;">&nbsp;&nbsp;用户名：<input id="carUserName" class="easyui-textbox" name="carUserName" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>	
	</div>
	
	
	<!-- 充值金额窗口 -->
	<div id="editDialog" style="padding: 10px">   
    	<form id="editForm" method="post">
			<input type="hidden" id="edit_id" name="euid"  />	  
			<input type="hidden" id="edit_ban" name="eban"  />	    
			  
			<table cellpadding="8" >
	    		<tr>	    		
	    			<td>充值金额:</td>
	    			<td>
	    				<input id="edit_ubalance"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="eubalance" data-options="required:true, missingMessage:'请输入金额'" />
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>