<template>
	<el-container class="home-container">
		<el-header>
			<div>
				<span>员工管理系统</span>
			</div>
			<el-button type="info" @click='logout'>退出</el-button>
		</el-header>
		<el-container>
			<!-- 侧边栏 -->
			<el-aside :width="isCollapse?'64px':'200px'">
				<div class="toggle-button" @click="toggleCollapse">|||</div>
				<!-- 侧边栏菜单区 -->
				<el-menu background-color="#333744" text-color="#fff" active-text-color="#409eff" unique-opened
					:collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
					<el-submenu :index="item.id+''" v-for="item in menulist" :key="item.id" v-if="item.pid==0">
						<template slot="title">
							<i :class="iconObj[item.id]"></i>
							<span>{{item.name}}</span>
						</template>
						<el-menu-item :index="subitem.url" v-for="subitem in menulist" :key="subitem.id"
							v-if="subitem.pid==item.id" @click="saveNavState(subitem.url)">
							<i class="el-icon-menu"></i>
							<span>{{subitem.name}}</span>
						</el-menu-item>
					</el-submenu>
				</el-menu>
			</el-aside>
			<!-- 右侧内容主体 -->
			<el-main>
				<router-view>
				</router-view>
			</el-main>
		</el-container>
	</el-container>
</template>

<script>
	export default {
		created() {
			this.getMenu();
			this.activePath = window.sessionStorage.getItem('activePath')
		},
		data() {
			return {
				dId: window.sessionStorage.getItem("dId"),
				menulist: [],
				isCollapse: false,
				iconObj: {
					'1': 'iconfont icon-user',
					'4': 'iconfont icon-tijikongjian',
					'10':'iconfont icon-baobiao'
				},
				activePath:''
			}
		},
		methods: {
			async getMenu() {
				const {
					data: res
				} = await this.$http.get("menu/" + this.dId);
				if (res.code == 100) {
					// console.log(res.extend)
					this.menulist = res.extend.menu
				} else {
					return this.$message.error("获取菜单失败");
				}
			},
			logout() {
				window.sessionStorage.clear()
				this.$router.push("/login")
			},
			toggleCollapse() {
				this.isCollapse = !this.isCollapse
			},
			saveNavState(activePath){
				window.sessionStorage.setItem("activePath",activePath)
				this.activePath = activePath
			}
		}
	};
</script>

<style lang="less" scoped>
	.home-container {
		height: 100%;
	}

	.el-header {
		background-color: #373d41;
		display: flex;
		justify-content: space-between;
		padding-left: 0;
		align-items: center;
		color: #fff;
		font-size: 20px;

		>div {
			display: flex;
			align-items: center;

			span {
				margin-left: 15px;
			}
		}
	}

	.el-aside {
		background-color: #333744;

		.el-menu {
			border-right: none;
		}
	}

	.el-main {
		background-color: #eaedf1;
	}

	.toggle-button {
		background-color: #4a5064;
		font-size: 10px;
		line-height: 24px;
		color: #fff;
		text-align: center;
		letter-spacing: 0.2em;
		cursor: pointer;
	}

	.iconfont {
		margin-right: 10px;
	}
</style>
