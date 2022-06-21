package com.ateupeonding.contentservice.service.impl;

import com.ateupeonding.contentservice.model.ReferenceType;
import com.ateupeonding.contentservice.model.entity.File;
import com.ateupeonding.contentservice.model.error.ResourceNotFoundException;
import com.ateupeonding.contentservice.repository.FileRepository;
import com.ateupeonding.contentservice.service.api.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @Override
    public File create(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File update(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void deleteById(UUID id) {
        fileRepository.deleteById(id);
    }

    @Override
    public void deleteByPostId(UUID postId) {
        fileRepository.deleteAllByRefIdAndRefType(postId, ReferenceType.POST);
    }

    @Override
    public void deleteByAccountId(UUID accountId) {
        fileRepository.deleteAllByRefIdAndRefType(accountId, ReferenceType.ACCOUNT);
    }

    @Override
    public void deleteByProjectId(UUID projectId) {
        fileRepository.deleteAllByRefIdAndRefType(projectId, ReferenceType.PROJECT);
    }

    @Override
    public File getById(UUID id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("file", "id", id));
    }

    @Override
    public List<File> getByPostId(UUID postId) {
        return fileRepository.findAllByRefIdAndRefType(postId, ReferenceType.POST);
    }

    @Override
    public File getByResource(String resource) {
        return fileRepository.findByResource(resource);
    }

    @Override
    public File getByProjectId(UUID projectId) {
        List<File> files = fileRepository.findAllByRefIdAndRefType(projectId, ReferenceType.PROJECT);
        File file = files.isEmpty() ? null : files.get(0);
        if (file == null) {
            throw new ResourceNotFoundException("file", "projectId", projectId);
        }
        return file;
    }

    @Override
    public File getByAccountId(UUID accountId) {
        List<File> files = fileRepository.findAllByRefIdAndRefType(accountId, ReferenceType.ACCOUNT);
        return files.stream().findAny().orElseThrow(() -> new ResourceNotFoundException("file", "accountId", accountId));
    }

}
