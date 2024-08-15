package com.violet.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.article.domain.po.Article;
import com.violet.article.domain.po.PublishArticleGroup;
import com.violet.article.mapper.ArticleMapper;
import com.violet.article.mapper.PublishArticleGroupMapper;
import com.violet.article.service.IPublishArticleGroupService;
import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.GroupVO;
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
public class PublishArticleGroupServiceImpl extends ServiceImpl<PublishArticleGroupMapper, PublishArticleGroup>
        implements IPublishArticleGroupService {

    private final ArticleMapper articleMapper;
    private final PublishArticleServiceImpl publishArticleServiceImpl;

    @Override
    public Result createGroup(GroupFormDTO dto) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 组名不能重复
        PublishArticleGroup one = this.lambdaQuery()
            .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
            .eq(PublishArticleGroup::getName, dto.getName())
            .one();
        if (one != null) throw new ParameterException("组名不能重复");
        // 4.创建新分组
        PublishArticleGroup group = new PublishArticleGroup();
        group.setName(dto.getName());
        group.setUserId(UserContext.getUserId());
        group.setDesc(dto.getDesc());
        group.setCreateTime(LocalDateTime.now());
        // 5.入库
        this.save(group);
        // 6.返回消息
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getAllGroups() {
        // 1.校验用户
        UserContext.verify();
        // 2.获取登录用户对应的分组
        List<PublishArticleGroup> list = this.lambdaQuery()
                .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
                .list();
        // 3.封装为 VO
        List<GroupVO> vos = BeanUtil.copyToList(list, GroupVO.class);
        for (GroupVO vo : vos) {
            Integer count = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                    .eq(Article::getPublishArticleGroupId, vo.getId())
            );
            vo.setCount(count);
        }
        // 4.返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result getGroupById(Long id) {
        // 1.校验用户
        UserContext.verify();
        // 2.根据ID获取分组
        PublishArticleGroup group = this.lambdaQuery()
            .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
            .eq(PublishArticleGroup::getId, id)
            .one();
        if (group == null) throw new ParameterException("错误的分组");
        // 3.封装为 VO
        GroupVO vo = BeanUtil.copyProperties(group, GroupVO.class);
        Integer count = articleMapper.selectCount(
            new LambdaQueryWrapper<Article>()
                .eq(Article::getPublishArticleGroupId, group.getId())
        );
        vo.setCount(count);
        // 4.返回数据
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result updateGroupById(GroupFormDTO dto, Long id) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 分组存在才能修改
        PublishArticleGroup group = this.lambdaQuery()
            .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
            .eq(PublishArticleGroup::getId, id)
            .one();
        if (group == null) throw new ParameterException("错误分组");
        // 修改后的分组名不能有重复
        PublishArticleGroup one = this.lambdaQuery()
                .eq(PublishArticleGroup::getName, dto.getName())
                .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
                .ne(PublishArticleGroup::getId, group.getId())
                .one();
        if (one != null) throw new ParameterException("分组名不能重复");
        // 修改入库
        group.setName(dto.getName());
        group.setDesc(dto.getDesc());
        this.updateById(group);
        // 返回消息
        return Result.Builder.buildUpdateResult();
    }

    @Override
    public Result deletePublishArticleGroupById(Long id) {
        // 校验用户
        UserContext.verify();
        // 获取对应分组
        PublishArticleGroup group = this.lambdaQuery()
                .eq(PublishArticleGroup::getUserId, UserContext.getUserId())
                .eq(PublishArticleGroup::getId, id)
                .one();
        // 获取对应分组下的所有文章 ID
        Set<Long> articleIds = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getPublishArticleGroupId, group.getId())
        ).stream().map(Article::getId).collect(Collectors.toSet());
        // 根据文章 ID 列表删除文章
        for (Long articleId : articleIds) {
            publishArticleServiceImpl.deletePublishArticleById(articleId);
        }
        // 最后删除分组
        this.removeById(group.getId());
        // 返回详情
        return Result.Builder.buildDeleteResult();
    }
}
