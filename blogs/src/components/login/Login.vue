<template>
    <div id="register">
        <el-alert title="验证码和密码二选一" type="success" />
        <el-divider />

        <h4 class="logo" @click="login()"><i>登录</i></h4>

        <el-form ref="form" :model="fromdata" label-position="right" label-width="65px" style="width: 500px"
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

            <el-form-item>
                <el-button @click="login('form')">登录</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { DArrowRight, User, Lock, Plus } from '@element-plus/icons-vue'
import utils from '@/utils'
import { ElMessage } from 'element-plus';

export default {
    name: 'login',
    components: { DArrowRight, User, Lock, Plus },
    data() {
        return {
            // 校验数据
            rules: {
                password: [{ 'max': 8, 'min': 4, 'message': '必须是 4 - 8 位字符' }],
                phone: [{ 'required': true, 'pattern': /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, 'message': '格式不正确' }],
            },

            genderOptions: [{ 'value': 0, 'label': '男' }, { 'value': 1, 'label': '女' }],
            file: null,
            // 表单数据
            fromdata: {
                phone: '',  // 电话
                code: '',  // 验证码
                password: '',  // 密码
            },

            phone: '',
            isSending: false,
            countdown: 0,
        }
    },

    methods: {
        login(formEl) {
            this.$refs[formEl].validate((valid) => {
                if (valid) {
                    utils.request(
                        "登录中", 
                        {url: '/users/login', method: 'post', data: this.fromdata},
                        response => {
                            if (response.code == 200) {
                                this.$store.state.user = response.data
                                utils.methods.gotoPage(utils.routers.HOME)
                            }
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
                        {url: `/users/login/code/${this.fromdata.phone}`, method: 'get'},
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
    },
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