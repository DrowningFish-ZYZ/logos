import router from "@/router"
import { ElMessage, ElLoading } from 'element-plus'
import axios from '@/http'
import store from "@/store";

function copyToClipboard(textToCopy, success, error) {
    // navigator clipboard 需要https等安全上下文
    if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(textToCopy)
            .then(success())
    } else {
        // 创建text area
        let textArea = document.createElement("textarea");
        textArea.value = textToCopy;
        // 使text area不在viewport，同时设置不可见
        textArea.style.position = "absolute";
        textArea.style.opacity = 0;
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        document.execCommand('copy') ? success() : error();
        textArea.remove();
    }
}

function copyToClipboardSimple(item) {
    copyToClipboard(
        item.src, 
        () => ElMessage(
            {
                message: '粘贴成功: ' + item.src,
                type: 'success',
                plain: true,
            }
        ),
        () => ElMessage(
            {
                message: '粘贴失败:',
                type: 'error',
                plain: true,
            }
        )
    )
}

function message(response) {
    if (response.status && response.status == 401) {
        ElMessage({
            message: '请先登录',
            type: 'warning'
        })
    } else if (response.code && response.code == 500) {
        ElMessage({
            message: '服务器无响应、请稍后重试',
            type: 'error'
        })
    } else {
        ElMessage({
            message: response.message,
            type: "warning"
        })
    }
}

function goto(name) { router.push({name}) }

const pageQuery = {
    page: 1,                    // 当前页
    size: 20,                   // 每页大小
    groupId: 0,                 // 分组
    sort: '',                   // 排序方式
    keyword: '',                // 关键字
}

const query = {
    keyword: '',
    sort: '',
    groupId: 0,
}

const pageData = {
    current: 0,                 // 当前页
    data: [],                   // 数据
    pages: 0,                   // 总页数
    size: 0,                    // 每页大小
    total: 0,                   // 总数据
}

const sorts = {
    noSort: {label: '不排序', value: 0}, 
    timeSort: {label: "按时间排序", value: "createTime"},
    collectSort: {label: '按收藏排序', value: "collectCount"},
    commentSort: {label: '按评论排序', value: "commentCount"}
}

const panes = {
    hotPan: {label: "全站热门", name: "hot"},
    collectPan: {label: '最多收藏', name: 'collectCount'}, 
    commentPan: {label: "最多评论", name: "commentCount"}, 
    timePan: {label: "最近发布", name: "createTime"}
}

const routers = {
    USER_INFO: 'user-info',
    USER_PUBLISH: 'user-publish',
    USER_PUBLISH_WRITE: 'user-publish-write',
    USER_PUBLISH_REWRITE: 'user-publish-rewrite',
    USER_FILES: 'user-files',
    USER_COLLECTOR: 'user-collector',
    USER_EDIT: 'user-edit',
    HOME: 'home',
    ARTICLE: 'article',
    REGISTER: 'register',
    LOGIN: 'login',
    GROUP_MANAGER: 'group-manager',
}

const groupTypes = {
    IMAGE_GROUP: 'image_group',
    PUBLISH_ARTICLE_GROUP: 'publish_article_group',
    COLLECTION_ARTICLE_GROUP: 'collection_article_group'
}

export default {
    // 路由路径
    routers: routers,
    // 分组类型
    groupTypes: groupTypes,
    // 不分也查询条件实体
    query: query,
    // 分页查询条件实体
    pageQuery: pageQuery,
    // 分页查询接收数据实体
    pageData: pageData,
    // 排序方式一
    sorts: sorts,
    // 排序方式二
    panes: panes,

    // 通用方法
    methods: {
        copyUrl: copyToClipboardSimple,
        gotoPage: goto,

        // 前往用户详情界面
        gotoUserInfo(val) {
            store.state.descUserId = val
            goto(routers.USER_INFO)
        },

        // 前往文章页面
        gotoArticle(val) {
            store.state.descActiveId = val
            goto(routers.ARTICLE)
        },

        // 前往编辑文章页面
        gotoEditArticle(val) {
            store.state.editArticleId = val
            goto(routers.USER_PUBLISH_REWRITE)
        },

        // 前往分组管理页面
        gotoGroupManager(val) {
            store.state.groupType = val
            goto(routers.GROUP_MANAGER)
        },

        
        /**
         * 格式化对象的date
         * @param {Array} array 
         * @param {String} key 对象的哪个属性是要格式化的
         */
        formatDateList(array, key) {
            for (let index = 0; index < array.length; index++) {
                array[index][key] = this.formatDate(array[index][key]) 
            }
        },

        /**
         * 格式化日期
         * @param {String} date : 2024-08-08T20:04:44
         */
        formatDate(date) {
            let result = date.split("T")[0].replace('-', '/')
            return result.replace('-', '/')
        }
    },

    /**
     * 请求耗时操作
     * @param {string} text 提示文本
     * @param {object} requestObj 请求对象 {url, method, data, headers}
     * @param {function} thenFun 成功后要执行的操作 res => {}
     */
    async request(text, requestObj, thenFun) {
        let loading = ElLoading.service({
            lock: true,
            text: text,
            background: 'rgba(0, 0, 0, 0.7)',
        })

       if (store.state.user.token) {
            if (requestObj['headers']) {
                // 有 headers，只对其添加 Authorization
                requestObj['headers']['Authorization'] = store.state.user.token
            } else {
                // 没有 headers
                requestObj['headers'] = {'Authorization': store.state.user.token}
            }
       }

        // 发起请求
        axios.request(requestObj)
        .then(res => {
            if (res.code == 200) thenFun(res)
            else message(res)
            loading.close()
        })
        .catch(error => {
            loading.close()
            message(error)
        })
    },
}