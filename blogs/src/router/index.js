import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import store from '@/store'

const router = createRouter({
    routes:routes,
    history:createWebHistory()
    //process.env.BASE_URL
})

// 路由权限拦截
router.beforeEach((to, from, next) => {
    // 如果是去的登录/注册页面
    if (to.name == 'login' || to.name == 'register') {
        // 查看是否已经登录过了
        let token = store.state.user.token
        if (token) {
            // 登录过了就别访问了，直接重定向到主页
            return router.replace({
                name: 'home'
            })
        }
        next()
    } else { next() }

    // 如果要去的是需要登陆后才能访问的页面
    if (to.meta.auth) {
        // 需要校验 token
        let token = store.state.user.token
        if (!token) {
            // 没有登录就让他重定向去登录界面
            return router.replace({
                name: 'login'
            })
        }
        next()
    } else { next() }
})

export default router