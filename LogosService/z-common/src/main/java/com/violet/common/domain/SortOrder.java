package com.violet.common.domain;

public class SortOrder {

    /**
     * 根据创建时间排序
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 根据收藏数排序
     */
    public static final String COLLECT_COUNT = "collectCount";

    /**
     * 根据评论数排序
     */
    public static final String COMMENT_COUNT = "commentCount";

    /**
     * 最热们排序：评论数+收藏数
     */
    public static final String HOT = "hot";

}
