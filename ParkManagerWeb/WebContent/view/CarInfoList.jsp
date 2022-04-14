<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			return "<img style='height:50px;width:207px;' src='PhotoServlet?method=getPhoto&cid="+row.cid+"' />";
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
            	
            	var cids = [];
            	$(selectRows).each(function(i, selectRows){
            		cids[i] = selectRows.cid;
            	});
            	$.messager.confirm("消息提醒", "将删除与车主相关的所有数据，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "CarInfoServlet?method=DeleteCarInfo",
							data: {cids: cids},
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
	    
	    //下拉框通用属性
	  	$("#add_cname").combobox({
	  		width: "200",
	  		height: "30",
	  		valueField: "uname",
	  		textField: "uname",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});
	  	$("#add_cname").combobox({
	  		url: "CarUserServlet?method=getCarUserList&t="+new Date().getTime()+"&from=combox",
	  		onLoadSuccess: function(){
		  		//默认选择第一条数据
				var data = $(this).combobox("getData");;
				$(this).combobox("setValue", data[0].uname);
	  		}
	  	});
	    
	    
	  	//设置添加车辆窗口
	    $("#addDialog").dialog({
	    	title: "添加车主",
	    	width: 450,
	    	height: 340,
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
							//var gradeid = $("#add_gradeList").combobox("getValue");
							//var clazzid = $("#add_clazzList").combobox("getValue");
							
							var formData = new FormData();
							var cplatenum = $("#add_cplatenum").val();
							var cname = $("#add_cname").combobox('getValue');
							var img_file = document.getElementById("add_cphoto");
							var cphoto = img_file.files[0]; 
							var cbrand = $("#add_cbrand").val();

							formData.append("cplatenum",cplatenum);
							formData.append("cname",cname);
							formData.append("cphoto",cphoto);
							formData.append("cbrand",cbrand);
							$.ajax({
								type: "post",
								url: "CarInfoServlet?method=AddCarInfo",
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
										$("#add_cplatenum").textbox('setValue', "");
										$("#add_cname").textbox('setValue', "");
										$("#add_cbrand").textbox('setValue', "");
										
										//重新刷新页面数据
										//$('#dataList').datagrid("options").queryParams = {clazzid: clazzid};
							  			$('#dataList').datagrid("reload");		
									} else{
										$.messager.alert("消息提醒","添加失败,请审查表单信息","warning");
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
						$("#add_cplatenum").textbox('setValue', "");
						//$("#add_cname").textbox('setValue', "");
						$("#add_cbrand").textbox('setValue', "");
					}
				},
			]
	    });
	  	
	  	//设置编辑车辆窗口
	    $("#editDialog").dialog({
	    	title: "修改车主信息",
	    	width: 350,
	    	height: 350,
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
								url: "CarInfoServlet?method=EditCarInfo&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","更新成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										
										$("#edit_cplatenum").textbox('setValue', "");
										$("#edit_cname").textbox('setValue', "");
										$("#edit_cbrand").textbox('setValue', "");
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
						$("#edit_cplatenum").textbox('setValue', "");
						$("#edit_cname").textbox('setValue', "");
						$("#edit_cbrand").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值
				
				$("#edit_username").textbox('setValue', selectRow.username);
				$("#edit_cplatenum").textbox('setValue', selectRow.cplatenum);
				$("#edit_cname").textbox('setValue', selectRow.cname);
				$("#edit_cbrand").textbox('setValue', selectRow.cbrand);
				$("#edit_cphoto").attr("src", "PhotoServlet?method=getPhoto&type=2&cid="+selectRow.cid);
				$("#edit_id").val(selectRow.cid);	
				$("#set_cphoto_id").val(selectRow.cid);
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
	//上传图片按钮事件
	function uploadPhoto(){
		var action = $("#uploadForm").attr('action');
		$("#uploadForm").attr('action',action+'&cid='+$("#set_cphoto_id").val());
		$("#uploadForm").submit();
		
		setTimeout(function(){
			var message =  $(window.frames["photo_target"].document).find("#message").text();
			$.messager.alert("消息提醒",message,"info");			
			$("#edit_cphoto").attr("src", "PhotoServlet?method=getPhoto&cid="+$("#set_cphoto_id").val());
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
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加车辆</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;"><a id="edit-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;" class="datagrid-btn-separator"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>		
		<div style="margin-top:3px;">&nbsp;&nbsp;车牌号：<input id="carPlatenum" class="easyui-textbox" name="carPlatenum" />车主姓名：<input id="carcname" class="easyui-textbox" name="carcname" />
		<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>	
	</div>
	
	<!-- 添加学生窗口 -->
	<div id="addDialog" style="padding: 10px">  
		
	   <form id="addForm" method="post" enctype="multipart/form-data">
	    	<table cellpadding="8" >
	    		
	    		<tr>
	    			<td>车牌号:</td>
	    			<td>
	    				<input id="add_cplatenum"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="cplatename" data-options="required:true, missingMessage:'请输入车牌号'" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>车主:</td>
	    			<td><input id="add_cname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="cname" data-options="required:true, missingMessage:'请输入车主名'" /></td>
	    		</tr>
	    		<tr>
	    			<td>选择上传的图片:</td>
	    			<td>
					<input id="add_cphoto" type="file"  name="cphoto" data-options="prompt:'选择照片'" style="width:200px;"></td>
	    		</tr>
	    		<tr>
	    			<td>车辆品牌:</td>
	    			<td><input id="add_cbrand" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="cbrand" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 修改学生窗口 -->
	<div id="editDialog" style="padding: 10px">
		<div  width: 200px; border: 1px solid #EBF3FF">
	    	<img id="edit_cphoto" alt="照片" style="max-width: 100px; max-height: 200px;" title="照片" src="PhotoServlet?method=getPhoto" />
	    	<form id="uploadForm" method="post" enctype="multipart/form-data" action="PhotoServlet?method=SetPhoto" target="photo_target">
	    		<!-- StudentServlet?method=SetPhoto -->
	    		<input type="hidden" id="set_cphoto_id" name="cid"  />	    
		    	<input class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
		    	<input id="upload-photo-btn" onClick="uploadPhoto()" class="easyui-linkbutton" style="width: 50px; height: 24px;" type="button" value="上传"/>
		    </form>
	    </div>   
    	<form id="editForm" method="post">
			<input type="hidden" id="edit_id" name="ecid"  />	    
			<table cellpadding="8" >		
	    		<tr>
	    			<td>车牌号:</td>
	    			<td>
	    				<input id="edit_cplatenum"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="ecplatenum" data-options="required:true, missingMessage:'请输入车牌号'" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>车主:</td>
	    			<td><input id="edit_cname" style="width: 200px; height: 30px;" class="easyui-textbox"  name="ecname"  /></td>
	    		</tr>
	    		<tr>
	    			<td>车辆品牌:</td>
	    			<td><input id="edit_cbrand" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="ecbrand" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
		<!-- 提交表单处理iframe框架 -->
	<iframe id="photo_target" name="photo_target"></iframe>  
</body>
</html>