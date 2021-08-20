import Vue from 'vue'
import App from './App.vue'
// import './plugins/element.js'
import router from './router'
import './assets/css/global.css'
import './plugins/element.js'
import './assets/fonts/iconfont.css'
import axios from 'axios'
Vue.config.productionTip = false
axios.defaults.baseURL='/api/'
Vue.prototype.$http = axios

new Vue({
  router,
  render: function (h) { return h(App) }
}).$mount('#app')

