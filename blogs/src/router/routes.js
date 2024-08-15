const routes = [
    // 主页模块
    { path:'/', redirect:'/home' }, // 默认路径重定向至首页
    { path:'/home', name:'home', component: () => import('@/components/Home.vue') },

    // 登录模块
    { path: '/register', name: 'register', component: () => import('@/components/login/Register.vue') },
    { path: '/login', name: 'login', component: () => import('@/components/login/Login.vue') },

    // 用户模块
    { 
        path:'/user/collector', name:'user-collector', 
        component: () => import('@/components/user/UserCollector.vue'),
        meta: {auth: true}  // 这个表示当前路由必须登录才能访问
    },
    { 
        path:'/user/publish', name:'user-publish', 
        component: () => import('@/components/user/UserPublish.vue'),
        meta: {auth: true}
    },
    { 
        path:'/user/publish/write', name:'user-publish-write', 
        component: () => import('@/components/user/UserPublishWrite.vue'),
        meta: {auth: true}
    },
    { 
        path:'/user/publish/rewrite', name:'user-publish-rewrite', 
        component: () => import('@/components/user/UserPublishReWrite.vue'),
        meta: {auth: true}
    },
    { 
        path:'/user/files', name:'user-files', 
        component: () => import('@/components/user/UserFiles.vue'),
        meta: {auth: true}
    },
    { 
        path:'/user/edit', name:'user-edit', 
        component: () => import('@/components/user/UserEdit.vue'),
        meta: {auth: true}
    },
    { path:'/user/info', name:'user-info', component: () => import('@/components/user/UserInfo.vue') },

    // 文章模块
    { path:'/article', name:'article', component: () => import('@/components/Article.vue') },

    // 分组管理模块
    { 
        path: '/group/manager', name: 'group-manager', 
        component: () => import('@/components/GroupManager.vue'),
        meta: {auth: true}
    }
]

export default routes