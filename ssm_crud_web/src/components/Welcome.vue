<template>
	<div>
		<h3>
			欢迎 {{name}}登录
		</h3>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				name: window.sessionStorage.getItem("name")
			}
		}

	}
</script>

<style>
</style>
