# ParkManagement
## 登录模块实现
用户打开由login.jsp展示的登录界面，输入用户名，密码，验证码并选择登录权限，前台界面会自动判断用户输入是否为空，随后点击登录按钮，系统后端自动查询数据库，判断用户名是否存在，用户名密码与权限是否匹配，验证码输入是否正确。当系统判断为普通用户登录，且用户名密码匹配，验证码正确，则系统跳转至前台界面。若系统判断为管理员登录，且用户名密码匹配，验证码正确，则系统跳转至后台界面。若系统查询用户不存在，则无法登录系统。
