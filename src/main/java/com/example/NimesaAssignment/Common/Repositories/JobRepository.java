package com.example.NimesaAssignment.Common.Repositories;

import com.example.NimesaAssignment.Common.Models.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobRepository extends MongoRepository<Job, String> {}
