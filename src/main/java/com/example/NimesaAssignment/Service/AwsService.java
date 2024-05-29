package com.example.NimesaAssignment.Service;

import com.example.NimesaAssignment.Common.Models.AwsResource;
import com.example.NimesaAssignment.Common.Models.Job;
import com.example.NimesaAssignment.Common.Repositories.AwsResourceRepository;
import com.example.NimesaAssignment.Common.Repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AwsService {

    @Autowired
    private Ec2Client ec2Client;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private AwsResourceRepository awsResourceRepository;

    @Autowired
    private JobRepository jobRepository;

    @Async
    public CompletableFuture<String> discoverEC2Instances() {
        return CompletableFuture.supplyAsync(() -> {
            DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            List<Instance> instances = response.reservations().stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .collect(Collectors.toList());

            List<AwsResource> awsResources = instances.stream()
                    .map(instance -> new AwsResource("EC2", instance.instanceId()))
                    .collect(Collectors.toList());
            awsResourceRepository.saveAll(awsResources);

            return createJob("EC2 Discovery Completed");
        });
    }

    @Async
    public CompletableFuture<String> discoverS3Buckets() {
        return CompletableFuture.supplyAsync(() -> {
            ListBucketsResponse response = s3Client.listBuckets();

            List<Bucket> buckets = response.buckets();

            List<AwsResource> awsResources = buckets.stream()
                    .map(bucket -> new AwsResource("S3", bucket.name()))
                    .collect(Collectors.toList());
            awsResourceRepository.saveAll(awsResources);

            return createJob("S3 Buckets Discovery Completed");
        });
    }

    @Async
    public CompletableFuture<String> discoverS3BucketObjects(String bucketName, String jobId) {
        return CompletableFuture.supplyAsync(() -> {
            ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).build();
            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            List<S3Object> objects = response.contents();

            List<AwsResource> awsResources = objects.stream()
                    .map(object -> new AwsResource("S3_OBJECT", object.key()))
                    .collect(Collectors.toList());
            awsResourceRepository.saveAll(awsResources);

            updateJobStatus(jobId, "S3 Objects Discovery Completed");
            return jobId;
        });
    }

    private String createJob(String status) {
        Job job = new Job();
        job.setStatus(status);
        jobRepository.save(job);
        return job.getId();
    }

    private void updateJobStatus(String jobId, String status) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job != null) {
            job.setStatus(status);
            jobRepository.save(job);
        }
    }
}