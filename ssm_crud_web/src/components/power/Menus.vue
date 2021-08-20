<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>权限管理</el-breadcrumb-item>
			<el-breadcrumb-item>权限列表</el-breadcrumb-item>
		</el-breadcrumb>
		<!-- 卡片视图 -->
		<el-card>
			<el-table :data="menusList" border stripe>
				<el-table-column label="Id" prop="id"></el-table-column>
				<el-table-column label="权限名称" prop="name"></el-table-column>
				<el-table-column label="路径" prop="url"></el-table-column>
				<el-table-column label="权限等级" prop="pid">
					<template slot-scope="scope">
						<el-tag v-if="scope.row.pid===0">一级</el-tag>
						<el-tag type="success" v-else-if="secondMenu(scope.row.pid)">
							二级</el-tag>
							<el-tag type="warning" v-else>三级</el-tag>
					</template>
				</el-table-column>
			</el-table>
		</el-card>
	</div>
</template>

<script>
</script>

<script>
	export default {
		data() {
			return {
				menusList: []
			}
		},
		created() {
			this.getMenusList()
		},
		methods: {
			async getMenusList() {
				const {
					data: res
				} = await this.$http.get('menus')
				// console.log(res)
				if (res.code != 100) {
					return this.$message.error("获取权限列表失败")
				}
				this.menusList = res.extend.menus
			},
			secondMenu(pid) {
				let result = false
				if (pid > 0) {
					this.menusList.forEach(function(item, index) {
						if (pid == item.id && item.pid == 0) {
							// console.log(item)
							result = true
						}
					})
				}
				return result
			}
		}
	}
</script>

<style lang="less" scoped>
</style>
