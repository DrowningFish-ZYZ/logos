<template>
    <div class="files">
        <!-- 顶部 -->
        <TopModule
            ref="top"
            placeholder="搜索图片"
           :showSearch="true"
           :showWriteButton="false"
           :showGourpButton="true"
           :showUploadButton="true"
           :groups="groups"
           :sorts="sorts"
           :groupType="utils.groupTypes.IMAGE_GROUP"

           :@groupChange="groupChangeHandle"
           :@sortChange="sortChangeHandle"
           :@search="searchHandle"
           :@upload="uploadImage"
           :@groupManager="utils.methods.gotoGroupManager"/>

        <el-alert title="双击图片可以复制链接" type="info" show-icon />

        <!-- 显示图片 -->
        <div class="content">
            <Waterfall :list="list" :width="285">
                <template #item="{ item, url, index }">
                    <el-card class="card" shadow="hover">
                        <el-icon class="del" @click="delHandle(item.id)"><Close /></el-icon>
                        <LazyImg :url="url" @dblclick="utils.methods.copyUrl(item)" class="image"/>
                        <p class="text">
                            <span style="text-align: left;">{{ item.name }}</span>
                            <span style="text-align: right; color: gray;">{{ item.createTime }}</span>
                        </p>
                    </el-card>
                </template>
            </Waterfall>
        </div>

        <!-- 空空如也 -->
        <el-empty 
            v-if="list.length == 0" 
            description="空空如也" 
            style="width: 100%; height: 100%; background-color: white; letter-spacing: 1px"
            :image-size="200" />

        <!-- 回到顶部 -->
        <el-backtop :bottom="50" target=".content" style="color: salmon; padding: 5px;"/>

        <!-- 上传图片 -->
        <el-dialog v-model="uploadImageDialog" title="上传图片" width="600">
            <el-form :model="uploadImageForm">
                <el-form-item label="名字">
                    <el-input v-model="uploadImageForm.name" autocomplete="off" />
                </el-form-item>
                <el-form-item label="组别">
                    <el-select v-model="uploadImageForm.groupID" placeholder="选择图片分组">
                        <el-option 
                            v-for="group in groups.slice(1)" 
                            :key="group" 
                            :label="group.name" 
                            :value="group.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="图片">
                    <el-upload action="#" 
                        ref="upload"
                        list-type="picture-card" 
                        :auto-upload="false" 
                        :multiple="false"
                        :on-change="change" 
                        :on-exceed="exceed" 
                        accept=".jpg,.png" 
                        :limit="1">
                        <el-icon>
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="uploadImageDialog = false">取消</el-button>
                    <el-button type="primary" @click="uploadImageHandle">上传</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
  
<script>
import utils from '@/utils';
import TopModule from '../module/TopModule.vue';
import { LazyImg, Waterfall } from 'vue-waterfall-plugin-next'
import 'vue-waterfall-plugin-next/dist/style.css'
import { ElMessage } from 'element-plus'

export default {
    name: 'UserFiles',
    components: {
        TopModule,
        LazyImg, 
        Waterfall
    },

    data() {
        return {
            // 工具类
            utils: utils,
            // 查询条件
            query: utils.query,
            groups: [{id: 0, name: '所有图片'}],
            sorts: [
                utils.sorts.noSort,
                utils.sorts.timeSort
            ],
            // 上传图片
            uploadImageDialog: false,
            uploadImageForm: {
                name: '',
                groupID: '',
                file: null
            },
            //图片数据
            list: []
        }
    },

    mounted() {
        // 加载分组数据
        utils.request(
            "加载数据", {url: '/imageGroups', method: 'get'},
            response => {
                for (let index = 0; index < response.data.length; index++) {
                    this.groups.push(response.data[index]);
                }
                this.pullImages()
            }
        )
    },

    methods: {
        // 上传图片  
        uploadImageHandle() {
            utils.request(
                "上传图片中", 
                {url: `/images`, method: 'post', data: this.uploadImageForm, headers: {'Content-Type': 'multipart/form-data'}},
                response => {
                    this.uploadImageDialog = false
                    this.groupChangeHandle(this.uploadImageForm.groupID)
                    this.$refs.upload.clearFiles()
                    this.$refs.top.group_value = this.uploadImageForm.groupID
                }
            )
        },

        // 删除图片
        delHandle(imageId) {
            utils.request(
                "删除图片", {url: `/images/${imageId}`, method: 'delete'},
                response => {
                    this.groupChangeHandle(this.activeGroupID)
                }
            )
        },

        // 分组改变时
        groupChangeHandle(val) {
            this.query.groupId = val
            this.pullImages()
        },

        // 排序改变时
        sortChangeHandle(val) {
            this.query.sort = val
            this.pullImages()
        },
    
        // 搜索
        searchHandle(val) {
            this.query.keyword = val
            this.pullImages()
        },

        // 添加图片
        uploadImage() {
            this.uploadImageDialog = true
        },

        // 根据查询条件拉取图片
        pullImages() {
            utils.request(
                "加载图片", {url: `/images`, method: 'get', params: this.query},
                response => {
                    this.list = response.data
                    utils.methods.formatDateList(this.list, 'createTime')
                }
            )
        },

        change(uploadFile, uploadFiles) {
            // upload 组件选中文件成功后触发
            this.uploadImageForm.file = uploadFile.raw
        },

        exceed(files, uploadFiles) {
            // upload 组件选中文件失败后触发
            ElMessage({
                message: '只能选择一个图片！',
                type: 'warning',
            })
        }
    }
}
</script>

<style scoped>
.files {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.content {
    flex: 1;
    overflow: auto;
    padding: 10px;
}

.content {
  scrollbar-width: none;  /* 针对 Firefox */
  -ms-overflow-style: none;  /* 针对 Internet Explorer 和 Edge */
}

.content::-webkit-scrollbar {
  display: none;  /* 针对 Chrome、Safari 和 Opera */
}

.text {
    display: flex;
    justify-content: space-around;
    padding-top: 15px;
    font-size: smaller
}
.text > span {
    flex: 1;
}

.del {
    position: absolute; 
    right: 4px; 
    top: 4px;
    display: none;
    transition: transform 0.5s ease;
}

.card {
    position: relative;
}

.card:hover .del {
    display: block;
}

.del:hover {
    cursor: pointer;
    transform: rotate(360deg);
    color: salmon;
}

.image:hover {
    cursor: pointer;
}
</style>
  