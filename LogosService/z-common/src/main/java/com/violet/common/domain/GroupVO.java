package com.violet.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GroupVO implements Serializable {
    private Long id;
    private String name;
    private String desc;
    private Integer count;
    private LocalDateTime createTime;
}
