package com.violet.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.article.domain.dto.CollectArticleFormDTO;
import com.violet.article.domain.dto.CollectGroupAndArticleFormDTO;
import com.violet.article.domain.po.Article;
import com.violet.article.domain.po.CollectArticleGroup;
import com.violet.article.domain.po.CollectGroupAndArticle;
import com.violet.article.domain.vo.CollectArticleVO;
import com.violet.article.mapper.ArticleMapper;
import com.violet.article.mapper.CollectArticleGroupMapper;
import com.violet.article.mapper.CollectGroupAndArticleMapper;
import com.violet.article.service.ICollectArticleService;
import com.violet.article.utils.ArticleAndUserUtils;
import com.violet.article.utils.QueryMatchPageUtils;
import com.violet.common.domain.PageData;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class CollectArticleServiceImpl extends ServiceImpl<CollectGroupAndArticleMapper, CollectGroupAndArticle>
        implements ICollectArticleService {

    private final CollectArticleGroupMapper collectArticleGroupMapper;
    private final ArticleMapper articleMapper;
    private final QueryMatchPageUtils queryMatchPageUtils;
    private final ArticleAndUserUtils articleAndUserUtils;

    @Override
    public Result createCollectArticle(CollectGroupAndArticleFormDTO dto) {
        // 校验数据
        UserContext.verify();
        dto.verify();
        // 根据分组 ID 去查询
        CollectArticleGroup group = collectArticleGroupMapper.selectById(dto.getGroupId());
        if (group == null || !group.getUserId().equals(UserContext.getUserId())) throw new ParameterException("异常的分组");
        // 根据文章 ID 去查询
        Article article = articleMapper.selectById(dto.getArticleId());
        if (article == null) throw new ParameterException("异常的文章");
        if (article.getAuthorId().equals(UserContext.getUserId())) throw new ParameterException("你不能收藏自己的文章");
        // 创建收藏数据
        CollectGroupAndArticle groupAndArticle = new CollectGroupAndArticle();
        groupAndArticle.setGroupId(group.getId());
        groupAndArticle.setArticleId(article.getId());
        groupAndArticle.setCreateTime(LocalDateTime.now());
        // 入库
        boolean save = this.save(groupAndArticle);
        if (save) {
            article.setCollectCount(article.getCollectCount() + 1);
            articleMapper.updateById(article);
        }
        // 响应
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result isCollectArticle(Long articleId) {
        // 校验用户
        UserContext.verify();
        // 获取当前文章
        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ParameterException("异常文章");
        // 查看该文章有没有被收藏
        CollectGroupAndArticle groupAndArticle = this.lambdaQuery()
                .eq(CollectGroupAndArticle::getArticleId, article.getId())
                .one();
        if (groupAndArticle == null) return Result.Builder.buildGetResult(CollectArticleVO.Builder.buildNotCollected());
        // 如果被收藏了查看有没有被当前登录用户收藏
        List<CollectArticleGroup> groups = collectArticleGroupMapper.selectList(
                new LambdaQueryWrapper<CollectArticleGroup>()
                        .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
        );
        for (CollectArticleGroup group : groups) {
            if (group.getId().equals(groupAndArticle.getGroupId()))
                return Result.Builder.buildGetResult(CollectArticleVO.Builder.build(group.getId(), article.getId()));
        }
        // 未收藏
        return Result.Builder.buildGetResult(CollectArticleVO.Builder.buildNotCollected());
    }

    @Override
    public Result deleteCollectArticle(CollectArticleFormDTO dto) {
        // 校验用户
        UserContext.verify();
        // 获取文章
        Article article = articleMapper.selectById(dto.getArticleId());
        if (article == null) throw new ParameterException("异常文章");
        // 查看当前用户是否有这个分组
        CollectArticleGroup group = collectArticleGroupMapper.selectOne(
            new LambdaQueryWrapper<CollectArticleGroup>()
                .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
                .eq(CollectArticleGroup::getId, dto.getGroupId())
        );
        if (group == null) throw new ParameterException("异常分组");
        // 删除收藏信息
        boolean remove = this.lambdaUpdate()
                .eq(CollectGroupAndArticle::getArticleId, article.getId())
                .eq(CollectGroupAndArticle::getGroupId, group.getId())
                .remove();
        if (remove) {
            article.setCollectCount(article.getCollectCount() - 1);
            articleMapper.updateById(article);
        }
        // 返回信息
        return Result.Builder.buildDeleteResult();
    }

    @Override
    public Result getCollectArticles(QueryMatchPage query) {
        // 校验用户
        UserContext.verify();
        // 如果当前用户没有收藏分组、那么肯定就没有收藏任何文章
        Set<Long> groupIds = collectArticleGroupMapper.selectList(
                new LambdaQueryWrapper<CollectArticleGroup>()
                        .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
        ).stream().map(CollectArticleGroup::getId).collect(Collectors.toSet());
        if (groupIds.isEmpty()) return Result.Builder.buildGetResult(PageData.Builder.build());
        // 分页查询
        Page<Article> page = new Page<>(query.getPage(), query.getSize());
        // 根据条件查询
        LambdaQueryWrapper<CollectGroupAndArticle> queryWrapper = queryMatchPageUtils
                .buildCollectGroupAndArticleWrapper(query);
        // 获取到对应分组下的所有收藏的文章的ID
        Set<Long> articleIds = baseMapper.selectList(queryWrapper)
                .stream()
                .map(CollectGroupAndArticle::getArticleId)
                .collect(Collectors.toSet());
        if (!articleIds.isEmpty()) {
            // 根据剩余条件和文章ID获取对应文章
            query.setGroupId(null);
            LambdaQueryWrapper<Article> wrapper = QueryMatchPageUtils.buildArticleWrapper(query);
            wrapper.in(Article::getId, articleIds);
            Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
            // 如果匹配到的文章不在当前收藏分组下、不返回
            Set<Long> exit = new HashSet<>();
            for (Article article : articlePage.getRecords()) {
                if (articleIds.contains(article.getId()))
                    exit.add(article.getId());
            }
            List<Article> records = articlePage.getRecords().stream()
                    .filter(article -> exit.contains(article.getId()))
                    .collect(Collectors.toList());
            articlePage.setRecords(records);
            // 根据文章去获取对应的作者然后封装为 VO
            PageData pageData = articleAndUserUtils.acquireUsersForTheArticle(articlePage);
            // 返回数据
            return Result.Builder.buildGetResult(pageData);
        }
        // 返回空数据
        return Result.Builder.buildGetResult(PageData.Builder.build());
    }

    @Override
    public Result deleteCollectArticleById(Long articleId) {
        // 校验用户
        UserContext.verify();
        // 校验文章
        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new ParameterException("异常的文章");
        // 获取当前用户的所有分组ID
        Set<Long> groupIds = collectArticleGroupMapper.selectList(
                new LambdaQueryWrapper<CollectArticleGroup>()
                        .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
        ).stream().map(CollectArticleGroup::getId).collect(Collectors.toSet());
        if (groupIds.isEmpty()) throw new ParameterException("异常的分组");
        // 根据分组ID和文章ID去删除
        boolean remove = this.lambdaUpdate()
                .eq(CollectGroupAndArticle::getArticleId, articleId)
                .in(CollectGroupAndArticle::getGroupId, groupIds)
                .remove();
        if (remove) {
            article.setCollectCount(article.getCollectCount() - 1);
            articleMapper.updateById(article);
        }
        // 返回详情
        return Result.Builder.buildDeleteResult();
    }

}
