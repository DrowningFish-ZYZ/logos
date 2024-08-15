<template>
<!-- 顶部 -->
<div class="top">
    <div style="flex: 1;">
        <!-- 分组选择 -->
        <el-select 
            v-if="groups"
            v-model="group_value" 
            placeholder="分组选择" 
            style="width: 200px"
            @change="groupChangeHandle">

            <el-option
            v-for="item in groups"
            :key="item.name"
            :label="item.name"
            :value="item.id"
            :disabled="item.disabled"/>
        </el-select>

        <!-- 排序选择 -->
        <el-select 
            v-if="sorts"
            v-model="sort_value" 
            placeholder="排序选择" 
            style="width: 200px"
            @change="sortChangeHandle">

            <el-option
            v-for="item in sorts"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
                <span style="float: left">{{ item.label }}</span>
                <span
                    style="
                    float: right;
                    color: var(--el-text-color-secondary);
                    font-size: 13px;">
                    <el-icon><Sort /></el-icon>
                </span>
            </el-option>
        </el-select>

        <!-- 内容搜索 -->
        <el-input
            v-if="showSearch"
            v-model="searchValue"
            style="min-width: 400px; max-width: 400px"
            :placeholder="placeholder"
            class="input-with-select">
            <template #append>
                <el-button icon="Search" @click="searchHandle"/>
            </template>
        </el-input>
    </div>

    <!-- 管理 -->
    <div class="top-manager">
        <el-button-group>
            <el-button
                v-if="showGourpButton"
                type="warning" 
                icon="Coin" 
                @click="groupManagerHandle"
                style="width: 100px;">
                分组管理
            </el-button>

            <el-button
                v-if="showWriteButton"
                type="success" 
                icon="Edit" 
                @click="utils.methods.gotoPage(utils.routers.USER_PUBLISH_WRITE)"  
                style="width: 100px;">
                编写文章
            </el-button>

            <el-button
                v-if="showUploadButton"
                type="success" 
                icon="UploadFilled" 
                @click="uploadHandle"  
                style="width: 100px;">
                上传图片
            </el-button>
        </el-button-group>
    </div>
</div>
</template>

<script>
import utils from '@/utils';

export default {
    props: {
        placeholder: String,
        groupType: String,            // 分组要前往的页面

        showSearch: Boolean,          // 是否显示搜索
        showGourpButton: Boolean,     // 是否显示分组管理按钮
        showWriteButton: Boolean,     // 是否显示文章编写按钮
        showUploadButton: Boolean,    // 显示文件上传按钮

        groups: Array,                // 分组数据[{label: String, value: String, disabled: Boolean}]
        sorts: Array,                 // 排序数据[{label: String, value: String, disabled: Boolean}]

        '@groupChange': Function,      // 分组值发生改变时 function (val)
        '@sortChange': Function,       // 排序值发生改变时 function (val)
        '@search': Function,           // 搜索事件 function (val)
        '@upload': Function,           // 点击上传按钮触发
        '@groupManager': Function,     // 点击分组管理是触发
    },

    data() {
        return {
            utils: utils,
            group_value: null,
            sort_value: null,
            searchValue: null
        }
    },

    methods: {
        groupChangeHandle(val) {
            if (this['@groupChange']) this['@groupChange'](val)
        },

        sortChangeHandle(val) {
            if (this['@sortChange']) this['@sortChange'](val)
        },

        searchHandle() {
            if (this['@search']) this['@search'](this.searchValue)
        },

        uploadHandle() {
            if (this['@upload']) this['@upload']()
        },

        groupManagerHandle() {
            if (this['@groupManager']) this['@groupManager'](this.groupType)
        }
    },

    mounted() {
        if (this.sorts && this.sorts.length > 0) this.sort_value = this.sorts[0].value
        if (this.groups && this.groups.length > 0) this.group_value = this.groups[0].id
    }
}
</script>

<style scoped>
/* 顶部 */
.top {
    padding: 10px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    background-color: rgb(233, 233, 233);
}

.top-manager {
    display: flex; 
    justify-content: flex-end;
}
</style>