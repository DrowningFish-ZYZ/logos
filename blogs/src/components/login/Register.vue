<template>
    <div id="register">
        <h4 class="logo"><i>注册</i></h4>
        
        <el-form 
            ref="form" 
            :model="fromdata" 
            label-position="right" 
            label-width="65px" 
            style="width: 500px"
            :rules="rules">
            <el-form-item label="号码" prop="phone">
                <el-input v-model="fromdata.phone" placeholder="手机号码">
                    <template #append>
                        <el-button @click="sendCode" :disabled="isSending || countdown > 0">
                            {{ isSending ? '发送中...' : countdown > 0 ? `${countdown}秒后重试` : '发送验证码' }}
                        </el-button>
                    </template>
                </el-input>
            </el-form-item>

            <el-form-item label="验证码" prop="code">
                <el-input placeholder="验证码" v-model='fromdata.code' />
            </el-form-item>

            <el-form-item label="密码" prop="password">
                <el-input type="password" placeholder="密码" v-model='fromdata.password' />
            </el-form-item>

            <el-form-item label="用户名" prop="username">
                <el-input placeholder="用户名" v-model='fromdata.username' />
            </el-form-item>

            <el-form-item label="简介" prop="info">
                <el-input placeholder="个性简介" v-model='fromdata.info' />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
                <el-select-v2 v-model="fromdata.gender" placeholder="性别" :options="genderOptions" />
            </el-form-item>

            <el-form-item>
                <el-button @click="register('form')">注册</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { DArrowRight, User, Lock, Plus } from '@element-plus/icons-vue'
import utils from '@/utils'
import { ElMessage } from 'element-plus';

export default {
    name: 'register',
    components: { DArrowRight, User, Lock, Plus },
    data() {
        return {
            // 校验数据
            rules: {
                username: [ { 'required': true, 'message': '这是必须的' },{ 'max': 8, 'min': 4, 'message': '必须是 4 - 8 位字符' }],
                password: [{ 'required': true, 'message': '这是必须的' },{ 'max': 8, 'min': 4, 'message': '必须是 4 - 8 位字符' }],
                gender: [{ 'required': true, 'message': '这是必须的', 'trigger': 'change' }],
                info: [{ 'max': 15, 'min': 4, 'message': '必须是 4 - 15 位字符' }],
                phone: [{'required': true, 'pattern': /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, 'message': '格式不正确' }],
                code: [{'required': true, 'message': '格式不正确' }],
            },
            genderOptions: [{ 'value': 0, 'label': '男' }, { 'value': 1, 'label': '女' }],
            // 表单数据
            fromdata: {
                gender: '',  // 性别
                phone: '',  // 电话
                code: '',  // 验证码
                username: '',  // 用户名
                password: '',  // 密码
                info: ''  // 简介
            },
            phone: '',
            isSending: false,
            countdown: 0,
        }
    },

    methods: {
        register(formEl) {
            this.$refs[formEl].validate((valid) => {
                if (valid) {
                    utils.request(
                        "注册中", 
                        {url: '/users/register', method: 'post', data: this.fromdata},
                        response => {
                            utils.methods.gotoPage(utils.routers.LOGIN)
                        }
                    )
                } else { return false }
            });
        },

        sendCode() {
            let flag = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/.exec(this.fromdata.phone)
            if (flag) {
                if (this.countdown > 0 || this.isSending) {
                    return; // 防止重复点击发送
                }
                // 假设在这个方法中实现发送验证码的逻辑
                // 可以调用sendVerificationCode()方法发送验证码
                // 这里只是简单模拟发送过程
                this.isSending = true;
                this.startCountdown();
                setTimeout(() => {
                    utils.request(
                        "发送验证码", 
                        {url: `/users/code/${this.fromdata.phone}`, method: 'get'},
                        response => {
                            ElMessage({
                                message: '发送成功',
                                type: "success"
                            })
                        }
                    )
                    this.isSending = false;
                }, 2000); // 这里使用2秒的延迟来模拟发送过程，你需要替换为实际的发送逻辑
            } else {
                ElMessage({
                    message: "手机号错误",
                    type: "warning"
                })
            }
        },

        startCountdown() {
            this.countdown = 60;
            const timer = setInterval(() => {
                this.countdown--;
                if (this.countdown <= 0) {
                    clearInterval(timer);
                }
            }, 1000);
        },
    }
}
</script>

<style scoped>
#register {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 20px 15px;
    align-items: center;
}

.logo {
    text-align: center;
    margin-bottom: 20px;
    letter-spacing: 2px;
}

/* ===================================== 配色 ===================================== */
</style>