<template>
<div class="my-tabs">
    <el-tabs v-model="activeName" @tab-change="handle">
        <el-tab-pane :label="pane.label" :name="pane.name" v-for="pane in panes" :key="pane"/>
        <!-- <el-tab-pane label="最近发布" name="最近"></el-tab-pane> -->
    </el-tabs>
</div>
</template>

<script>
export default {
    props: {
        '@change': Function,    // 选项卡事件改变时触发 function(name:String)
        panes: Array,           // 选项列表：[{label, name}]
    },

    data() {
        return {
            activeName: null
        }
    },

    methods: {
        handle(name) {
            if (this['@change']) this['@change'](name)
        }
    },

    mounted() {
        if (this.panes) this.activeName = this.panes[0].name
    }
}
</script>

<style scoped>
/* 自定义 Tabs */
.my-tabs >>> .el-tabs__item.is-active {
    /* background: rgba(0, 102, 255, 0.08); */
    /* border-radius: 4px 4px 3px 3px; */
    color: salmon;
}

.my-tabs >>> .el-tabs__item:hover {
    color: salmon;
}

.my-tabs >>> .el-tabs__active-bar {
    background-color: salmon; /* 修改底部横杠的颜色 */
    height: 3px; /* 修改底部横杠的高度 */
}

.my-tabs /deep/ .el-tabs__active-bar::after{
    content: "";
    width: 0;
    height: 0;
    position: absolute;
    left: 39%;
    bottom:2px;
    border-top: 10px solid transparent;
    border-right: 10px solid transparent;
    border-left: 10px solid transparent;
    border-bottom: 5px solid salmon;
}

.my-tabs /deep/ .el-tabs__item {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100px !important;
    padding: 0 !important;
}

.my-tabs /deep/ .el-tabs__nav.is-top{
    display: flex;  
}

.my-tabs /deep/ .el-tabs__nav.is-top > div {
    width: 100px;
    text-align: center;
}
</style>