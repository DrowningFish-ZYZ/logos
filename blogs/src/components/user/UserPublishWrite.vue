<template>
    <div class="publish-write">
        <div style="padding: 10px;height: 94%;">
            <MdEditor 
            style="height: 100%;"
            v-model="formData.content" 
            theme=""
            :toolbars = "toolbars"
            />
        </div>

        <div style="display: flex; flex-direction: row-reverse; padding-right: 10px;">
            <el-button-group class="ml-4">
                <el-button type="success" icon="Cloudy" @click="addArticleDialog = true">发布</el-button>
                <el-button type="danger" icon="Delete" @click="del">删除</el-button>
            </el-button-group>
        </div>

        <!-- 创建文章 -->
        <el-dialog v-model="addArticleDialog" title="创建文章" width="600">
            <el-form :model="formData">
                <el-form-item label="标题">
                    <el-input 
                        v-model="formData.title" 
                        autocomplete="off"
                        minlength="3"
                        maxlength="10"
                        show-word-limit />
                </el-form-item>
                <el-form-item label="组别">
                    <el-select v-model="formData.publishArticleGroupId" placeholder="选择文章分组">
                        <el-option 
                            v-for="group in groups" 
                            :key="group" 
                            :label="group.name" 
                            :value="group.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input 
                        v-model="formData.desc" 
                        type="textarea"
                        maxlength="100"
                        show-word-limit />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addArticleDialog = false">取消</el-button>
                    <el-button type="primary" @click="addArticle">创建</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
  
<script>
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import utils from '@/utils';
import { ElMessage } from 'element-plus';

export default {
    name: 'UserPublishWrite',
    components: {
        MdEditor
    },

    data() {
        return {
            addArticleDialog: false,
            groups: [],
            formData: {
                content: "",                    // 文章内容
                desc: "",                       // 文章简介
                title: "",                      // 文章标题
                publishArticleGroupId: null     // 文章分组
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

    mounted() {
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

    methods: {
        addArticle() {
            utils.request(
                "文章发布中", {url: '/publishArticles', method: 'post', data: this.formData},
                response => {
                    this.addArticleDialog = false
                    ElMessage({
                        message: '发布成功',
                        type: 'success'
                    })
                }
            )
        },

        del() {
            this.formData = {
                content: "",     // 文章内容
                desc: "",        // 文章简介
                title: "",       // 文章标题
            }
        }
    }
}
</script>

<style scoped>
.publish-write {
    width: 100%;
    height: 100%;
}
</style>
  