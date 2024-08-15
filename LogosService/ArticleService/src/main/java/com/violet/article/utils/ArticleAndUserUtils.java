package com.violet.article.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.violet.api.client.UserClient;
import com.violet.api.domain.vo.ArticleVO;
import com.violet.api.domain.vo.UserVO;
import com.violet.article.domain.po.Article;
import com.violet.common.domain.PageData;
import com.violet.common.domain.Result;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleAndUserUtils {

    private final UserClient userClient;
    private final ModelMapper modelMapper;

    public PageData acquireUsersForTheArticle(Page<Article> page) {
        // 根据文章的作者ID去获取用户
        Set<Long> userIds = page.getRecords().stream()
                .map(Article::getAuthorId)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) return PageData.Builder.build();
        // 发起请求获取用户
        Result result = userClient.getUsersByIds(userIds);
        List users = (List) result.getData();
        if (users.isEmpty()) return PageData.Builder.build();
        // 将用户列表转换成 map 格式 => {id, user}
        Map<Long, UserVO> map = new HashMap<>();
        for (Object object : users) {
            UserVO userVO = modelMapper.map(object, UserVO.class);
            map.put(userVO.getId(), userVO);
        }
        // 根据文章作者 id 封装 vo
        List<ArticleVO> vos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            ArticleVO vo = BeanUtil.copyProperties(article, ArticleVO.class);
            UserVO userVO = map.get(article.getAuthorId());
            vo.setUser(userVO);
            vos.add(vo);
        }
        // 返回数据
        return PageData.Builder.build(vos, page);
    }

}
