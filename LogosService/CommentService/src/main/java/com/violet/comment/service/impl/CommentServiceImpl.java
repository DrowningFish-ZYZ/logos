package com.violet.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.violet.api.client.ArticleClient;
import com.violet.api.domain.vo.ArticleVO;
import com.violet.comment.domain.dto.CommentFormDTO;
import com.violet.comment.domain.po.Comment;
import com.violet.comment.domain.vo.CommentVO;
import com.violet.comment.mapper.CommentMapper;
import com.violet.comment.service.ICommentService;
import com.violet.comment.utils.CommentAndUserUtils;
import com.violet.comment.utils.CommentRedisUtils;
import com.violet.common.domain.Result;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements ICommentService {

    private final ArticleClient articleClient;
    private final ModelMapper modelMapper;
    private final CommentAndUserUtils commentAndUserUtils;
    private final CommentRedisUtils commentRedisUtils;
    private final Cache<Long, Result> cache;

    @Override
    public Result createComment(CommentFormDTO dto) {
        // 1.校验用户和数据
        UserContext.verify();
        dto.verify();
        // 2.查看是否存在这么一个文章
        Result result = articleClient.getArticleById(dto.getArticleId());
        if (result.getData() == null) throw new ParameterException("异常文章");
        ArticleVO article = modelMapper.map(result.getData(), ArticleVO.class);
        // 3.评论入库
        Comment comment = new Comment();
        comment.setArticleId(article.getId());
        comment.setUserId(UserContext.getUserId());
        comment.setCreateTime(LocalDateTime.now());
        comment.setContent(dto.getContent());
        boolean save = this.save(comment);
        if (save) {
            // 入库成功、修改评论量
            articleClient.commentIncreaseById(article.getId());
            // 将数据添加到 Redis 中
            List<CommentVO> vos = commentAndUserUtils.parse(comment);
            commentRedisUtils.saveComments(vos, article.getId());
            // 删除旧的 JVM
            cache.invalidate(article.getId());
        }
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getCommentsByArticleId(Long articleId) {
        // 如果Redis中存在这条数据那么就直接返回, 格式: [comments:${articleId} type: list]
        List<Object> commentList = commentRedisUtils.getComments(articleId);
        if (!commentList.isEmpty()) return Result.Builder.buildGetResult(commentList);
        // 查看是否存在这么一个文章
        Result result = articleClient.getArticleById(articleId);
        if (result.getData() == null) throw new ParameterException("异常文章");
        ArticleVO article = modelMapper.map(result.getData(), ArticleVO.class);
        // 根据文章ID获取评论
        List<Comment> comments = this.lambdaQuery()
                .eq(Comment::getArticleId, article.getId())
                .orderByDesc(Comment::getCreateTime)
                .list();
        // 将评论和用户解析封装为 vo
        List<CommentVO> vos = commentAndUserUtils.parse(comments);
        // 封装完毕、将数据转换成 JSON 存入 Redis
        commentRedisUtils.saveComments(vos, article.getId());
        // 返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result deleteCommentByArticleId(Long articleId) {
        // 校验用户
        UserContext.verify();
        // 删除
        this.lambdaUpdate()
            .eq(Comment::getArticleId, articleId)
            .remove();
        // 删除缓存
        commentRedisUtils.remove(articleId);
        cache.invalidate(articleId);
        // 返回详情
        return Result.Builder.buildDeleteResult();
    }
}
