package com.ateupeonding.contentservice.service.api;

import com.ateupeonding.contentservice.model.entity.File;

import java.util.List;
import java.util.UUID;

public interface FileService {

    File create(File file);

    File update(File file);

    void deleteById(UUID id);

    void deleteByPostId(UUID postId);

    void deleteByAccountId(UUID accountId);

    void deleteByProjectId(UUID projectId);

    File getById(UUID id);

    List<File> getByPostId(UUID postId);

    File getByResource(String resource);

    File getByProjectId(UUID projectId);

    File getByAccountId(UUID accountId);

}
