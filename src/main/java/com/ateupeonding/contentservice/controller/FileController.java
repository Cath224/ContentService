package com.ateupeonding.contentservice.controller;

import com.ateupeonding.contentservice.mapper.FileMapper;
import com.ateupeonding.contentservice.model.dto.FileDto;
import com.ateupeonding.contentservice.model.entity.File;
import com.ateupeonding.contentservice.service.api.FileResourceService;
import com.ateupeonding.contentservice.service.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ateupeonding.contentservice.controller.ControllerUtils.parseIdToUuid;

@RestController
@RequestMapping("content-service/api/v1/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileMapper fileMapper;

    @GetMapping("{id}")
    public FileDto getFile(@PathVariable String id) {
        UUID parsedFileId = parseIdToUuid(id);
        return fileMapper.toDto(fileService.getById(parsedFileId));
    }

    @GetMapping()
    public List<FileDto> getFiles(@RequestParam String postId) {
        UUID parsedPostId = parseIdToUuid(postId);
        return fileService.getByPostId(parsedPostId).stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList());
    }


}
