package com.ateupeonding.contentservice.service.api;

import com.ateupeonding.contentservice.model.dto.ProjectDto;

import java.util.UUID;

public interface CoreServiceAdapter {

    ProjectDto getById(UUID id);

}
