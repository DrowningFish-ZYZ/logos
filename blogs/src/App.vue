<template>
  <div class="common-layout">
    <div class="left">
      <Sidebar></Sidebar>
    </div>

    <transition 
      class="main"
      appear
      enter-active-class="animate__animated animate__fadeIn"
      leave-active-class="animate__animated animate__fadeOut">
        <router-view></router-view>
    </transition>
  </div>
</template>

<script>
import Sidebar from './components/Sidebar.vue'
import { onBeforeUnmount, onBeforeMount } from 'vue';
import store from './store';

export default {
  setup() {
    // 监听页面刷新事件
    window.addEventListener('beforeunload', handleBeforeUnload);

    // 在组件卸载时移除监听
    onBeforeUnmount(() => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    });

    // 在组件加载前执行
    onBeforeMount(() => {
      // 页面加载时尝试从本地存储中恢复数据
      if (localStorage.getItem('vuexState')) {
          store.replaceState(JSON.parse(localStorage.getItem('vuexState')));
      }
    })

    function handleBeforeUnload(event) {
      // 在这里处理刷新前的逻辑，保存 Vuex 数据到本地存储
      localStorage.setItem('vuexState', JSON.stringify(store.state));
    }
  },

  name: 'App',
  components: {
    Sidebar
  },
}
</script>

<style>
@import 'animate.css';

* {
  margin: 0;
  padding: 0;
}

.common-layout {
  width: 100vw;
  height: 100vh;
  display: flex;
  background-color: #f1f2f5;
  overflow: hidden;
}

.common-layout .left {
  flex: 0.1;
  display: flex;
}

.common-layout .main {
  flex: 1;
}
</style>
