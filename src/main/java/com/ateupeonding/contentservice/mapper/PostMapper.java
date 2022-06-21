package com.ateupeonding.contentservice.mapper;

import com.ateupeonding.contentservice.model.dto.PostDto;
import com.ateupeonding.contentservice.model.entity.File;
import com.ateupeonding.contentservice.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {



    Post toEntity(PostDto dto);
    PostDto toDto(Post entity);

    default Set<String> toResourcesSet(List<File> files) {
        if (files == null || files.isEmpty()) {
            return null;
        }
        return files.stream()
                .map(File::getResource)
                .collect(Collectors.toSet());
    }

    default List<File> toFilesList(Set<String> resources) {
        return null;
    }

}
