package com.ateupeonding.contentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {

    private UUID id;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
