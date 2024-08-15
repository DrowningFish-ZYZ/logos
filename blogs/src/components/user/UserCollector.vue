<!-- 用户收藏页面 -->
<template>
    <div class="collector">
        <!-- 顶部 -->
        <TopModule
            ref="top"
            placeholder="搜索文章"
           :showSearch="true"
           :showWriteButton="false"
           :showGourpButton="true"
           :groups="groups"
           :sorts="sorts"
           :groupType="utils.groupTypes.COLLECTION_ARTICLE_GROUP"

           :@groupChange="groupChangeHandle"
           :@sortChange="sortChangeHandle"
           :@search="searchHandle"
           :@groupManager="utils.methods.gotoGroupManager"/>

        <!-- 显示文章 -->
        <ContentModule
            type="delCollect"
            :author="true"
            :sources="data.data"
            
            :@descHandle="utils.methods.gotoArticle"
            :@descAuthorHandle="utils.methods.gotoUserInfo"
            :@delCollect="delCollect"/>

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
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
    name: 'UserCollector',
    components: {
        ContentModule,
        PageModule,
        TopModule
    },

    mounted() {
        // 加载分组数据
        utils.request(
            "加载数据", {url: '/collectArticleGroups', method: 'get'},
            response => {
                for (let index = 0; index < response.data.length; index++) {
                    this.groups.push(response.data[index]);
                }
                this.pullCollectArticle()
            }
        )
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

    methods: {
        pullCollectArticle() {
            utils.request(
                "加载文章", {url: `/collectArticles/list`, method: 'get', params: this.pageQuery},
                response => {
                    this.data = response.data
                    utils.methods.formatDateList(this.data.data, 'createTime')
                }
            )
        },

        // 删除收藏
        delCollect(val) {
            ElMessageBox.confirm('确定取消收藏吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            })
            .then(() => {
                utils.request(
                    "加载文章", {url: `/collectArticles/${val}`, method: 'delete'},
                    response => {
                        ElMessage({
                            message: '删除成功',
                            type: "success"
                        })
                        this.pullCollectArticle()
                    }
                )
            })
        },

        // 分组改变时
        groupChangeHandle(val) {
            this.pageQuery.groupId = val
            this.pullCollectArticle()
        },

        // 排序改变时
        sortChangeHandle(val) {
            this.pageQuery.sort = val
            this.pullCollectArticle()
        },
    
        // 搜索
        searchHandle(val) {
            this.pageQuery.keyword = val
            this.pullCollectArticle()
        },

        // 页码更改触发
        pageHandle(val) {
            this.pageQuery.page = val
            this.pullCollectArticle()
        }
    }
}
</script>

<style scoped>
/* 整体 */
.collector {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}
</style>
  