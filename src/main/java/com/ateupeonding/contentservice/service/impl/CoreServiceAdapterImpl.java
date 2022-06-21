package com.ateupeonding.contentservice.service.impl;

import com.ateupeonding.contentservice.configuration.CoreServiceProperties;
import com.ateupeonding.contentservice.model.dto.ProjectDto;
import com.ateupeonding.contentservice.model.error.ContentServiceException;
import com.ateupeonding.contentservice.service.api.CoreServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CoreServiceAdapterImpl implements CoreServiceAdapter {
    private static final String REQUEST_PATTERN = "%s/%s";

    @Autowired
    private CoreServiceProperties properties;

    @Autowired
    @Qualifier("coreServiceRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public ProjectDto getById(UUID id) {
        try {
            ResponseEntity<ProjectDto> response =
                    restTemplate.getForEntity(String.format(REQUEST_PATTERN,
                            properties.getProjectsApi(), id), ProjectDto.class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            throw new ContentServiceException(ex);
        }
    }
}
