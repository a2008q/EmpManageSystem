<template>
	<div>
		<!-- 面包屑导航 -->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>数据统计</el-breadcrumb-item>
			<el-breadcrumb-item>数据报表</el-breadcrumb-item>
		</el-breadcrumb>

		<el-row :gutter="20">
			<el-col :span="12">
				<el-card>
					<div id="main" style="height: 350px;"></div>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card>
					<div id="main1" style="height: 350px;"></div>
				</el-card>
			</el-col>
		</el-row>


	</div>
</template>

<script>
	import * as echarts from 'echarts'
	export default {
		data() {
			return {
				gender: [],
				cDept: []
			}
		},
		async mounted() {
			let myChart1 = echarts.init(document.getElementById('main'));
			let myChart2 = echarts.init(document.getElementById('main1'));
			const {
				data: res1
			} = await this.$http.get('gender')
			if (res1.code != 100) {
				return this.$message.error("获取男女比例数据失败")
			}
			let temp = res1.extend.gender
			this.gender = [{
				value: temp.女,
				name: Object.keys(temp)[0]
			}, {
				value: temp.男,
				name: Object.keys(temp)[1]
			}]
			const {
				data: res2
			} = await this.$http.get('cdept')
			if (res2.code != 100) {
				return this.$message.error("获取部门人数数据失败")
			}
			this.cDept = res2.extend.cdept
			let option1 = {
				title: {
					text: '男女比例',
					left: 'center'
				},
				tooltip: {
					trigger: 'item'
				},
				legend: {
					orient: 'vertical',
					left: 'left',
				},
				series: [{
					type: 'pie',
					radius: '50%',
					data: this.gender,
					emphasis: {
						itemStyle: {
							shadowBlur: 10,
							shadowOffsetX: 0,
							shadowColor: 'rgba(0, 0, 0, 0.5)'
						}
					}
				}]
			};
			let option2 = {
				title: {
					text: '部门人数',
					left: 'center'
				},
				tooltip: {
					trigger: 'item'
				},
				legend: {
					orient: 'vertical',
					left: 'left',
				},
				series: [{
					type: 'pie',
					radius: '50%',
					data: this.cDept,
					emphasis: {
						itemStyle: {
							shadowBlur: 10,
							shadowOffsetX: 0,
							shadowColor: 'rgba(0, 0, 0, 0.5)'
						}
					}
				}]
			};
			myChart1.setOption(option1);
			myChart2.setOption(option2);
		}
	}
</script>

<style lang="less" scoped>
</style>
