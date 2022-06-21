package com.ateupeonding.contentservice.service.api;

import com.ateupeonding.contentservice.model.entity.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Post create(Post post);

    Post update(Post post);

    void deleteById(UUID id);

    void deleteByProjectId(UUID projectId);

    Post getById(UUID id);

    List<Post> getByProjectId(UUID projectId, Integer limit, Integer offset);
}
