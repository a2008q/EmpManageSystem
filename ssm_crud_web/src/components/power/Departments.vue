<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>权限管理</el-breadcrumb-item>
			<el-breadcrumb-item>部门列表</el-breadcrumb-item>
		</el-breadcrumb>
		<!-- 卡片视图 -->
		<el-card>
			<!-- 添加部门按钮区-->
			<el-row>
				<el-col>
					<el-button type="primary" @click="addDeptDialogVisible = true">添加部门</el-button>
				</el-col>
			</el-row>
			<el-table :data="deptList" border stripe>
				<!-- 展开列 -->
				<el-table-column type="expand">
					<template slot-scope="scope">
						<el-row :class="['bdbottom',i1===0 ? 'bdtop':'','vcenter']"
							v-for="(item1, i1) in scope.row.menus" :key="item1.id">
							<!-- 一级权限 -->
							<el-col :span="5">
								<el-tag closable @close="removeRightById(scope.row,item1.id)">{{item1.name}}</el-tag>
								<i class="el-icon-caret-right"></i>
							</el-col>
							<!-- 二级和三级 -->
							<el-col :span="19">
								<!-- 通过for循环 渲染二级权限 -->
								<el-row :class="[i2 ===0?'':'bdtop','vcenter']" v-for="(item2,i2) in item1.children"
									:key="item2.id">
									<el-col :span="6">
										<el-tag type="success" closable @close="removeRightById(scope.row,item2.id)">
											{{item2.name}}
										</el-tag>
										<i class="el-icon-caret-right"></i>
									</el-col>
									<el-col :span="18">
										<el-tag v-for="(item3,i3) in item2.children" :key="item3.id" type="warning">
											{{item3.name}}
										</el-tag>
									</el-col>
								</el-row>
							</el-col>
						</el-row>
					</template>


				</el-table-column>
				<el-table-column label="Id" prop="deptId"></el-table-column>
				<el-table-column label="部门" prop="deptName"></el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.deptId)">编辑
						</el-button>
						<el-button type="danger" icon="el-icon-delete" @click="deleteDeptById(scope.row.deptId)">删除
						</el-button>
						<el-button type="warning" icon="el-icon-setting" @click="showSetMenuDialog(scope.row)">分配权限
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-card>
		<!-- 分配权限对话框 -->
		<el-dialog title="分配权限" :visible.sync="setMenuDialogVisible" width="50%" @close="setMenuDialogClose">
			<!-- 树形控件 -->
			<el-tree :data="menuList" :props="treeProps" ref="treeRef" show-checkbox node-key="id" default-expand-all
				:default-checked-keys="defKeys"></el-tree>
			<span slot="footer" class="dialog-footer">
				<el-button @click="setMenuDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="allotMenus">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 添加部门对话框 -->
		<el-dialog title="添加部门" :visible.sync="addDeptDialogVisible" width="50%">
			<!-- 内容主体区 -->
			<el-form ref="addDeptFormRef" :model="addDeptForm" label-width="70px">
				<el-form-item label="部门名称">
					<el-input v-model="addDeptForm.deptName"></el-input>
				</el-form-item>
			</el-form>
			<!-- 底部按钮区域 -->
			<span slot="footer" class="dialog-footer">
				<el-button @click="addDeptDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="addDept">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 修改部门对话框 -->
		<el-dialog title="修改部门" :visible.sync="editDeptDialogVisible" width="50%">
			<!-- 内容主体区 -->
			<el-form ref="editDeptFormRef" :model="editDeptForm" label-width="70px">
				<el-form-item label="Id">
					<el-input v-model="editDeptForm.deptId" disabled></el-input>
				</el-form-item>
				<el-form-item label="部门名称">
					<el-input v-model="editDeptForm.deptName"></el-input>
				</el-form-item>
			</el-form>
			<!-- 底部按钮区域 -->
			<span slot="footer" class="dialog-footer">
				<el-button @click="editDeptDialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="editDept">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				deptList: [],
				setMenuDialogVisible: false,
				menuList: [],
				treeProps: {
					children: 'children',
					label: 'name'
				},
				defKeys: [],
				deptId: '',
				addDeptDialogVisible: false,
				editDeptDialogVisible: false,
				addDeptForm: {},
				editDeptForm: {}
			}
		},
		created() {
			this.getDeptList()
		},
		methods: {
			async getDeptList() {
				const {
					data: res
				} = await this.$http.get('dept')
				if (res.code != 100) {
					return this.$message.error("获取部门列表失败")
				}
				// console.log(res.extend)
				// this.deptList = res.extend.dept
				let dept = res.extend.dept

				function toTree(data) {
					data.forEach(function(item) {
						delete item.children;
					})
					let map = {};
					data.forEach(function(item) {
						map[item.id] = item;
					})
					let val = [];
					data.forEach(function(item) {
						item.label = item.name;
						let parent = map[item.pid];
						if (parent) {
							(parent.children || (parent.children = [])).push(item);
						} else {
							val.push(item);
						}
					})
					return val;
				}
				dept.forEach(function(item, index, dept) {
					// console.log(item)
					dept[index].menus = toTree(item.menus)
				})
				this.deptList = dept
				console.log(this.deptList)
			},
			toTree(data) {
				// console.log(data)
				// 删除 所有 children,以防止多次调用
				data.forEach(function(item) {
					delete item.children;
				})

				// 将数据存储为 以 id 为 KEY 的 map 索引数据列
				let map = {};
				data.forEach(function(item) {
					map[item.id] = item;
				})
				//console.log(map);
				let val = [];
				data.forEach(function(item) {
					// 以当前遍历项，的pid,去map对象中找到索引的id
					item.label = item.name;
					let parent = map[item.pid];
					// 好绕啊，如果找到索引，那么说明此项不在顶级当中,那么需要把此项添加到，他对应的父级中
					if (parent) {
						(parent.children || (parent.children = [])).push(item);
					} else {
						//如果没有在map中找到对应的索引ID,那么直接把 当前的item添加到 val结果集中，作为顶级
						val.push(item);
					}
				})
				return val;
			},
			changeDeptList(data) {
				let optionsObj = this.$options.methods
				data.forEach(function(item, index, deptList) {
					// console.log(item)
					deptList[index].menus = optionsObj.toTree(item.menus)
				})
				return data
				console.log(this.deptList)
			},
			async removeRightById(dept, mId) {
				const confirmResult = await this.$confirm('此操作将永久删除该权限, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).catch(err => err)
				if (confirmResult != 'confirm') {
					return this.$message.info("取消删除")
				}
				// console.log(mId)
				const {
					data: res
				} = await this.$http.delete(`dept/${dept.deptId}/menu/${mId}`)
				if (res.code != 100) {
					return this.$message.error('删除失败')
				}
				this.getDeptList()
				// console.log(res.extend.menu)
				// dept.children = this.$options.methods.toTree(res.extend.menu)
				// console.log(dept.children)
			},
			async showSetMenuDialog(menus) {
				this.deptId = menus.deptId
				const {
					data: res
				} = await this.$http.get('menus')
				if (res.code != 100) {
					return this.$message.error("获取部门列表失败")
				}
				let menu = res.extend.menus
				let optionsObj = this.$options.methods
				menu = optionsObj.toTree(menu)
				this.menuList = menu
				// console.log(menus)
				// console.log(this.menuList)
				this.getLeafKeys(menus, this.defKeys)
				// console.log(this.defKeys)
				this.setMenuDialogVisible = true
			},
			getLeafKeys(node, arr) {
				// console.log(node)
				node.menus.forEach(item => {
					if (item.id) {
						arr.push(item.id)
					} else if (item.children) {
						item.children.forEach(item => {
							if (item.id) {
								arr.push(item.id)
							}
							if (item.children) {
								item.children.forEach(item => {
									arr.push(item.id)
								})
							}
						})
					}
				})
			},
			setMenuDialogClose() {
				this.defKeys = []
			},
			async allotMenus() {
				const keys = [
					...this.$refs.treeRef.getCheckedKeys(),
					...this.$refs.treeRef.getHalfCheckedKeys()
				]
				const idStr = keys.join(',')
				const {
					data: res
				} = await this.$http.post(`/dept/${this.deptId}/menu`, {
					mIds: idStr
				})

				if (res.code != 100) {
					return this.$message.error("分配权限失败")
				}
				this.$message.success('分配权限成功')
				this.getDeptList()
				this.setMenuDialogVisible = false
			},
			async addDept() {
				const {
					data: res
				} = await this.$http.post('dept', this.addDeptForm)
				if (res.code != 100) {
					return this.$message.error("添加部门失败")
				}
				this.$message.success('添加部门成功')
				this.getDeptList()
				this.addDeptDialogVisible = false
			},
			async showEditDialog(id) {
				const {
					data: res
				} = await this.$http.get('dept/' + id)
				if (res.code != 100) {
					return this.$message.error("获取部门信息失败")
				}
				this.editDeptForm = res.extend.dept
				this.editDeptDialogVisible = true
			},
			async editDept() {
				const {
					data: res
				} = await this.$http.put('dept/' + this.editDeptForm.deptId, this.editDeptForm)
				if (res.code != 100) {
					return this.$message.error("添加部门失败")
				}
				this.$message.success("修改部门成功")
				this.editDeptDialogVisible = false
				this.getDeptList()
			},
			async deleteDeptById(id) {
				const confirmResult = await this.$confirm('此操作将永久删除该部门, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).catch(err => err)
				if (confirmResult != "confirm") {
					return this.$message.info("已取消删除")
				}
				const {
					data: res
				} = await this.$http.delete('dept/' + id)
				if (res.code != 100) {
					return this.$message.error("删除失败")
				}
				this.$message.success("删除成功")
				this.getDeptList()
			}
		},
	}
</script>

<style lang="less" scoped>
	.el-tag {
		margin: 7px;
	}

	.bdtop {
		border-top: 1px solid #eee;
	}

	.bdbottom {
		border-bottom: 1px solid #eee;
	}

	.vcenter {
		display: flex;
		align-items: center;
	}
</style>
