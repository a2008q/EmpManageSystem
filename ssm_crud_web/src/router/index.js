import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import Home from '../components/Home.vue'
import Welcome from '../components/Welcome.vue'
import Users from '../components/user/Users.vue'
import Departments from '../components/power/Departments.vue'
import Menus from '../components/power/Menus.vue'
import Report from '../components/report/Report.vue'

Vue.use(VueRouter)

const routes = [{
		path: '/',
		redirect: "/login"
	},
	{
		path: '/login',
		component: Login
	},
	{
		path: '/home',
		component: Home,
		redirect: "/welcome",
		children: [{
				path: '/welcome',
				component: Welcome
			}, {
				path: '/emp',
				component: Users
			}, {
				path: '/department',
				component: Departments
			},
			{
				path: '/menu',
				component: Menus
			},
			{
				path: '/reports',
				component: Report
			}
		]
	},
]

const router = new VueRouter({
	mode: 'history',
	routes
})

router.beforeEach((to, from, next) => {
	if (to.path == '/login') return next();
	const name = window.sessionStorage.getItem("name")
	if (!name) return next('/login')
	next()
})

export default router
