package com.violet.comment.domain.vo;

import com.violet.api.domain.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private LocalDateTime createTime;
    private String content;
    private UserVO user;
}
