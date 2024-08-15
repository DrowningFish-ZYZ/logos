package com.violet.article.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.violet.article.domain.po.Article;
import com.violet.article.domain.po.CollectArticleGroup;
import com.violet.article.domain.po.CollectGroupAndArticle;
import com.violet.article.mapper.CollectArticleGroupMapper;
import com.violet.common.domain.PageData;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import com.violet.common.domain.SortOrder;
import com.violet.common.enums.ResponseCode;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QueryMatchPageUtils {

    private final CollectArticleGroupMapper collectArticleGroupMapper;

    public static LambdaQueryWrapper<Article> buildArticleWrapper(QueryMatchPage query) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 分组筛选
        if (query.getGroupId() != null && query.getGroupId() > 0) {
            wrapper.eq(Article::getPublishArticleGroupId, query.getGroupId());
        }
        // 关键字筛选
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper
                .like(Article::getTitle, query.getKeyword())
                .or()
                .like(Article::getDesc, query.getKeyword());
        }
        // 是否排序
        if (SortOrder.CREATE_TIME.equals(query.getSort())) {
            // 按时间排序
            wrapper.orderByDesc(Article::getCreateTime);
        } else if (SortOrder.COLLECT_COUNT.equals(query.getSort())) {
            // 按收藏量排序
            wrapper.orderByDesc(Article::getCollectCount);
        } else if (SortOrder.COMMENT_COUNT.equals(query.getSort())) {
            // 按评论量排序
            wrapper.orderByDesc(Article::getCommentCount);
        } else if (SortOrder.HOT.equals(query.getSort())) {
            // 最热排序
            wrapper
                .orderByDesc(Article::getCollectCount)
                .orderByDesc(Article::getCommentCount);
        }
        return wrapper;
    }

    public LambdaQueryWrapper<CollectGroupAndArticle> buildCollectGroupAndArticleWrapper(QueryMatchPage query) {
        LambdaQueryWrapper<CollectGroupAndArticle> wrapper = new LambdaQueryWrapper<>();
        // 分组查询
        if (query.getGroupId() != null && query.getGroupId() > 0) {
            CollectArticleGroup group = collectArticleGroupMapper.selectOne(
                    new LambdaQueryWrapper<CollectArticleGroup>()
                            .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
                            .eq(CollectArticleGroup::getId, query.getGroupId())
            );
            if (group == null) throw new ParameterException("异常的分组");
            wrapper.eq(CollectGroupAndArticle::getGroupId, group.getId());
        } else {
            // 获取当前用户所有的分组 ID
            Set<Long> groupIds = collectArticleGroupMapper.selectList(
                    new LambdaQueryWrapper<CollectArticleGroup>()
                            .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
            ).stream().map(CollectArticleGroup::getId).collect(Collectors.toSet());
            wrapper.in(CollectGroupAndArticle::getGroupId, groupIds);
        }
        return wrapper;
    }

}
