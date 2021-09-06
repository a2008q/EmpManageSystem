<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>员工</el-breadcrumb-item>
			<el-breadcrumb-item>员工信息</el-breadcrumb-item>
		</el-breadcrumb>
		<!-- 卡片视图区 -->
		<el-card>
			<!-- 搜索与添加区域 -->

			<el-row :gutter="20">
				<el-col :span="8">
					<el-input placeholder="请输入内容" v-model="query" clearable @clear="getEmpList(1)">
						<el-button slot="append" icon="el-icon-search" @click="queryUser(1)"></el-button>
					</el-input>
				</el-col>
			</el-row>
			<!-- 用户列表区 -->
			<el-table :data="empList" border stripe>
				<el-table-column label="Id" prop="empId"></el-table-column>
				<el-table-column label="姓名" prop="empName"></el-table-column>
				<el-table-column label="邮箱" prop="email"></el-table-column>
				<el-table-column label="性别" prop="gender" :formatter="sexFormatter"></el-table-column>
				<el-table-column label="部门" prop="department.deptName"></el-table-column>
			</el-table>
			<!-- 分页 -->
			<el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize"
				layout="total, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-card>

	</div>
</template>

<script>
	export default {
		data() {
			return {
				pageNum: 0,
				pageSize: 0,
				empList: [],
				deptList: [],
				total: 0,
				query: '',
			}
		},

		created() {
			this.getEmpList(1)
		},
		methods: {
			async queryUser(pn) {
				const {
					data: res
				} = await this.$http.get('empSearch?content=' + this.query + "&pn=" + pn)
				// console.log(res)
				if (res.code != 100) {
					return this.$message.error("搜索失败")
				}
				this.empList = res.extend.pageInfo.list
				this.total = res.extend.pageInfo.total
				this.pageNum = res.extend.pageInfo.pageNum
				this.pageSize = res.extend.pageInfo.pageSize
			},
			async getEmpList(pn) {
				const {
					data: res
				} = await this.$http.get('emps_json?pn=' + pn)
				// console.log(res)
				if (res.code != 100) {
					return this.$message.error("获取用户列表失败")
				}
				this.empList = res.extend.pageInfo.list
				this.total = res.extend.pageInfo.total
				this.pageNum = res.extend.pageInfo.pageNum
				this.pageSize = res.extend.pageInfo.pageSize
			},
			async getDeptList() {
				const {
					data: res
				} = await this.$http.get('depts_json')
				if (res.code != 100) {
					return this.$message.error("获取部门列表失败")
				}
				this.deptList = res.extend.depts
			},
			sexFormatter(row, column, cellValue, index) {
				return cellValue === 'M' ? '男' : '女'
			},
			handleCurrentChange(newPage) {
				// console.log(this.query != "")
				if (this.query != "") {
					this.queryUser(newPage)
				} else {
					this.getEmpList(newPage)
				}
			}
		}
	}
</script>

<style lang="less" scoped>
</style>
