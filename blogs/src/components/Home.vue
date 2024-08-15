<template>
    <div class="home">
        <TopModuleSmall
            :panes="panes"
            placeholder="搜索文章"
            :@change="tabsHandle"
            :@search="searchHandle"/>

        <!-- 显示文章 -->
        <ContentModule
            type="none"
            :sources="data.data"
            :author="true"
            
            :@descHandle="utils.methods.gotoArticle"
            :@descAuthorHandle="utils.methods.gotoUserInfo"/>

        <!-- 分页 -->
        <PageModule
            :@change="pageHandle"
            :total="data.total"
            :pageSize="data.size"/>
    </div>
</template>
  
<script> 
import utils from '@/utils';
import ContentModule from './module/ContentModule.vue';
import PageModule from './module/PageModule.vue';
import TopModuleSmall from './module/TopModuleSmall.vue';

export default {
    name: 'Home',
    components: {
        ContentModule,
        PageModule,
        TopModuleSmall
    },

    mounted() {
        this.pageQuery.sort = this.panes[0].name
        this.pullArticles()
    },

    data() {
        return {
            utils: utils,
            pageQuery: utils.pageQuery,
            data: utils.pageData,
            panes: [
                utils.panes.hotPan,
                utils.panes.timePan
            ],
        }
    },

    methods: {
        pullArticles() {
            utils.request(
                "加载数据", {url: '/articles', method: 'get', params: this.pageQuery},
                response => {
                    this.data = response.data
                    utils.methods.formatDateList(this.data.data, 'createTime')
                }
            )
        },

        // 选项卡更改触发
        tabsHandle(val) {
            this.pageQuery.sort = val
            this.pullArticles()
        },

        // 搜索
        searchHandle(val) {
           this.pageQuery.keyword = val
           this.pullArticles()
        },

        // 页码更改触发
        pageHandle(val) {
            this.pageQuery.page = val
            this.pullArticles()
        },
    }
}
</script>

<style scoped>
.home {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.top {
    padding: 0px 20px;
    display: flex;
    background-color: rgb(245, 245, 245);
    align-items: center;
}

.top-left {
    flex: 1;
    display: flex;
    flex-direction: row-reverse;
    margin-left: 20px;
}
</style>
  