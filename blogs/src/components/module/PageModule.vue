<template>
<!-- 分页 -->
<div class="page">
    <el-pagination
        :page-size="pageSize"
        :total="total"
        background
        layout="total, prev, pager, next"
        @current-change="handle"/>
</div>
</template>

<script>
export default {
    props: {
        '@change': Function,    // 页码改变时触发 Function(val: Number)
        pageSize: Number,       // 每页个数
        total: Number,          // 总数        
    },

    methods: {
        handle(val) {
            if(this['@change']) this['@change'](val)
        }
    }
}
</script>

<style scoped>
/* 自定义分页样式 */
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: salmon !important;
}

/* 尾巴 */
.page {
    background-color: rgb(233, 233, 233); 
    padding: 10px; 
    display: flex; 
    justify-content: center;
}
</style>