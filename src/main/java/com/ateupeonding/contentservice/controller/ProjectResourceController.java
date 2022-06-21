package com.ateupeonding.contentservice.controller;

import com.ateupeonding.contentservice.model.FileContent;
import com.ateupeonding.contentservice.service.api.FileResourceService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import static com.ateupeonding.contentservice.controller.ControllerUtils.parseIdToUuid;

@RestController
@RequestMapping("content-service/api/v1/projectsResources")
public class ProjectResourceController {

    @Autowired
    private FileResourceService fileResourceService;

    @PostMapping(value = "{projectId}/resources", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadResource(@PathVariable String projectId, @RequestParam("file") MultipartFile file) {
        fileResourceService.createWithProjectId(parseIdToUuid(projectId), file);
    }

    @GetMapping(value = "{projectId}/resources")
    public void downloadResource(@PathVariable String projectId, HttpServletResponse httpServletResponse) {
        try (FileContent content = fileResourceService.getByProjectId(parseIdToUuid(projectId)))  {
            if (content != null) {
                httpServletResponse.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.getSize()));
                IOUtils.copy(content, httpServletResponse.getOutputStream());
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
