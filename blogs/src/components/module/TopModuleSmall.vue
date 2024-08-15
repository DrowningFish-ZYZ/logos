<template>
<div class="top">
    <!-- 选项卡 -->
    <TabsModule
        :@change="tabsHandle"
        :panes="panes"/>

    <div class="top-left">
        <el-input
            v-model="searchValue"
            style="min-width: 400px; max-width: 400px"
            :placeholder="placeholder"
            class="input-with-select">
            <template #append>
                <el-button icon="Search" @click="searchHandle"/>
            </template>
        </el-input>
    </div>
</div>
</template>

<script>
import TabsModule from './TabsModule.vue';

export default {
    props: {
        panes: Array,
        placeholder: String,
        '@change': Function,
        '@search': Function
    },

    components: {
        TabsModule
    },

    data() {
        return {
            searchValue: null,
            activeName: null,
            tabsHandle: null
        }
    },

    methods: {
        searchHandle() {
            if (this['@search']) this['@search'](this.searchValue)
        },
    },

    mounted() {
        if (this.panes) this.activeName = this.panes[0].name
        if (this['@change']) this.tabsHandle = this['@change']
    }
}
</script>

<style scoped>
.top {
    padding: 0px 20px;
    display: flex;
    background-color: rgb(245, 245, 245);
    align-items: center;
}

.top-left {
    flex: 1;
    display: flex;
    flex-direction: row-reverse;
    margin-left: 20px;
}
</style>