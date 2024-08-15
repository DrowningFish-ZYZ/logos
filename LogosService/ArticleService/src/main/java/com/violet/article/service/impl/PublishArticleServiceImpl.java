package com.violet.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.api.client.CommentClient;
import com.violet.api.domain.vo.ArticleVO;
import com.violet.article.domain.dto.PublishArticleFormDTO;
import com.violet.article.domain.po.Article;
import com.violet.article.domain.po.CollectArticleGroup;
import com.violet.article.domain.po.CollectGroupAndArticle;
import com.violet.article.mapper.ArticleMapper;
import com.violet.article.mapper.CollectArticleGroupMapper;
import com.violet.article.mapper.CollectGroupAndArticleMapper;
import com.violet.article.service.IPublishArticleService;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class PublishArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements IPublishArticleService {

    private final CollectGroupAndArticleMapper groupAndArticleMapper;
    private final CollectArticleGroupMapper articleGroupMapper;

    private final CommentClient commentClient;

    @Override
    public Result getPublishArticles(QueryMatchPage query) {
        // 1.校验用户
        UserContext.verify();
        // 2.分页查询
        Page<Article> page = new Page<>(query.getPage(), query.getSize());
        // 3.根据条件查询
        LambdaQueryWrapper<Article> wrapper = QueryMatchPageUtils.buildArticleWrapper(query);
        wrapper.eq(Article::getAuthorId, UserContext.getUserId());
        // 4.查询
        Page<Article> articlePage  = baseMapper.selectPage(page, wrapper);
        // 5.封装 VO
        List<ArticleVO> vos = BeanUtil.copyToList(page.getRecords(), ArticleVO.class);
        // 6.将数据和分页信息封装到 PageData 中
        PageData data = PageData.Builder.build(vos, articlePage);
        // 7.返回数据
        return Result.Builder.buildGetResult(data);
    }

    @Override
    public Result createPublishArticle(PublishArticleFormDTO dto) {
        // 1.校验用户和数据
        UserContext.verify();
        dto.verify();
        // 2.文章名不能重复
        Article one = this.lambdaQuery()
            .eq(Article::getAuthorId, UserContext.getUserId())
            .eq(Article::getTitle, dto.getTitle())
            .one();
        if (one != null) throw new ParameterException("文章名不能重复");
        // 4.创建文章
        Article article = BeanUtil.copyProperties(dto, Article.class);
        article.setAuthorId(UserContext.getUserId());
        article.setCreateTime(LocalDateTime.now());
        article.setCollectCount(0);
        article.setCommentCount(0);
        // 5.入库
        this.save(article);
        // 6.返回信息
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getPublishArticlesById(Long id) {
        // 1.校验用户
        UserContext.verify();
        // 2.根据 ID 获取文章
        Article article = this.lambdaQuery()
            .eq(Article::getId, id)
            .eq(Article::getAuthorId, UserContext.getUserId())
            .one();
        if (article == null) throw new ParameterException("错误的文章");
        // 3.封装 VO
        ArticleVO vo = BeanUtil.copyProperties(article, ArticleVO.class);
        // 4.返回数据
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result updatePublishArticleById(PublishArticleFormDTO dto, Long id) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 1.获取文章
        Article article = this.lambdaQuery()
                .eq(Article::getAuthorId, UserContext.getUserId())
                .eq(Article::getId, id)
                .one();
        if (article == null) throw new ParameterException("错误的文章");
        // 2.文章名字不能重复
        Article one = this.lambdaQuery()
                .eq(Article::getTitle, dto.getTitle())
                .ne(Article::getId, id)
                .eq(Article::getAuthorId, UserContext.getUserId())
                .one();
        if (one != null) throw new ParameterException("名字不能重复");
        // 3.修改数据
        article.setTitle(dto.getTitle());
        article.setDesc(dto.getDesc());
        article.setContent(dto.getContent());
        article.setPublishArticleGroupId(dto.getPublishArticleGroupId());
        // 4.入库
        this.updateById(article);
        // 5.返回信息
        return Result.Builder.buildUpdateResult();
    }

    @Override
    public Result deletePublishArticleById(Long id) {
        // 校验用户
        UserContext.verify();
        // 获取对应文章
        Article article = this.lambdaQuery().eq(Article::getId, id).one();
        if (article == null) throw new ParameterException("错误的文章");
        // 根据文章 ID 找到对应的收藏记录 ID
        Set<Long> collects = groupAndArticleMapper.selectList(
                new LambdaQueryWrapper<CollectGroupAndArticle>()
                        .eq(CollectGroupAndArticle::getArticleId, article.getId())
        ).stream().map(CollectGroupAndArticle::getId).collect(Collectors.toSet());
        // 删除对应文章的收藏分组
        if (!collects.isEmpty()) {
            groupAndArticleMapper.deleteBatchIds(collects);
            // 再把文章的评论删除
            commentClient.deleteCommentByArticleId(article.getId());
        }
        // 最后把文章删除
        this.removeById(article.getId());
        // 返回详情
        return Result.Builder.buildDeleteResult();
    }
}
