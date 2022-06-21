package com.ateupeonding.contentservice.service.api;

import com.ateupeonding.contentservice.model.FileContent;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface FileResourceService {

    void create(UUID postId, MultipartFile file);

    void createWithProjectId(UUID projectId, MultipartFile file);

    void createWithAccountId(UUID projectId, MultipartFile file);

    FileContent getByProjectId(UUID projectId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    FileContent getByAccountId(UUID projectId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    FileContent getByNameAndPostId(String resourceName, UUID postId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}
