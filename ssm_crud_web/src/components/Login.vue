<template>
	<div class="login_container">
		<div class="login_box">
			<div class="avatar_box">
				<img src="../assets/logo.png" />
			</div>
			<el-form ref="loginFormRef" :model="loginForm" label-width="0px" class="login_form">
				<el-form-item prop="empName">
					<el-input v-model="loginForm.empName" prefix-icon="iconfont icon-user" ></el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input type="password" v-model="loginForm.password" prefix-icon="iconfont icon-3702mima" ></el-input>
				</el-form-item>
				<el-form-item class="btns">
					<el-button type="primary" @click="login">登录</el-button>
					<el-button type="info" @click="resetLoginForm">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	export default {
		data(){
			return{
				loginForm:{
					empName:"",
					password:""
				}
			}
		},
		methods:{
			resetLoginForm(){
				this.$refs['loginFormRef'].resetFields();
			},
			async login(){
				const {data:res} = await this.$http.post("login",this.loginForm);
				// console.log(res);
				if(res.code==100){
					this.$message.success("登录成功");
					window.sessionStorage.setItem('name',res.extend.emp.empName);
					window.sessionStorage.setItem('empId',res.extend.emp.empId);
					window.sessionStorage.setItem('dId',res.extend.emp.dId);
					// console.log(res.extend)
					this.$router.push("/home")
				}else{
					return this.$message.error("登录失败！请检查账户密码");
				}
				
			}
		}
	}
</script>

<style lang="less" scoped>
	.login_container {
		background-color: #2b4b6b;
		height: 100%;
	}

	.login_box {
		width: 450px;
		height: 300px;
		background-color: #fff;
		border-radius: 3px;
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
	}

	.avatar_box {
		height: 130px;
		width: 130px;
		border: 1px solid #eee;
		border-radius: 50%;
		padding: 10px;
		box-shadow: 0 0 10px #ddd;
		position: absolute;
		left: 50%;
		transform: translate(-50%, -50%);
		background-color: #fff;

		img {
			width: 100%;
			height: 100%;
			border-radius: 50%;
			background-color: #eee;
		}
	}
	.btns{
		display: flex;
		justify-content: flex-end;
	}
	.login_form{
		position: absolute;
		bottom: 0;
		width: 100%;
		padding: 0 20px;
		box-sizing: border-box;
	}
</style>
