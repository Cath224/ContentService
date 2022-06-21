package com.ateupeonding.contentservice.repository;

import com.ateupeonding.contentservice.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

    File findByResource(@Param("resource") String resource);
    List<File> findAllByRefIdAndRefType(@Param("refId") UUID refId, @Param("refType") String type);

    void deleteAllByRefIdAndRefType(@Param("refId") UUID refId, @Param("refType") String type);


}
