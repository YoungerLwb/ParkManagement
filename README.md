# ParkManagement
## 登录模块实现
用户打开由login.jsp展示的登录界面，输入用户名，密码，验证码并选择登录权限，前台界面会自动判断用户输入是否为空，随后点击登录按钮，系统后端自动查询数据库，判断用户名是否存在，用户名密码与权限是否匹配，验证码输入是否正确。当系统判断为普通用户登录，且用户名密码匹配，验证码正确，则系统跳转至前台界面。若系统判断为管理员登录，且用户名密码匹配，验证码正确，则系统跳转至后台界面。若系统查询用户不存在，则无法登录系统。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/login.png)
## 系统后台实现
### （1）系统后台首页
进入系统后台，左侧为导航菜单栏，包括车主信息管理、车位信息管理、车辆信息管理、车辆出入管理、停车收费价格设置、预约系信息管理、系统用户管理七个菜单项，用户可以根据需求执行各个操作。右侧窗口为不同模块信息表的内容展示，可以进行不同页面的展开，切换和删除。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/back.png)
### （2）车主信息管理
该部分管理员可以进行添加新的车主，对已有车主的信息进行修改，删除相关车主的信息，根据车主名查询相关车主，对车主IC卡余额进行充值等操作。点击相关信息可进行选中，每次操作只可选择一人，选中的信息会高光显示。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/carusermanage.png)
### （3）车位信息管理
该部分管理员可通过点击添加按钮增加车位，点击删除按钮去除车位，点击修改按钮来修改车位信息，每次只可选中一条信息进行修改。通过键盘输入车位号来查询相关车位的信息。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/carPark.png)
### （4）车辆信息管理
该部分管理员可以点击添加按钮，点击后出现添加表单，可以选择目前已有车主，并填写该车主车辆信息进行添加，一个车主可以添加多个车辆信息。点击删除按钮删除相关车辆信息，点击修改按钮来修改车辆信息，每次只可选中一条信息进行修改。通过键盘输入车牌号或车主姓名来查询相关车辆的信息。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/car.png)
### （5）车辆出入管理
管理员点击车辆驶入添加，进入车辆驶入添加页面，选择要驶入的车辆，点击选择驶入车辆按钮，填写表单，添加完成后提示车辆入库成功。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/carDriveIn.png)
管理员点击车辆停车缴费，进入车辆停车缴费界面，选择车辆，点击选择驶出车辆按钮弹出表单，表单包括车牌号、车主、驶入时间、驶出时间、停车时长、卡内余额、费用、单价等信息，点击提交进行缴费出库。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/carDriveOut.png)
### （6）预约信息管理
该部分管理员可以对车主预约列表进行审核，选择一条要审核的数据，点击审核按钮，选择审核通过或审核不通过，点击提交按钮进行审核提交。也可选择一条审核信息，点击删除按钮，删除有关预约信息。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/parkOrder.png)
## 系统前台实现
### （1）停车场车位列表
该部分用户进入车位列表查看页面，可以查看当前停车场车位状态，通过车位号搜索自己想停的车位，查看是否已有车辆。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/parkList.png)
### （2）停车记录查看
该部分用户进入停车记录查询页面，可以查看用户自己的历史停车记录，也可以将停车记录导出为Excel文件格式。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/parkRecord.png)
### （3）预约信息管理模块
该部分用户进入预约列表模块，页面上显示出当前车主自己已有的预约信息，点击添加按钮，车主可以添加新的预约，点击修改按钮，可以修改预约时间，若预约完成后修改信息，则需要管理员重新审核。
![image](https://github.com/YoungerLwb/ParkManagement/blob/master/image/userRecord.png)
