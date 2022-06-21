package com.ateupeonding.contentservice.controller;

import com.ateupeonding.contentservice.mapper.PostMapper;
import com.ateupeonding.contentservice.model.FileContent;
import com.ateupeonding.contentservice.model.dto.PostDto;
import com.ateupeonding.contentservice.service.api.FileResourceService;
import com.ateupeonding.contentservice.service.api.PostService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ateupeonding.contentservice.controller.ControllerUtils.parseIdToUuid;

@RestController
@RequestMapping("content-service/api/v1/posts")
public class PostController {


    @Autowired
    private PostService postService;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private PostMapper postMapper;

    @PostMapping
    public PostDto create(@RequestBody PostDto postDto) {
        return postMapper.toDto(postService.create(postMapper.toEntity(postDto)));
    }


    @PutMapping("{id}")
    public PostDto update(@PathVariable String id, @RequestBody PostDto postDto) {
        UUID parsedId = parseIdToUuid(id);
        postDto.setId(parsedId);
        return postMapper.toDto(postService.update(postMapper.toEntity(postDto)));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable String id) {
        postService.deleteById(parseIdToUuid(id));
    }

    @GetMapping("{id}")
    public PostDto getById(@PathVariable String id) {
        return postMapper.toDto(postService.getById(parseIdToUuid(id)));
    }

    @GetMapping
    public List<PostDto> get(@RequestParam String projectId,
                             @RequestParam(required = false, defaultValue = "10") Integer limit,
                             @RequestParam(required = false, defaultValue = "0") Integer offset) {
        return postService.getByProjectId(parseIdToUuid(projectId), limit, offset).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "{postId}/resources", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadResource(@PathVariable String postId, @RequestParam("file") MultipartFile file) {
        fileResourceService.create(parseIdToUuid(postId), file);
    }

    @GetMapping(value = "{postId}/resources/{resourceName}")
    public void downloadResource(@PathVariable String postId, @PathVariable String resourceName, HttpServletResponse httpServletResponse) {
        try (FileContent content = fileResourceService.getByNameAndPostId(resourceName, parseIdToUuid(postId)))  {
            httpServletResponse.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.getSize()));
            IOUtils.copy(content, httpServletResponse.getOutputStream());
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }



}
