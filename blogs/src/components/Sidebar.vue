<template>
  <div class="sidebar">
    <el-space :size="25" direction="vertical">
      <el-tooltip
        v-if="$store.state.user.token"
        effect="dark"
        content="查看个人信息">
        <el-avatar 
          @click="descAuthorHandle" 
          size="large" 
          :src="$store.state.user.headPortrait" />
      </el-tooltip>

      <el-link 
        @click="utils.methods.gotoPage(utils.routers.HOME)" 
        type="primary" 
        icon="House">&nbsp;博客主页</el-link>

      <el-link 
        @click="utils.methods.gotoPage(utils.routers.USER_COLLECTOR)" 
        :type="!$store.state.user.token? 'info':'success'" 
        icon="Star"
        :disabled="!$store.state.user.token">
          &nbsp;我的收藏
        </el-link>

      <el-link 
        @click="utils.methods.gotoPage(utils.routers.USER_PUBLISH)" 
        :type="!$store.state.user.token? 'info':'warning'" 
        icon="CirclePlus"
        :disabled="!$store.state.user.token">
          &nbsp;我的发布
      </el-link>

      <el-link 
        @click="utils.methods.gotoPage(utils.routers.USER_FILES)" 
        :type="!$store.state.user.token? 'info':'danger'" 
        icon="Picture"
        :disabled="!$store.state.user.token">
          &nbsp;我的图片
      </el-link>

      <el-link 
        @click="utils.methods.gotoPage(utils.routers.USER_EDIT)" 
        :type="!$store.state.user.token? 'info':'warning'" 
        icon="Edit"
        :disabled="!$store.state.user.token">
          &nbsp;我的信息
      </el-link>

      <el-link
        v-if="!$store.state.user.token"
        @click="utils.methods.gotoPage(utils.routers.REGISTER)" 
        type="danger"
        :underline="false">
          &nbsp;注册
      </el-link>

      <el-link
        v-if="!$store.state.user.token"
        @click="utils.methods.gotoPage(utils.routers.LOGIN)" 
        type="danger"
        :underline="false">
          &nbsp;登录
      </el-link>
    </el-space>

    <div class="logo">
      <el-image 
        style="width: 110px;"
        :src="require('@/assets/logo.png')"/>
    </div>
  </div>
</template>

<script>
import utils from '@/utils'

export default {
  name: 'Sidebar',
  methods: {
    // 点击查看作者详情
    descAuthorHandle() {
        this.$store.state.descUserId = this.$store.state.user.id
        this.$router.push({name: utils.routers.USER_INFO})
    },
  },

  data() {
    return {
      utils: utils,
    }
  }
}
</script>

<style scoped>
.sidebar {
  flex: 1;
  padding-top: 20px;
  background-color: #545c64;
  display: flex;
  justify-content: center;
  position: relative;
}

.logo {
  background-color: white; 
  position: absolute; 
  bottom: 0px; 
  width: 100%; 
  display: flex; 
  align-items: center; 
  justify-content: center;
}

.el-link {
  font-size: medium;
  word-wrap: break-word;
  width: 100px;
}

.el-avatar:hover {
  cursor: pointer;
}
</style>
