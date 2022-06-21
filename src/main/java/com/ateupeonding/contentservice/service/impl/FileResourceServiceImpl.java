package com.ateupeonding.contentservice.service.impl;

import com.ateupeonding.contentservice.model.FileContent;
import com.ateupeonding.contentservice.model.ReferenceType;
import com.ateupeonding.contentservice.model.entity.File;
import com.ateupeonding.contentservice.model.entity.Post;
import com.ateupeonding.contentservice.model.error.ContentServiceException;
import com.ateupeonding.contentservice.service.api.FileResourceService;
import com.ateupeonding.contentservice.service.api.FileService;
import com.ateupeonding.contentservice.service.api.PostService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.minio.ObjectWriteArgs.MIN_MULTIPART_SIZE;

@Service
public class FileResourceServiceImpl implements FileResourceService {

    private static final long PART_DIVIDER = MIN_MULTIPART_SIZE + 1;

    private static final String OBJECT_NAME_FORMAT = "%s_%s_%s.%s";

    private static final String ACCOUNT_BUCKET_NAME = "account-%s";


    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioClient minioClient;

    @Override
    public void create(UUID postId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Post post = postService.getById(postId);
            UUID projectId = post.getProjectId();
            String projectIdString = projectId.toString();
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(projectIdString)
                    .build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(projectIdString)
                        .build());
            }

            long originalLength = file.getResource().contentLength();

            long length = originalLength;

            String originalFileName = file.getOriginalFilename();

            String fileName = createFileName(originalFileName, projectId);


            do {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(projectIdString)
                        .stream(is, length, PART_DIVIDER)
                        .object(fileName)
                        .build());
            } while ((length -= PART_DIVIDER) > 0);

            File fileEntity = new File();
            fileEntity.setName(originalFileName);
            fileEntity.setResource(fileName);
            fileEntity.setRefId(post.getId());
            fileEntity.setRefType(ReferenceType.POST);
            fileEntity.setSize(originalLength);
            fileService.create(fileEntity);
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException ex) {
            throw new ContentServiceException(ex);
        }
    }

    @Override
    public void createWithProjectId(UUID projectId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            String projectIdString = projectId.toString();
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(projectIdString)
                    .build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(projectIdString)
                        .build());
            } else {
                Iterable<Result<Item>> objects = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(projectIdString)
                                .build()
                );
                if (objects != null) {
                    List<DeleteObject> objectsToDelete = StreamSupport.stream(objects.spliterator(), false)
                            .map((el) -> {
                                try {
                                    Item item = el.get();
                                    return new DeleteObject(item.objectName());
                                } catch (Exception ex) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    minioClient.removeObjects(RemoveObjectsArgs.builder()
                            .objects(objectsToDelete)
                            .bucket(projectIdString)
                            .build()).forEach((el) -> {
                        try {
                            DeleteError error = el.get();
                        } catch (Exception ex) {
                            return;
                        }
                    });
                    fileService.deleteByProjectId(projectId);
                }
            }

            long originalLength = file.getResource().contentLength();

            long length = originalLength;

            String originalFileName = file.getOriginalFilename();

            String fileName = createFileName(originalFileName, projectId);


            do {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(projectIdString)
                        .stream(is, length, PART_DIVIDER)
                        .object(fileName)
                        .build());
            } while ((length -= PART_DIVIDER) > 0);

            File fileEntity = new File();
            fileEntity.setName(originalFileName);
            fileEntity.setResource(fileName);
            fileEntity.setRefId(projectId);
            fileEntity.setRefType(ReferenceType.PROJECT);
            fileEntity.setSize(originalLength);
            fileService.create(fileEntity);
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException ex) {
            throw new ContentServiceException(ex);
        }
    }

    @Override
    public void createWithAccountId(UUID accountId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            String accountIdString = accountId.toString();
            String accountBucketName = String.format(ACCOUNT_BUCKET_NAME, accountIdString);
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(accountBucketName)
                    .build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(accountBucketName)
                        .build());
            } else {
                Iterable<Result<Item>> objects = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(accountBucketName)
                                .build()
                );
                if (objects != null) {
                    List<DeleteObject> objectsToDelete = StreamSupport.stream(objects.spliterator(), false)
                            .map((el) -> {
                                try {
                                    Item item = el.get();
                                    return new DeleteObject(item.objectName());
                                } catch (Exception ex) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    minioClient.removeObjects(RemoveObjectsArgs.builder()
                                    .objects(objectsToDelete)
                                    .bucket(accountBucketName)
                                    .build()).forEach((el) -> {
                        try {
                            DeleteError error = el.get();
                        } catch (Exception ignored) {
                        }
                    });
                    fileService.deleteByAccountId(accountId);
                }
            }

            long originalLength = file.getResource().contentLength();

            long length = originalLength;

            String originalFileName = file.getOriginalFilename();

            String fileName = createFileName(originalFileName, accountId);


            do {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(accountBucketName)
                        .stream(is, length, PART_DIVIDER)
                        .object(fileName)
                        .build());
            } while ((length -= PART_DIVIDER) > 0);


            File fileEntity = new File();
            fileEntity.setName(originalFileName);
            fileEntity.setResource(fileName);
            fileEntity.setRefId(accountId);
            fileEntity.setRefType(ReferenceType.ACCOUNT);
            fileEntity.setSize(originalLength);
            fileService.create(fileEntity);
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException ex) {
            throw new ContentServiceException(ex);
        }
    }

    @Override
    public FileContent getByProjectId(UUID projectId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File file = fileService.getByProjectId(projectId);
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(projectId.toString())
                .object(file.getResource())
                .build();
        return new FileContent(minioClient.getObject(args), file.getSize());
    }

    @Override
    public FileContent getByAccountId(UUID accountId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File file = fileService.getByAccountId(accountId);
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(String.format(ACCOUNT_BUCKET_NAME, accountId.toString()))
                .object(file.getResource())
                .build();
        return new FileContent(minioClient.getObject(args), file.getSize());
    }

    @Override
    public FileContent getByNameAndPostId(String resourceName, UUID postId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Post post = postService.getById(postId);
        File file = fileService.getByResource(resourceName);
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(post.getProjectId().toString())
                .object(file.getResource())
                .build();
        return new FileContent(minioClient.getObject(args), file.getSize());
    }

    private String createFileName(String fileName, UUID projectId) {
        String extension = FilenameUtils.getExtension(fileName);
        String name = FilenameUtils.getBaseName(fileName);
        return String.format(OBJECT_NAME_FORMAT, name, projectId, UUID.randomUUID(), extension);
    }


}
