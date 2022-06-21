package com.ateupeonding.contentservice.service.impl;

import com.ateupeonding.contentservice.model.dto.ProjectDto;
import com.ateupeonding.contentservice.model.entity.Post;
import com.ateupeonding.contentservice.model.error.ResourceNotFoundException;
import com.ateupeonding.contentservice.repository.PostRepository;
import com.ateupeonding.contentservice.service.api.CoreServiceAdapter;
import com.ateupeonding.contentservice.service.api.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private static final String SORTING_FIELD = "createdTimestamp";
    private final PostRepository repository;
    private final CoreServiceAdapter coreServiceAdapter;

    public PostServiceImpl(PostRepository repository, CoreServiceAdapter coreServiceAdapter) {
        this.repository = repository;
        this.coreServiceAdapter = coreServiceAdapter;
    }

    @Override
    public Post create(Post post) {
        UUID projectId = post.getProjectId();
        ProjectDto projectDto = coreServiceAdapter.getById(projectId);
        if (projectDto == null) {
            throw new RuntimeException();
        }
        return repository.save(post);
    }

    @Override
    public Post update(Post post) {
        UUID projectId = post.getProjectId();
        ProjectDto projectDto = coreServiceAdapter.getById(projectId);
        if (projectDto == null) {
            throw new RuntimeException();
        }
        return repository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByProjectId(UUID projectId) {
        repository.deleteAllByProjectId(projectId);
    }

    @Override
    public Post getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
    }

    @Override
    public List<Post> getByProjectId(UUID projectId, Integer limit, Integer offset) {
        Sort sort = Sort.by(Sort.Direction.DESC, SORTING_FIELD);
        Pageable pageable = PageRequest.of(offset, limit, sort);
        return repository.findAllByProjectId(projectId, pageable);
    }


}
