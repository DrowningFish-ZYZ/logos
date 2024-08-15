<template>
    <div class="userinfo">
        <div class="top">

            <span v-if="user.status == 0" class="freeze">
                <el-icon :size="19" style="margin-right: 2px;">
                <Warning /></el-icon>该账户封禁中
            </span>

            <div class="top-right">
                <el-breadcrumb separator="|">
                    <el-breadcrumb-item><el-statistic title="发布数" :value="publishCount" /></el-breadcrumb-item>
                    <el-breadcrumb-item><el-statistic title="获藏数" :value="collectorCount" /></el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="top-left">
                <div class="top-left-content">
                    <p>
                        <el-icon v-if="user.gender == 0" size="smaller" style="color: blue;"><Male /></el-icon>
                        <el-icon v-if="user.gender == 1" size="smaller" style="color: salmon;"><Female /></el-icon>
                        {{ user.username }}
                    </p>
                    <span>{{ user.desc }}</span>
                </div>
                <el-avatar style="width: 65px; height: 65px;" :src="user.headPortrait" />
            </div>
        </div>

        <TopModuleSmall
            placeholder="搜索他的作品"
            style="background-color: #f1f2f5;"
            :panes="panes"
            :@change="tabsHandle"
            :@search="searchHandle"/>

        <!-- 显示文章 -->
        <ContentModule
            type="del"
            :sources="data.data"
            :@descHandle="utils.methods.gotoArticle"/>

        <!-- 分页 -->
        <PageModule
            :@change="pageHandle"
            :total="data.total"
            :pageSize="data.size"/>
    </div>
</template>
  
<script>
import utils from '@/utils';
import { ref } from 'vue'
import { useTransition } from '@vueuse/core'
import ContentModule from '../module/ContentModule.vue';
import PageModule from '../module/PageModule.vue';
import TopModuleSmall from '../module/TopModuleSmall.vue';

export default {
    name: 'UserInfo',
    components: {
        ContentModule,
        PageModule,
        TopModuleSmall,
    },

    mounted() {
        this.pageQuery.sort = this.panes[0].name
        utils.request(
            "加载数据", {url: `/articles/user/${this.$store.state.descUserId}`, method: 'get', params: this.pageQuery},
            response => {
                this.data = response.data.data
                this.user = response.data.user
                utils.methods.formatDateList(this.data.data, 'createTime')

                let publishCountSource = ref(0)
                let collectorCountSource = ref(0)
                let publishCount = useTransition(publishCountSource, {duration: 1500,})
                let collectorCount = useTransition(collectorCountSource, {duration: 1500,})
                publishCountSource.value = response.data.allArticleCount
                collectorCountSource.value = response.data.allArticleCollectCount
                this.publishCount = publishCount
                this.collectorCount = collectorCount
            }
        )
    },

    data() {
        return {
            utils: utils,
            pageQuery: utils.pageQuery,
            data: utils.pageData,
            panes: [
                utils.panes.collectPan,
                utils.panes.commentPan,
                utils.panes.timePan
            ],
            user: {
                desc: '',
                gender: 0,
                headPortrait: '',
                status: 0,
                username: ''
            },
            publishCount: null,
            collectorCount: null,
        }
    },

    methods: {
        // 选项卡更改触发
        tabsHandle(val) {
            this.pageQuery.sort = val
            this.pullUserArticle()
        },

        // 搜索
        searchHandle(val) {
            this.pageQuery.keyword = val
            this.pullUserArticle()
        },

        // 页码更改触发
        pageHandle(val) {
            this.pageQuery.page = val
            this.pullUserArticle()
        },

        pullUserArticle() {
            utils.request(
                "加载数据", {url: `/articles/user/${this.$store.state.descUserId}`, method: 'get', params: this.pageQuery},
                response => {
                    this.data = response.data.data
                    utils.methods.formatDateList(this.data.data, 'createTime')
                }
            )
        }
    }
}
</script>

<style scoped>
.userinfo {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.top {
    background-color: white;
    display: flex;
    align-items: center;
    padding: 5px 20px;
    position: relative;
}

.freeze {
    position: absolute;
    right: 50%;
    color: gray;
    font-size: medium;
    display: flex;
    align-items: center;
    justify-content: center;
}

.top-left {
    display: flex;
    align-items: center;
}

.top-left > div {
    margin-right: 10px;
}

.top-left-content {
    display: flex;
    flex-direction: column;
    text-align: right;
}

.top-left-content > p {
    font-size: medium;
}

.top-left-content > span {
    font-size: smaller;
    color: gray;
    word-wrap: break-word;
    max-width: 300px;
}

.top-right {
    flex: 1;
    display: flex;
    flex-direction: row;
    text-align: center;
    color: gray;
}

.mid {
    display: flex;
    padding: 0px 20px;
    align-items: center;
}

.mid-right {
    flex: 1;
    display: flex;
    flex-direction: row-reverse;
}
</style>
  