package com.violet.article.service;

import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;

public interface ICollectArticleGroupService {
    Result createGroup(GroupFormDTO dto);

    Result getGroups();

    Result getGroupById(Long id);

    Result updateGroupById(Long id, GroupFormDTO dto);
}
