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
@RequestMapping("content-service/api/v1/accountsResources")
public class AccountResourceController {

    @Autowired
    private FileResourceService fileResourceService;

    @PostMapping(value = "{accountId}/resources", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadResource(@PathVariable String accountId, @RequestParam("file") MultipartFile file) {
        fileResourceService.createWithAccountId(parseIdToUuid(accountId), file);
    }

    @GetMapping(value = "{accountId}/resources")
    public void downloadResource(@PathVariable String accountId, HttpServletResponse httpServletResponse) {
        try (FileContent content = fileResourceService.getByAccountId(parseIdToUuid(accountId)))  {
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
