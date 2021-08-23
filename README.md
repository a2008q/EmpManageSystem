# EmpManageSystem 员工管理系统

## 前端

### 前端使用vue_cli脚手架完成，需安装好相关依赖

```
cd ssm_crud_web
npm install
```

### 开发模式运行前端

```
npm run serve
前端地址 http://127.0.0.1:8080
```

### 编译发布

```
npm run build
```

![image](https://z3.ax1x.com/2021/08/20/fOuuI1.png)
![image](https://z3.ax1x.com/2021/08/20/fOKIht.png)
![image](https://z3.ax1x.com/2021/08/20/fOM3HH.png)
![image](https://z3.ax1x.com/2021/08/20/fOMdv8.png)

## 后端

### 后端使用ssm+maven+redis

```
后端接口:http://127.0.0.1:8081/swagger/index.html#/
```

### 邮件发送需在resource目录配置email.properties

```
# 协议
mail.transport.protocol = SMTP
# smtp服务器地址
mail.host = smtpdm.aliyun.com
mail.smtp.auth = true
# 账号
mail.account = xxx
# 密码
mail.password =xxx
```

![image](https://z3.ax1x.com/2021/08/20/fOM75R.png)

## TODO

1. ~~部门增加修改删除~~
2. ~~个人信息界面~~
3. ~~修改邮箱（邮箱验证码）~~
4. 修改密码（使用邮箱验证）
5. token鉴权
6. 头像上传
7. 批量导出导入