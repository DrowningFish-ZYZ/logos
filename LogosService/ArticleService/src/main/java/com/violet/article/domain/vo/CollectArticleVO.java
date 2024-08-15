package com.violet.article.domain.vo;

import lombok.Data;

@Data
public class CollectArticleVO {

    private Boolean collected;
    private Long groupId;
    private Long articleId;

    public static class Builder {
        /**
         * 构建一个未收藏文章的响应数据
         * @return
         */
        public static CollectArticleVO buildNotCollected() {
            CollectArticleVO vo = new CollectArticleVO();
            vo.setCollected(false);
            return vo;
        }

        /**
         * 构建一个收藏了文章的响应数据
         * @return
         */
        public static CollectArticleVO build(Long groupId, Long articleId) {
            CollectArticleVO vo = new CollectArticleVO();
            vo.setCollected(true);
            vo.setGroupId(groupId);
            vo.setArticleId(articleId);
            return vo;
        }
    }
}
