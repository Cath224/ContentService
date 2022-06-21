package com.ateupeonding.contentservice.model;

import io.minio.GetObjectResponse;

import java.io.FilterInputStream;

public class FileContent extends FilterInputStream {

    private Long size;

    public FileContent(GetObjectResponse objectResponse, Long size) {
        super(objectResponse);
        this.size = size;
    }


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
