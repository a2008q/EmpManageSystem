<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>员工管理</el-breadcrumb-item>
			<el-breadcrumb-item>员工列表</el-breadcrumb-item>
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
				<el-col :span="4">
					<el-button type="primary" @click="showAddDialog">添加用户</el-button>
				</el-col>
			</el-row>
			<!-- 用户列表区 -->
			<el-table :data="empList" border stripe>
				<el-table-column label="Id" prop="empId"></el-table-column>
				<el-table-column label="姓名" prop="empName"></el-table-column>
				<el-table-column label="邮箱" prop="email"></el-table-column>
				<el-table-column label="性别" prop="gender" :formatter="sexFormatter"></el-table-column>
				<el-table-column label="部门" prop="department.deptName"></el-table-column>
				<el-table-column label="操作" width="150px">
					<template slot-scope="scope">
						<el-tooltip effect="dark" content="修改员工" placement="top" :enterable="false">
							<el-button type="primary" icon="el-icon-edit" size="mini"
								@click="showEditDialog(scope.row.empId)">
							</el-button>
						</el-tooltip>
						<el-tooltip effect="dark" content="删除员工" placement="top" :enterable="false">
							<el-button type="danger" icon="el-icon-delete" size="mini"
								@click="deleteEmpById(scope.row.empId)"></el-button>
						</el-tooltip>
					</template>
				</el-table-column>
			</el-table>
			<!-- 分页 -->
			<el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-size="pageSize"
				layout="total, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-card>
		<!-- 添加用户 -->
		<el-dialog title="添加员工" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
			<!-- 内容主体区 -->
			<el-form ref="addFormRef" :model="addForm" :rules="addFormRules" label-width="70px">
				<el-form-item label="姓名" prop="empName">
					<el-input v-model="addForm.empName"></el-input>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
					<el-input type="email" v-model="addForm.email"></el-input>
				</el-form-item>
				<el-form-item label="性别">
					<el-select v-model="addForm.gender" placeholder="请选择性别">
						<el-option label="男" value="M"></el-option>
						<el-option label="女" value="F"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="部门">
					<el-select v-model="addForm.dId" placeholder="请选择部门">
						<el-option v-for="(dept,index) in deptList" :key="index" :label="dept.deptName"
							:value="dept.deptId"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<!-- 底部按钮区域 -->
			<span slot="footer" class="dialog-footer">
				<el-button @click="addDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="addEmp">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 修改用户对话框 -->
		<el-dialog title="修改用户" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
			<!-- 内容主体区 -->
			<el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="70px">
				<el-form-item label="Id">
					<el-input v-model="editForm.empId" disabled></el-input>
				</el-form-item>
				<el-form-item label="姓名" prop="empName">
					<el-input v-model="editForm.empName"></el-input>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
					<el-input type="email" v-model="editForm.email"></el-input>
				</el-form-item>
				<el-form-item label="性别">
					<el-select v-model="editForm.gender" placeholder="请选择性别">
						<el-option label="男" value="M"></el-option>
						<el-option label="女" value="F"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="部门">
					<el-select v-model="editForm.dId" placeholder="请选择部门">
						<el-option v-for="(dept,index) in deptList" :key="index" :label="dept.deptName"
							:value="dept.deptId"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<!-- 底部按钮区域 -->
			<span slot="footer" class="dialog-footer">
				<el-button @click="editDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="editEmp">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			var checkEmail = (rule, value, callback) => {
				//正则
				const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-z0-9_-])+/
				if (!regEmail.test(value)) {
					return callback(new Error('请输入合法的邮箱'))
				}
				callback()
			}
			return {
				pageNum: 0,
				pageSize: 0,
				empList: [],
				deptList: [],
				total: 0,
				query: '',
				addDialogVisible: false,
				editDialogVisible: false,
				addForm: {},
				editForm: {},
				addFormRules: {
					empName: [{
						required: true,
						message: "请输入姓名",
						trigger: 'blur'
					}, {
						min: 6,
						max: 16,
						message: "姓名必须是6-16位数字，字母或者_-，也可以是2-5位中文组成",
						trigger: "blur"
					}],
					email: [{
						required: true,
						message: "请输入邮箱",
						trigger: 'blur'
					}, {
						validator: checkEmail,
						trigger: "blur"
					}]
				},
				editFormRules: {
					empName: [{
						required: true,
						message: "请输入姓名",
						trigger: 'blur'
					}, {
						min: 6,
						max: 16,
						message: "姓名必须是6-16位数字，字母或者_-，也可以是2-5位中文组成",
						trigger: "blur"
					}],
					email: [{
						required: true,
						message: "请输入邮箱",
						trigger: 'blur'
					}, {
						validator: checkEmail,
						trigger: "blur"
					}]
				}
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
			},
			async showAddDialog() {
				this.addDialogVisible = true
				this.getDeptList()
			},
			async showEditDialog(id) {
				this.getDeptList()
				const {
					data: res
				} = await this.$http.get('emp/' + id)
				// console.log(res)
				if (res.code != 100) {
					return this.$message.error("查询用户信息失败")
				}
				this.editForm = res.extend.emp
				this.editDialogVisible = true
				// console.log(this.editForm)
			},
			addDialogClosed() {
				this.$refs.addFormRef.resetFields()
			},
			editDialogClosed() {
				this.$refs.editFormRef.resetFields()
			},
			addEmp() {
				this.$refs.addFormRef.validate(async valid => {
					if (!valid) return
					const {
						data: res
					} = await this.$http.post('emp', this.addForm)
					if (res.code != 100) {
						return this.$message.error("添加失败")
					}
					this.$message.success("添加成功")
					this.addDialogVisible = false
					this.getEmpList(this.total)
				})
			},
			editEmp() {
				this.$refs.editFormRef.validate(async valid => {
					if (!valid) return
					const {
						data: res
					} = await this.$http.put('emp/' + this.editForm.empId, this.editForm)
					if (res.code != 100) {
						return this.$message.error("修改失败")
					}
					this.$message.success("修改成功")
					this.editDialogVisible = false
					this.getEmpList(1)
				})
			},
			async deleteEmpById(id) {
				const confirmResult = await this.$confirm('此操作将永久删除该员工, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).catch(err => err)
				if (confirmResult != "confirm") {
					return this.$message.info("已取消删除")
				}
				const {
					data: res
				} = await this.$http.delete('emp/' + id)
				if (res.code != 100) {
					return this.$message.error("删除失败")
				}
				this.$message.success("删除成功")
				this.getEmpList(1)
			}
		}
	}
</script>

<style lang="less" scoped>
</style>
