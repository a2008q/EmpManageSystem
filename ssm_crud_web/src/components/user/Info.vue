<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>个人信息修改</el-breadcrumb-item>
		</el-breadcrumb>
		<el-card>
			<el-form ref="infoFormRef" :model="infoForm" label-width="80px">
				<el-form-item label="Id">
					<el-input v-model="infoForm.empId" disabled></el-input>
				</el-form-item>
				<el-form-item label="姓名" prop="empName">
					<el-input v-model="infoForm.empName" disabled></el-input>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">

					<el-input type="email" v-model="infoForm.email"></el-input>
					<el-button @click="showChangeEmailDialog(infoForm.email)">更换邮箱</el-button>
				</el-form-item>
				<el-form-item label="性别">
					<el-input v-model="infoForm.gender=='M'?'男':'女'" disabled></el-input>
				</el-form-item>
				<el-form-item label="部门">
					<el-input v-model="infoForm.department.deptName" disabled></el-input>
				</el-form-item>
				<!-- 				<el-form-item>
					<el-button>取 消</el-button>
					<el-button type="primary">确 定</el-button>
				</el-form-item> -->
			</el-form>
		</el-card>
		<el-dialog title="提示" :visible.sync="ChangeEmailDialogVisible" width="50%">
			<el-form ref="changeEmailFormRef" :model="changeEmailForm" label-width="80px">
				<el-row :gutter="20">
					<el-form-item label="邮箱">

						<el-col :span="12">
							<el-input v-model="changeEmailForm.email"></el-input>
						</el-col>
						<el-col :span="8">
							<el-button type="primary" :disabled="disabled" @click="getVerifyCode">{{valiBtn}}
							</el-button>
						</el-col>

					</el-form-item>
				</el-row>
				<el-form-item label="验证码">
					<el-input v-model="changeEmailForm.verifyCode"></el-input>
				</el-form-item>

			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="ChangeEmailDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="changeEmail">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				infoForm: {},
				ChangeEmailDialogVisible: false,
				changeEmailForm: {},
				disabled: false,
				valiBtn: '获取验证码'
			}
		},
		created() {
			this.getInfo()
		},
		methods: {
			async getInfo() {
				const {
					data: res
				} = await this.$http.get("emp/" + window.sessionStorage.getItem("empId"));
				if (res.code != 100) {
					return this.$message.error("获取信息失败")
				}
				// console.log(res)
				this.infoForm = res.extend.emp
			},
			showChangeEmailDialog() {
				this.changeEmailForm.empId = this.infoForm.empId
				this.ChangeEmailDialogVisible = true
			},
			async getVerifyCode() {
				this.tackBtn()
				const {
					data: res
				} = await this.$http.get("send?email=" + this.changeEmailForm.email);
				if (res.code != 100) {
					return this.$message.error(res.extend.error)
				}
			},
			tackBtn() { //验证码倒数60秒
				let time = 60;
				let timer = setInterval(() => {
					if (time == 0) {
						clearInterval(timer);
						this.valiBtn = '获取验证码';
						this.disabled = false;
					} else {
						this.disabled = true;
						this.valiBtn = time + '秒后重试';
						time--;
					}
				}, 1000);
			},
			async changeEmail() {
				const {
					data: res
				} = await this.$http.post("emp/email/" + this.changeEmailForm.verifyCode, this.changeEmailForm);
				if (res.code != 100) {
					 //console.log(res)
					return this.$message.error(res.extend.error)
				}
				this.ChangeEmailDialogVisible = false
				this.$message.success("更改邮箱成功")
				this.getInfo()
			}
		}
	}
</script>

<style>
</style>
