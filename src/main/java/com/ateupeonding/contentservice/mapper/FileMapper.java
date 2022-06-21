package com.ateupeonding.contentservice.mapper;

import com.ateupeonding.contentservice.model.dto.FileDto;
import com.ateupeonding.contentservice.model.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    File toEntity(FileDto dto);

    FileDto toDto(File entity);

}
