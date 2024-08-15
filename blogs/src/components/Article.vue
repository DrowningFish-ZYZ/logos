<!-- 文章模板 -->
<template>
    <div class="article">
        <!-- 左边的文章显示 -->
        <div class="article-left">
            <!-- 显示文章 -->
            <MdPreview 
                style="flex: 1;"
                :modelValue="article.content"
                :editorId="id"/>
            <!-- 目录 -->
            <MdCatalog 
            style="background-color: white; padding: 10px; width: 140px; font-size: smaller;"
            :editorId="id"/> 
        </div>

        <!-- 右边信息 -->
        <div class="right">
            <!-- 顶部 -->
            <div class="right-top">
                <!-- 作者头像 -->
                <div class="right-top-photo" @click="utils.methods.gotoUserInfo(article.user.id)" >
                    <el-avatar :src="article.user.headPortrait"/>
                    <span>{{ article.user.username }}</span>
                </div>
                <!-- 文章信息 -->
                <div class="right-top-info">
                    <span><el-icon><ChatDotRound /></el-icon> {{ article.commentCount }}</span>
                    <!-- 已收藏 -->
                    <span class="right-top-info-collect" v-if="isCollectArticle.collected" @click="cancleCollection">
                        <el-icon><StarFilled /></el-icon> 
                        {{ article.collectCount }}
                    </span>
                    <!-- 未收藏 -->
                    <span class="right-top-info-collect" v-if="!isCollectArticle.collected"  @click="openCollectArticleDialog">
                        <el-icon><Star /></el-icon> 
                        {{ article.collectCount }}
                    </span>
                    <span><el-icon><Clock /></el-icon> {{ article.createTime }}</span>
                </div>
            </div>

            <!-- 中间：显示评论 -->
            <div class="right-mid">
                <div class="right-mid-container">
                    <el-card class="right-mid-card" shadow="always" v-for="comm in comments" :key="comm">
                        <div class="right-mid-card-content">
                            <div style="position: relative;">
                                <div class="right-mid-card-content-photo" @click="utils.methods.gotoUserInfo(comm.user.id)" >
                                    <el-avatar :src="comm.user.headPortrait"/>
                                    <span style="margin-left: 5px;">{{ comm.user.username }}</span>
                                </div>
                            </div>
                            <div class="right-mid-card-content-comment">
                               {{  comm.content }}
                            </div>
                            <span class="right-mid-card-content-time">{{ comm.createTime }}</span>
                        </div>
                    </el-card>
                </div>
            </div>

            <!-- 底部 -->
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <div class="mt-4">
                    <el-input
                    v-model="comment.content"
                    style="max-width: 800px"
                    placeholder="留下你的评论吧"
                    class="input-with-select"
                    maxlength="50"
                    show-word-limit>
                    <template #append>
                        <el-button @click="putComment">Enter</el-button>
                    </template>
                    </el-input>
                </div>
            </div>
        </div>

        <!-- 收藏文章 -->
        <el-dialog v-model="collectArticleDialog" title="收藏文章" width="600">
            <el-form :model="collectArticleDialog">
                <el-form-item label="组别">
                    <el-select v-model="collectArticleFormData.groupId" placeholder="选择图片分组">
                        <el-option 
                            v-for="group in collectGroup" 
                            :key="group" 
                            :label="group.name" 
                            :value="group.id"/>
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="collectArticleDialog = false">取消</el-button>
                    <el-button type="primary" @click="collectArticle">收藏</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
  
<script>
import utils from '@/utils';
import { ElMessage, ElMessageBox } from 'element-plus';
import { MdPreview, MdCatalog  } from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';

