package com.violet.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.api.client.UserClient;
import com.violet.api.domain.vo.ArticleVO;
import com.violet.api.domain.vo.UserVO;
import com.violet.article.domain.po.Article;
import com.violet.article.domain.vo.UserArticleVO;
import com.violet.article.mapper.ArticleMapper;
import com.violet.article.service.IArticleService;
import com.violet.article.utils.ArticleAndUserUtils;
import com.violet.article.utils.QueryMatchPageUtils;
import com.violet.common.domain.PageData;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class ArticleService extends ServiceImpl<ArticleMapper, Article>
        implements IArticleService {

    private final UserClient userClient;
    private final ModelMapper modelMapper;
    private final ArticleAndUserUtils articleAndUserUtils;

    @Override
    public Result getArticleById(Long id) {
        // 1.根据 ID 获取文章
        Article article = this.lambdaQuery()
            .eq(Article::getId, id)
            .one();
        if (article == null) throw new ParameterException("错误的文章");
        // 2.拉取文章对应的 User
        Result result = userClient.getUserById(article.getAuthorId());
        UserVO userVO = modelMapper.map(result.getData(), UserVO.class);
        // 3.封装为 vo
        ArticleVO vo = BeanUtil.copyProperties(article, ArticleVO.class);
        vo.setUser(userVO);
        // 4.返回数据
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result getArticles(QueryMatchPage query) {
        // 1.分页查询
        Page<Article> page = new Page<>(query.getPage(), query.getSize());
        // 3.根据条件查询
        LambdaQueryWrapper<Article> wrapper = QueryMatchPageUtils.buildArticleWrapper(query);
        // 4.查询
        Page<Article> articlePage  = baseMapper.selectPage(page, wrapper);
        // 5.根据文章的作者 ID 去获取对应作者，然后封装为 VO
        PageData data = articleAndUserUtils.acquireUsersForTheArticle(articlePage);
        // 返回数据
        return Result.Builder.buildGetResult(data);
    }

    @Override
    public Result commentIncreaseById(Long id) {
        // 校验用户
        UserContext.verify();
        // 获取对应文章
        Article article = this.lambdaQuery().eq(Article::getId, id).one();
        if (article == null) throw new ParameterException("异常文章");
        // 修改数据
        article.setCommentCount(article.getCommentCount() + 1);
        this.updateById(article);
        // 返回详情
        return Result.Builder.buildUpdateResult();
    }

    @Override
    public Result getArticleByUserId(Long userId, QueryMatchPage query) {
        // 先获取用户
        Result result = userClient.getUserById(userId);
        if (result.getData() == null) throw new ParameterException("异常的用户");
        UserVO user = modelMapper.map(result.getData(), UserVO.class);
        // 构建条件
        Page<Article> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Article> wrapper = QueryMatchPageUtils.buildArticleWrapper(query);
        wrapper.eq(Article::getAuthorId, user.getId());
        // 根据条件查询文章
        Page<Article> articlePage = baseMapper.selectPage(page, wrapper);
        // 封装 VO
        List<Article> list = this.lambdaQuery()
                .eq(Article::getAuthorId, user.getId())
                .list();
        UserArticleVO vo = new UserArticleVO();
        vo.setAllArticleCount(list.size());
        vo.setAllArticleCollectCount(list.stream().map(Article::getCollectCount).reduce(0, Integer::sum));
        vo.setUser(user);
        vo.setData(
            PageData.Builder.build(
                    BeanUtil.copyToList(articlePage.getRecords(), ArticleVO.class),
                    articlePage
            )
        );
        // 返回数据
        return Result.Builder.buildGetResult(vo);
    }
}
