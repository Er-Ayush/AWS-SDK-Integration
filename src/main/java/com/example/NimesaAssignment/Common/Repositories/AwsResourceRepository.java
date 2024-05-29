package com.example.NimesaAssignment.Common.Repositories;

import com.example.NimesaAssignment.Common.Models.AwsResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwsResourceRepository extends MongoRepository<AwsResource, String> {
    List<AwsResource> findByType(String type);
    long countByTypeAndResourceIdStartingWith(String type, String resourceId);
    List<AwsResource> findByTypeAndResourceIdStartingWith(String type, String resourceId);
}
