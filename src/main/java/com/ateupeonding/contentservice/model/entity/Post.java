package com.ateupeonding.contentservice.model.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "post", schema = "ateupeonding_content")
@Entity(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4096)
    private String content;

    @Column(nullable = false)
    private UUID projectId;
    @Column(columnDefinition = "now()")
    private OffsetDateTime createdTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public OffsetDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(OffsetDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
