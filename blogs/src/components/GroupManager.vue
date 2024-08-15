<!-- 分组管理界面 -->
<template>
    <div class="group-manager">
        <div style="width: 100%;">
            <el-card>
                <el-table
                    stripe
                    show-summary
                    :data="tableData"
                    :default-sort="{ prop: 'date', order: 'descending' }"
                    style="width: 100%"
                    max-height="600">

                    <el-table-column prop="name" label="分组名" width="180" />
                    <el-table-column prop="count" label="数量" sortable width="180" />
                    <el-table-column prop="desc" show-overflow-tooltip label="描述" />
                    <el-table-column prop="createTime" label="创建时间" sortable width="180" />

                    <el-table-column align="right">
                        <template #header>
                            <el-button type="success" icon="Plus" @click="addGroupDialog = true">新建分组</el-button>
                        </template>
                        <template #default="scope">
                            <el-button icon="Edit" circle @click="getGroupByID(scope.row.id)"/>
                            <el-button type="danger" icon="Close" circle @click="delGroupByID(scope.row.id)"/>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
        </div>

        <!-- 新建分组 -->
        <el-dialog v-model="addGroupDialog" title="新建分组" width="600">
            <el-form :model="addGroupForm">
                <el-form-item label="组名">
                    <el-input 
                        v-model="addGroupForm.name" 
                        autocomplete="off"
                        minlength="3"
                        maxlength="10"
                        show-word-limit />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input 
                        v-model="addGroupForm.desc" 
                        type="textarea"
                        minlength="3"
                        maxlength="20"
                        show-word-limit />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addGroupDialog = false">取消</el-button>
                    <el-button type="primary" @click="addGroup">创建</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 编辑分组 -->
        <el-dialog v-model="editGroupDialog" title="分组编辑" width="600">
            <el-form :model="editGroupForm">
                <el-form-item label="组名">
                    <el-input 
                        v-model="editGroupForm.name" 
                        autocomplete="off" 
                        minlength="3"
                        maxlength="10"
                        show-word-limit />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input 
                        v-model="editGroupForm.desc" 
                        type="textarea"
                        minlength="3"
                        maxlength="20"
                        show-word-limit />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="editGroupDialog = false">取消</el-button>
                    <el-button type="warning" @click="editGroup">修改</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import utils from '@/utils';
import { ElMessageBox } from 'element-plus';

export default {
    data() {
        return {
            addGroupDialog: false,
            editGroupDialog: false,

            tableData: null,

            editGroupID: null,
            addGroupForm: {
                name: '',
                desc: '',
            },
            editGroupForm: {
                name: '分组名',
                desc: '这是一段分组描述'
            }
        }
    },

    methods: {
        // 获取当前分组的 URL
        getUrl() {
            let url = ''
            if (this.$store.state.groupType == utils.groupTypes.IMAGE_GROUP) url = '/imageGroups'
            if (this.$store.state.groupType == utils.groupTypes.PUBLISH_ARTICLE_GROUP) url = '/publishArticleGroups'
            if (this.$store.state.groupType == utils.groupTypes.COLLECTION_ARTICLE_GROUP) url = '/collectArticleGroups'
            return url
        },

        // 拉取分组数据
        pullGroup() {
            utils.request(
                "加载数据", {url: this.getUrl(), method: 'get'},
                response => {
                    this.tableData = response.data;
                    utils.methods.formatDateList(this.tableData, 'createTime')
                }
            )
        },

        // 获取对应ID的分组
        getGroupByID(id) {
            utils.request(
                "获取中", {url: this.getUrl() + '/' + id, method: 'get'},
                response => {
                    this.editGroupID = id
                    this.editGroupDialog = true
                    this.editGroupForm = response.data
                }
            )
        },

        // 根据ID删除分组
        delGroupByID(id) {
            ElMessageBox.confirm('该操作会删除分组下的所有数据，你确定吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            })
            .then(() => {
                utils.request(
                    "删除中", {url: this.getUrl() + '/' + id, method: 'delete'},
                    response => {
                        this.pullGroup()
                    }
                )
            })
        },

        // 创建分组
        addGroup() {
            utils.request(
                "创建中", {url: this.getUrl(), method: 'post', data: this.addGroupForm},
                response => {
                    this.pullGroup()
                    this.addGroupDialog = false
                }
            )
        },

        // 编辑分组
        editGroup() {
            utils.request(
                "修改中", {url: this.getUrl() + '/' + this.editGroupID, method: 'put', data: this.editGroupForm},
                response => {
                    this.pullGroup()
                    this.editGroupDialog = false
                }
            )
        }
    },

    created() {
        this.pullGroup()
    }
}
</script>

<style scoped>
.group-manager {
    width: 100%;
    height: 100%;
    padding: 10px;
}
</style>