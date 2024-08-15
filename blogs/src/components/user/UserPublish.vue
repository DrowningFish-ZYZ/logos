<!-- 用户发布页面 -->
<template>
    <div class="publish">
        <TopModule
            placeholder="搜索文章"
           :showSearch="true"
           :showWriteButton="true"
           :showGourpButton="true"
           :groups="groups"
           :sorts="sorts"
           :groupType="utils.groupTypes.PUBLISH_ARTICLE_GROUP"

           :@groupChange="groupChangeHandle"
           :@sortChange="sortChangeHandle"
           :@search="searchHandle"
           :@groupManager="utils.methods.gotoGroupManager"/>

        <!-- 显示文章 -->
        <ContentModule
            type="edit"
            :sources="data.data"
            :author="false"
            
            :@editHandle="utils.methods.gotoEditArticle"
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
import ContentModule from '../module/ContentModule.vue';
import PageModule from '../module/PageModule.vue';
import TopModule from '../module/TopModule.vue';

export default {
    name: 'UserPublish',
    components: {
        ContentModule,
        PageModule,
        TopModule
    },

    data() {
        return {
            utils: utils,
            pageQuery: utils.pageQuery,
            data: utils.pageData,
            sorts: [
                utils.sorts.noSort,
                utils.sorts.collectSort,
                utils.sorts.commentSort,
                utils.sorts.timeSort
            ],
            groups: [{id: 0, name: '所有文章'}],
        }
    },

    mounted() {
        // 加载分组数据
        utils.request(
            "加载数据", {url: '/publishArticleGroups', method: 'get'},
            response => {
                for (let index = 0; index < response.data.length; index++) {
                    this.groups.push(response.data[index]);
                }
                this.pullPublishArticles()
            }
        )
    },

    methods: {
        // 拉取文章数据
        pullPublishArticles() {
            utils.request(
                "加载文章", {url: `/publishArticles`, method: 'get', params: this.pageQuery},
                response => {
                    this.data = response.data
                    utils.methods.formatDateList(this.data.data, 'createTime')
                }
            )
        },

        // 分组改变时
        groupChangeHandle(val) {
            this.pageQuery.groupId = val
            this.pullPublishArticles()
        },

        // 排序改变时
        sortChangeHandle(val) {
            this.pageQuery.sort = val
            this.pullPublishArticles()
        },
    
        // 搜索
        searchHandle(val) {
            this.pageQuery.keyword = val
            this.pullPublishArticles()
        },

        // 页码更改触发
        pageHandle(val) {
            this.pageQuery.page = val
            this.pullPublishArticles()
        }
    }
}
</script>

<style scoped>
/* 整体 */
.publish {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}
</style>
  