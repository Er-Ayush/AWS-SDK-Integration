package com.example.NimesaAssignment.Controller;

import com.example.NimesaAssignment.Common.Models.AwsResource;
import com.example.NimesaAssignment.Common.Models.Job;
import com.example.NimesaAssignment.Common.Repositories.AwsResourceRepository;
import com.example.NimesaAssignment.Common.Repositories.JobRepository;
import com.example.NimesaAssignment.Service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/aws")
    public class AwsController {

        @Autowired
        private AwsService awsService;

        @Autowired
        private JobRepository jobRepository;

        @Autowired
        private AwsResourceRepository awsResourceRepository;

//  1.DiscoverServices(List<String> services)

        @PostMapping("/discoverServices")
        public String discoverServices(@RequestBody List<String> services) {
            String jobId = null;
            for (String service : services) {
                if ("EC2".equalsIgnoreCase(service)) {
                    jobId = awsService.discoverEC2Instances().join();
                } else if ("S3".equalsIgnoreCase(service)) {
                    jobId = awsService.discoverS3Buckets().join();
                }
            }
            return jobId;
        }

//  2. GetJobResult(Jobid)

        @GetMapping("/getJobResult/{jobId}")
        public String getJobResult(@PathVariable String jobId) {
            Job job = jobRepository.findById(jobId).orElse(null);
            return (job != null) ? job.getStatus() : "Job not found";
        }

//  3. GetDiscoveryResult(String Service)

        @GetMapping("/getDiscoveryResult/{service}")
        public List<AwsResource> getDiscoveryResult(@PathVariable String service) {
            return awsResourceRepository.findByType(service.toUpperCase());
        }

//  4. GetS3BucketObjects(String BucketName)

        @PostMapping("/getS3BucketObjects")
        public String getS3BucketObjects(@RequestParam String bucketName) {
            // Implement logic to discover objects in the specified bucket and save to DB
            Job job = new Job();
            job.setStatus("In Progress");
            jobRepository.save(job);

            String jobId = awsService.discoverS3BucketObjects(bucketName, job.getId()).join();

            return jobId;
        }

//  5. GetS3BucketObjectCount(String BucketName)

        @GetMapping("/getS3BucketObjectCount/{bucketName}")
        public long getS3BucketObjectCount(@PathVariable String bucketName) {
            return awsResourceRepository.countByTypeAndResourceIdStartingWith("S3", bucketName + "/");
        }

//   6. GetS3BucketObjectlike(String BucketName, String Pattern)

        @GetMapping("/getS3BucketObjectlike/{bucketName}/{pattern}")
        public List<AwsResource> getS3BucketObjectlike(@PathVariable String bucketName, @PathVariable String pattern) {
            return awsResourceRepository.findByTypeAndResourceIdStartingWith("S3", bucketName + "/" + pattern);
        }
    }

