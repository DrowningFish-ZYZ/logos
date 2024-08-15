<template>
<!-- 显示文章 -->
<div class="content">
    <div v-if="sources.length != 0" class="content-container">
        <!-- 文章 -->
        <el-card 
            v-for="source in sources" 
            :key="source" 
            shadow="hover" 
            class="content-card">
            <!-- 文章头 -->
            <template #header>
                <div class="card-header">
                    <span>{{ source.title }}</span>
                    <span 
                        v-if="type == 'delCollect'" 
                        class="card-header-del" 
                        @click="delCollectHandle(source.id)">×
                    </span>
                    <span 
                        v-if="type == 'del'" 
                        class="card-header-del" 
                        @click="del(source.id)">×
                    </span>
                    <el-icon 
                        v-if="type == 'edit'" 
                        class="card-header-edit" 
                        @click="editHandle(source.id)">
                        <Edit />
                    </el-icon>
                </div>
            </template>
            <!-- 文章详情 -->
            <p class="content-card-desc" 
                @click="descHandle(source.id)">
                {{ source.desc }}
            </p>
            <!-- 文章信息 -->
            <template #footer>
                <div class="content-card-footer">
                    <!-- 作者 -->
                    <div class="content-card-footer-author" v-if="author">
                        <el-icon size="small"><User /></el-icon>
                        <span @click="descAuthorHandle(source.user.id)" >
                            {{ source.user.username }}
                        </span>
                    </div>
                    <!-- 收藏数 -->
                    <div>
                        <el-icon size="small"><StarFilled /></el-icon>
                        <span>{{ source.collectCount }}</span>
                    </div>
                    <!-- 评论数 -->
                    <div>
                        <el-icon size="small"><ChatLineRound /></el-icon>
                        <span>{{ source.commentCount }}</span>
                    </div>
                    <!-- 创建时间 -->
                    <span class="content-card-footer-time">
                        <el-icon size="small"><Clock /></el-icon>
                        <span>{{ source.createTime }}</span>
                    </span>
                </div>
            </template>
        </el-card>
    </div>

    <el-empty 
        v-if="sources.length == 0" 
        description="空空如也" 
        style="width: 100%; height: 100%; background-color: white; letter-spacing: 1px"
        :image-size="200" />
</div>
</template>

<script>
import utils from '@/utils';
export default {
    props: {
        author: Boolean,        // 是否显示作者
        type: String,           // 右上角功能选项 【edit、del、delCollect、none】
        sources: Array,         // 文章数据：{id, title, desc, author, collectorCount, commentCount, createDate}

        '@editHandle': Function,            // 文章编辑按钮触发事件
        '@descHandle': Function,            // 点击查看文章
        '@delCollect': Function,            // 删除收藏
        '@descAuthorHandle': Function,      // 点击查看作者详情
    },

    data() {
        return {
            utils: utils
        }
    },

    methods: {
        del(articleId) {
            alert("要删除的文章ID: " + articleId)
        },

        editHandle(articleId) {
            if(this['@editHandle']) this['@editHandle'](articleId)
        },

        descHandle(articleId) {
            if(this['@descHandle']) this['@descHandle'](articleId)
        },

        delCollectHandle(articleId) {
            if(this['@delCollect']) this['@delCollect'](articleId)
        },

        descAuthorHandle(articleId) {
            if(this['@descAuthorHandle']) this['@descAuthorHandle'](articleId)
        }
    }
}
</script>

<style scoped>
/* 中间 */
.content {
    overflow: auto; 
    flex: 1; 
}

.content-container {
    display: flex; 
    justify-content: center;
    flex-wrap: wrap;
}

.content-card {
    border: 1px solid rgb(183, 183, 183);
    border-radius: 5px;
    width: 350px;
    height: 240px;
    margin: 10px 5px;
}

.card-header {
    font-style: italic;
    color: black;
    font-weight: 600;
    font-size: larger;
    position: relative;
    display: flex;
    align-items: center;
}

.card-header-del {
    position: absolute; 
    right: 0px; 
    font-size: larger;
    font-style: normal;
    transition: transform 0.5s ease;
}

.card-header-del:hover {
    cursor: pointer;
    transform: rotate(360deg);
    color: salmon;
}

.card-header-edit {
    position: absolute; 
    right: 0px; 
    font-size: medium;
    font-style: normal;
}

.card-header-edit:hover {
    color: salmon;
    cursor: pointer;
}

.content-card-desc {
    color: grey;
    min-height: 82px;
    max-height: 82px;
    overflow: hidden;
}

.content-card-desc:hover {
    cursor: pointer;
}

.content-card-footer {
    position: relative;
    display: flex;
    align-items: center;
}

.content-card-footer > div {
    margin-right: 10px;
    font-size: small;
    display: flex;
    align-items: center;
}

.content-card-footer-time {
    position: absolute; 
    right: 0px; 
    color: gray; 
    font-size: small;
    display: flex;
    align-items: center;
}

.content-card-footer-author:hover {
    cursor: pointer;
    color: salmon;
}

.content-card-footer-time > span {
    margin-left: 2px;
}
</style>