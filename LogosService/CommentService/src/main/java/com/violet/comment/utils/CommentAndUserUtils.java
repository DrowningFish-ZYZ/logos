package com.violet.comment.utils;

import cn.hutool.core.bean.BeanUtil;
import com.violet.api.client.UserClient;
import com.violet.api.domain.vo.UserVO;
import com.violet.comment.domain.po.Comment;
import com.violet.comment.domain.vo.CommentVO;
import com.violet.common.domain.Result;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentAndUserUtils {

    private final UserClient userClient;
    private final ModelMapper modelMapper;

    public List<CommentVO> parse(Comment comment) {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        return parse(comments);
    }

    public List<CommentVO> parse(List<Comment> comments) {
        List<CommentVO> commentVOList = new ArrayList<CommentVO>();
        // 根据评论的用户 ID
        Set<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());
        // 如果没有任何用户，则表示没有任何评论、直接返回空
        if (userIds.isEmpty()) return commentVOList;
        // 根据用户 ID 获取对应用户信息
        Result users = userClient.getUsersByIds(userIds);
        // 转换成 map => {userId, user}
        List list = (List) users.getData();
        Map<Long, UserVO> map = new HashMap<>();
        for (Object object : list) {
            UserVO user = modelMapper.map(object, UserVO.class);
            map.put(user.getId(), user);
        }
        // 转换成 VO
        for (Comment comment : comments) {
            UserVO user = map.get(comment.getUserId());
            CommentVO vo = BeanUtil.copyProperties(comment, CommentVO.class);
            vo.setUser(user);
            commentVOList.add(vo);
        }
        return commentVOList;
    }

}
