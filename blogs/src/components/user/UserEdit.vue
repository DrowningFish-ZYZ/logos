<template>
    <div class="edit">
        <div style="width: 80%;">
            <el-card>
                <el-descriptions
                    class="margin-top"
                    title="编辑信息"
                    :column="1"
                    size="default"
                    border>
                    <template #extra>
                        <el-button type="primary" @click="uploadImageDialog = true">更改头像</el-button>
                        <el-button type="danger" @click="logout">退出登录</el-button>
                        <el-button type="warning" @click="updateUser">确认修改</el-button>
                    </template>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon><user /></el-icon>
                                用户名
                            </div>
                        </template>
                        <el-input 
                            v-model="formdata.username"
                            maxlength="10"
                            show-word-limit/>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon><iphone /></el-icon>
                                手机号
                            </div>
                        </template>
                        {{ formdata.phone }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <el-icon><Document /></el-icon>
                            个人简介
                        </template>
                        <el-input type="textarea" 
                            v-model="formdata.desc"
                            maxlength="20"
                            show-word-limit/>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <el-icon v-if="formdata.gender == 0"><Male /></el-icon>
                            <el-icon v-if="formdata.gender == 1"><Female /></el-icon>
                            性别
                        </template>
                        <el-radio-group v-model="formdata.gender">
                            <el-radio :value="0">男</el-radio>
                            <el-radio :value="1">女</el-radio>
                        </el-radio-group>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <el-icon><Lock /></el-icon>
                            旧密码
                        </template>
                        <el-input v-model="formdata.oldPwd" 
                            placeholder="如果你想修改密码请填写旧密码" 
                            type="password"
                            maxlength="10"
                            show-word-limit/>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <el-icon><Lock /></el-icon>
                            新密码
                        </template>
                        <el-input 
                            v-model="formdata.newPwd" 
                            placeholder="如果你想修改密码请填写旧密码" 
                            type="password"
                            maxlength="10"
                            show-word-limit/>
                    </el-descriptions-item>
                </el-descriptions>
            </el-card>
        </div>

        <!-- 更改头像 -->
        <el-dialog v-model="uploadImageDialog" title="更改头像" width="600">
            <el-alert title="输入图片地址、推荐使用`我的图片`模块进行管理哦" type="info" show-icon style="margin-bottom: 10px;"/>
            <el-form :model="uploadImageForm">
                <el-form-item label="图片路径">
                    <el-input placeholder="输入图片地址、推荐使用`我的图片`模块进行管理哦" v-model="formdata.headPortrait"/>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import utils from '@/utils';
import { ElMessage } from 'element-plus';

export default {
    data() {
        return {
            uploadImageDialog: false,
            formdata: {
                desc: '',
                gender: 0,
                phone: '',
                username: '',
                headPortrait: '',
                oldPwd: '',
                newPwd: ''
            }
        }
    },

    mounted() {
        utils.request(
            "加载信息", {url: `/users/logged`, method: 'get'},
            response => {
                this.formdata = response.data
            }
        )
    },

    methods: {
        updateUser() {
            utils.request(
                "加载信息", {url: `/users/logged`, method: 'put', data: this.formdata},
                response => {
                    ElMessage({
                        message: "修改成功",
                        type: "success"
                    })
                    this.$store.state.user.headPortrait = this.formdata.headPortrait
                    localStorage.setItem("user", JSON.stringify(this.$store.state.user))
                }
            )
        },

        logout() {
            localStorage.removeItem("vuexState")
            this.$store.state.user = {id: '', token: '', username: '', phone: '', desc: '', headPortrait: ''}
            utils.methods.gotoPage(utils.routers.HOME)
        }
    }
}
</script>

<style scoped>
.edit {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    padding: 10px;
}
</style>