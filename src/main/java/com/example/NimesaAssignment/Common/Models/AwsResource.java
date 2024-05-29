package com.example.NimesaAssignment.Common.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aws_resources")
public class AwsResource {
    @Id
    private String id;
    private String jobId;
    private String type;
    private String resourceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public AwsResource() {
    }

    // Constructor with parameters
    public AwsResource(String type, String resourceId) {
        this.type = type;
        this.resourceId = resourceId;
    }

}