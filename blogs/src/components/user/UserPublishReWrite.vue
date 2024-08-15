<!-- 用户重新编辑文章 -->
<template>
    <div class="publish-rewrite">
        <div style="padding: 10px;height: 94%;">
            <MdEditor 
            style="height: 100%;"
            v-model="article.content" 
            :toolbars = "toolbars"/>
        </div>

        <div style="display: flex; flex-direction: row-reverse; padding-right: 10px; position: relative;">
            <el-button-group class="ml-4">
                <el-button type="success" icon="Check" @click="updateArticle">确认修改</el-button>
                <el-button type="warning" icon="Edit" @click="editArticleDialog = true">编辑信息</el-button>
                <el-button type="danger" icon="Delete" @click="delArticle">删除文章</el-button>
            </el-button-group>
            <span class="time">{{ article.createTime }}</span>
        </div>

        <!-- 编辑信息 -->
        <el-dialog v-model="editArticleDialog" title="创建文章" width="600">
            <el-form :model="article">
                <el-form-item label="标题">
                    <el-input 
                        v-model="article.title" 
                        autocomplete="off"
                        minlength="3"
                        maxlength="10"
                        show-word-limit />
                </el-form-item>
                <el-form-item label="组别">
                    <el-select v-model="article.publishArticleGroupId" placeholder="选择文章分组">
                        <el-option 
                            v-for="group in groups" 
                            :key="group" 
                            :label="group.name" 
                            :value="group.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input 
                        v-model="article.desc" 
                        type="textarea"
                        maxlength="100"
                        show-word-limit />
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import utils from '@/utils';
import { ElMessage, ElMessageBox } from 'element-plus';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

export default {
    name: 'UserPublishReWrite',
    components: {
        MdEditor
    },

    mounted() {
        this.pullArticle()
        // 加载分组数据
        utils.request(
            "加载数据", {url: '/publishArticleGroups', method: 'get'},
            response => {
                for (let index = 0; index < response.data.length; index++) {
                    this.groups.push(response.data[index]);
                }
            }
        )
    },

    data() {
        return {
            editArticleDialog: false,
            groups: [],
            article: {
                id: 0,
                title: '',
                desc: '',
                content: '',
                createTime: '',
                commentCount: 0,
                collectCount: 0,
                publishArticleGroupId: 0
            },
            toolbars: [
                'bold',
                'underline',
                'italic',
                '-',
                'title',
                'strikeThrough',
                'sub',
                'sup',
                'quote',
                'unorderedList',
                'orderedList',
                'task',
                '-',
                'codeRow',
                'code',
                'link',
                'table',
                'mermaid',
                'katex',
                '-',
                '=',
                'pageFullscreen',
                'fullscreen',
                'preview',
                'previewOnly',
                'htmlPreview',
                'catalog',
            ],
        }
    },

    methods: {
        pullArticle() {
            utils.request(
                "加载文章", {url: `/publishArticles/${this.$store.state.editArticleId}`, method: 'get'},
                response => {
                    this.article = response.data
                    this.article.createTime = utils.methods.formatDate(this.article.createTime)
                }
            )
        },

        updateArticle() {
            utils.request(
                "修改文章", {url: `/publishArticles/${this.$store.state.editArticleId}`, method: 'put', data: this.article},
                response => {
                    ElMessage({
                        message: response.message,
                        type: "success"
                    })
                    this.pullArticle()
                }
            )
        },

        delArticle() {
            ElMessageBox.confirm('确定删除这篇文章吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            })
            .then(() => {
                utils.request(
                    "删除中", {url: `/publishArticles/${this.article.id}`, method: 'delete'},
                    response => {
                        utils.methods.gotoPage(utils.routers.USER_PUBLISH)
                    }
                )
            })
        }
    }
}
</script>

<style scoped>
.publish-rewrite {
    width: 100%;
    height: 100%;
}

.time {
    position: absolute;
    left: 15px;
    font-size: smaller;
    color: gray;
}
</style>