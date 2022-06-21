package com.ateupeonding.contentservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ateupeonding.core-service")
public class CoreServiceProperties {

    private String url;
    private String projectsApi;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectsApi() {
        return projectsApi;
    }

    public void setProjectsApi(String projectsApi) {
        this.projectsApi = projectsApi;
    }
}
