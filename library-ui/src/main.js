import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import locale from 'element-plus/es/locale/lang/zh-cn'; // 引入中文语言包

import '@/assets/global.css'
import '@/assets/style.css'
import '@/assets/icon/iconfont.js' // 图标
import '@/assets/icon/iconfont.css'
import * as ElIconModules from '@element-plus/icons'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)
const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)

app.use(ElementPlus, {locale});
for(let iconName in ElIconModules){
    app.component(iconName,ElIconModules[iconName])
}
app.mount('#app')
