package com.ateupeonding.contentservice.repository;

import com.ateupeonding.contentservice.model.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByProjectId(@Param("projectId") UUID projectId, Pageable pageable);

    void deleteAllByProjectId(@Param("projectId") UUID projectId);

}
