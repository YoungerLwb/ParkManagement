<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>停车场管理系统 管理员后台</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<link rel="bookmark" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="easyui/css/default.css" />
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css" />
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src='easyui/js/outlook2.js'> </script>
    <script type="text/javascript">
	 var _menus = {"menus":[
						 <c:if test="${userType == 1}">
						{"menuid":"1","icon":"","menuname":"车主信息管理",
							"menus":[
									{"menuid":"11","menuname":"车主信息管理","icon":"icon-book-add","url":"CarUserServlet?method=toCarUserListView"},
									{"menuid":"12","menuname":"IC卡余额充值","icon":"icon-exam","url":"CarUserServlet?method=addBalance"}
									]
						},
						</c:if>
						
						<c:if test="${userType == 2}">
						{"menuid":"7","icon":"","menuname":"个人信息管理",
							"menus":[
									{"menuid":"71","menuname":"个人信息","icon":"icon-user-teacher","url":"CarUserServlet?method=toCarUserListView"},
									]
						},
						</c:if>
						<c:if test="${userType == 2}">
						{"menuid":"8","icon":"","menuname":"停车场车位列表",
							"menus":[
									{"menuid":"81","menuname":"车位列表查看","icon":"icon-carpark","url":"ParkServlet?method=toParkListView"},
									]
						},
						</c:if>
						<c:if test="${userType == 2}">
						{"menuid":"9","icon":"","menuname":"停车记录查看",
							"menus":[
									{"menuid":"91","menuname":"停车记录查询","icon":"icon-bullets","url":"CarDriveInAOutServlet?method=toCarDriveOutListView"}
								]
						},
						</c:if>
						<c:if test="${userType == 1}">
						{"menuid":"2","icon":"","menuname":"车位信息管理",
							"menus":[
									{"menuid":"21","menuname":"车位管理","icon":"icon-carpark","url":"ParkServlet?method=toParkListView"},
									]
						},
						</c:if>
						<c:if test="${userType == 1}">
						{"menuid":"3","icon":"","menuname":"车辆信息管理",
							"menus":[
									{"menuid":"31","menuname":"车辆管理","icon":"icon-car","url":"CarInfoServlet?method=toCarInfoListView"},
									]
						},
						
						{"menuid":"4","icon":"","menuname":"车辆出入管理",
							"menus":[
									{"menuid":"41","menuname":"车辆驶入添加","icon":"icon-carin","url":"CarDriveInAOutServlet?method=toCarDriveIn"},
									{"menuid":"42","menuname":"车辆驶入情况","icon":"icon-bullets","url":"CarDriveInAOutServlet?method=toCarDriveInListView"},
									{"menuid":"43","menuname":"车辆停车缴费","icon":"icon-carout","url":"CarDriveInAOutServlet?method=toCarDriveOut"},
									{"menuid":"44","menuname":"停车记录查询","icon":"icon-bullets","url":"CarDriveInAOutServlet?method=toCarDriveOutListView"}
								]
						},
						
						{"menuid":"5","icon":"","menuname":"停车收费价格设置",
							"menus":[
									{"menuid":"51","menuname":"停车收费价格管理","icon":"icon-price","url":"CarPriceServlet?method=toCarPriceListView"},
									
								]
						},
						</c:if>
						
						{"menuid":"10","icon":"","menuname":"预约信息管理",
							"menus":[
									{"menuid":"101","menuname":"预约列表","icon":"icon-appoint","url":"AppointServlet?method=toAppointListView"}
								]
						},
						
						{"menuid":"6","icon":"","menuname":"系统用户管理",
							"menus":[
								<c:if test="${userType == 1}">
									{"menuid":"61","menuname":"系统用户","icon":"icon-administrator","url":"SystemServlet?method=toAdminiList"},//toAdminPersonalView
							        </c:if>
									{"menuid":"62","menuname":"修改密码","icon":"icon-set","url":"SystemServlet?method=toPersonalView"}
								]
						}
						
				]};


    </script>

</head>
<body class="easyui-layout" style="overflow-y: hidden"  scroll="no">
	<noscript>
		<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
		    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
    <div region="north" split="true" border="false" style="overflow: hidden; height: 30px;
        background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <span style="float:right; padding-right:20px;" class="head"><span style="color:red; font-weight:bold;">${user.username}&nbsp;</span>您好&nbsp;&nbsp;&nbsp;<a href="LoginServlet?method=LoginOut" id="loginOut">安全退出</a></span>
        <span style="padding-left:10px; font-size: 16px; ">停车场管理系统</span>
    </div>
    <div region="south" split="true" style="height: 30px; background: #D2E0F2; ">
        <div class="footer">Copyright &copy;  By LWB</div>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
	<div id="nav" class="easyui-accordion" fit="true" border="false">
		<!--  导航内容 -->
	</div>
	
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<jsp:include page="welcome.jsp" />
		</div>
    </div>

	<iframe width=0 height=0 src="refresh.jsp"></iframe>
	
</body>
</html>