export default {
    name: 'Article',
    components: {
        MdPreview,
        MdCatalog 
    },

    mounted() {
        this.pullArticle()
        if (this.$store.state.user.token) this.pullCollectGroup()
    },

    data() {
        return {
            utils: utils,
            isCollectArticle: {
                collected: false,
                groupId: '',
                articleId: ''
            },
            collectArticleDialog: false,
            collectGroup: [],
            collectArticleFormData: {
                groupId: '',
                articleId: ''
            },

            comments: [],
            comment: {
                content: '',
                articleId: ''
            },

            id: 'preview-only',
            article: {
                id: 0,
                title: '',
                desc: '',
                content: '',
                createTime: '',
                commentCount: 0,
                collectCount: 0,
                publishArticleGroupId: 0,
                user: {
                    id: 0,
                    desc: '',
                    gender: 0,
                    headPortrait: '',
                    status: 0,
                    username: ''
                }
            },
        }
    },

    methods: {
        pullComments() {
            utils.request(
                "加载评论", {url: `/comments/list/${this.article.id}`, method: 'get'},
                response => {
                    this.comments = response.data
                    utils.methods.formatDateList(this.comments, 'createTime')
                }
            )
        },

        pullCollectGroup() {
            utils.request(
                "加载收藏分组", {url: `/collectArticleGroups`, method: 'get'},
                response => {
                    this.collectGroup = response.data
                }
            )
        },

        pullArticle() {
            utils.request(
                "加载文章", {url: `/articles/${this.$store.state.descActiveId}`, method: 'get'},
                response => {
                    this.article = response.data;
                    this.article.createTime = utils.methods.formatDate(this.article.createTime)
                    this.collectArticleFormData.articleId = this.article.id
                    this.comment.articleId = this.article.id
                    if (this.$store.state.user.token) this.pullIsCollectArticle()
                    this.pullComments()
                }
            )
        },

        pullIsCollectArticle() {
            utils.request(
                "当前文章是否被我收藏", {url: `/collectArticles`, method: 'get', params: {articleId: this.article.id}},
                response => {
                    this.isCollectArticle = response.data
                }
            )
        },

        openCollectArticleDialog() {
            if (this.$store.state.user.token) {
                this.collectArticleDialog = true
            } else {
                ElMessage({
                    message: "请先登录",
                    type: "warning"
                })
            }
        },

        // 发送评论
        putComment() {
            utils.request(
                "发送评论", {url: `/comments`, method: 'post', data: this.comment},
                response => {
                    this.pullArticle()
                    this.pullComments()
                }
            )
        },

        // 收藏文章
        collectArticle() {
            utils.request(
                "收藏中", {url: `/collectArticles`, method: 'post', data: this.collectArticleFormData},
                response => {
                    this.pullIsCollectArticle()
                    this.collectArticleDialog = false
                    this.pullArticle()
                    ElMessage({
                        message: "收藏成功",
                        type: "success"
                    })
                }
            )
        },

        // 取消收藏
        cancleCollection() {
            ElMessageBox.confirm('确定取消收藏吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            })
            .then(() => {
                utils.request(
                    "取消收藏", {url: `/collectArticles`, method: 'delete', data: this.isCollectArticle},
                    response => {
                        this.isCollectArticle = response.data
                        this.pullArticle()
                        ElMessage({
                            message: '取消成功',
                            type: 'success'
                        })
                    }
                )
            })
        }
    }
}
</script>

<style scoped>
/* 整体 */
.article {
    width: 100%;
    height: 100%;
    display: flex;
}

.article-left {
    display: flex;
    flex: 1;
}

/* 右边整体 */
.right {
    display: flex; 
    flex-direction: column; 
    padding-left: 10px; 
    padding-top: 10px; 
    min-width: 300px;
    background-color: #f1f2f5;
}

/* 右边头部 */
.right-top {
    display: flex;
}

.right-top-photo {
    display: flex; 
    flex-direction: row; 
    align-items: center;
    justify-content: center;
}

.right-top-photo > span {
    font-size: 14px; 
    margin-left: 5px; 
    letter-spacing: 1px;
}

.right-top-photo:hover {
    color: salmon;
    cursor: pointer;
}

.right-top-info {
    display: flex;
    flex: 1;
    justify-content: flex-start;
    flex-direction: row-reverse;
    align-items: center;
}

.right-top-info > span {
    margin: 0px 10px;
    display: flex;
    align-items: center;
    font-size: smaller;
}

.right-top-info-collect:hover {
    cursor: pointer;
    color: salmon;
}

/* 右边中间 */
.right-mid {
    margin-top: 20px; 
    overflow: auto;
}

.right-mid-container {
    display: flex; 
    flex-direction: column; 
    overflow: auto; 
    align-items: center;
    padding: 0px 10px;
}

.right-mid-card {
    width: 300px;
    margin-bottom: 10px;
}

.right-mid-card-content {
    display: flex; 
    flex-direction: column; 
    /* align-items: center; */
    position: relative;
}

.right-mid-card-content-time {
    position: absolute;
    font-size: smaller;
    color: gray;
    top: -16px;
    right: -16px;
}

.right-mid-card-content-photo {
    display: flex; 
    align-items: center; 
    padding-right: 10px;
    margin-bottom: 5px;
    font-size: smaller;
}

.right-mid-card-content-photo:hover {
    color: salmon;
    cursor: pointer;
}

.right-mid-card-content-comment {
    font-size: smaller;
    overflow: hidden;
    margin-left: 5px;
    word-wrap: break-word;
    background-color: #e4e3e3a5;
    padding: 5px;
    border-radius: 5px;
    margin-top: 5px;
    color: rgb(78, 78, 78);
}
</style>
  