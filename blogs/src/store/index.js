import { createStore } from 'vuex'

export default createStore({
    state: {
        // 当前登录用户
        user: {id: '', token: '', username: '', phone: '', desc: '', headPortrait: ''},
        // 分组管理类型
        groupType: null,
        // 要查看的文章
        descActiveId: null,
        // 要编辑的文章
        editArticleId: null,
        // 要查看的作者详情
        descUserId: null
    }
})