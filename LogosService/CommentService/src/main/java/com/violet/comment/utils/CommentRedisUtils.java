package com.violet.comment.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.violet.comment.domain.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentRedisUtils {

    private final StringRedisTemplate redisTemplate;
    private Gson gson = new Gson();
    private Type commentType = new TypeToken<CommentVO>() {}.getType();

    /**
     * 将文章评论缓存到 Redis 当中
     * @param comments 要缓存的评论
     * @param articleId 对应的文章ID
     */
    public void saveComments(List<CommentVO> comments, Long articleId) {
        for (CommentVO vo : comments) {
            String json = gson.toJson(vo);
            redisTemplate.opsForList().leftPush("comments:"+articleId, json);
        }
    }

    /**
     * 获取对应文章的评论缓存数据、如果没用返回空的集合
     * @param articleId 文章ID
     * @return
     */
    public List<Object> getComments(Long articleId) {
        if (redisTemplate.hasKey("comments:"+articleId)) {
            return redisTemplate.opsForList().range("comments:" + articleId, 0, -1).stream()
                    .map(json -> gson.fromJson(json, commentType))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 删除对应文章的缓存
     * @param articleId 文章 ID
     */
    public void remove(Long articleId) {
        redisTemplate.delete("comments:"+articleId);
    }
}
