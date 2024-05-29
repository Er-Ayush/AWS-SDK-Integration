SETUP INSTRUCTIONS:

Clone the repository: git clone https://github.com/Er-Ayush/AWS-SDK-Integration.git

Database is Already created on MongoDb cloud

Navigate to the project directory: cd <project_directory>

Build the project: mvn clean install -U

then for AWS credentials, export them in the CLI using below commands:

$env:AWS_ACCESS_KEY_ID=" "

$env:AWS_SECRET_ACCESS_KEY=" "

// Inside the Double Quotes fill the acces key and secret Key

No run the Jar file using (java -jar C:\Users\Dell\Downloads\NimesaAssignment\NimesaAssignment\target\NimesaAssignment-0.0.1-SNAPSHOT.jar)

Access the application: Open a web browser or else open Postman to Hit API


API Documentation

1. DiscoverServices API
   
   Description:
   Discovers EC2 instances and S3 buckets asynchronously and persists the result in the database.
   
   Method:
   POST
   
   URL:
   http://localhost:8080/aws/discoverServices
   
   Request Body:
   {
   "services": ["EC2", "S3"]
   }
   
   Response:
   {
   "jobId": "some-job-id"
   }

   ![image](https://github.com/Er-Ayush/AWS-SDK-Integration/assets/89703959/6d265b26-7498-47aa-8ff1-7bf89cdb10f9)


******************************************************************************

2. GetJobResult API
   
   Description:
   Returns the job status for the given job ID.
   
   Method:
   GET
   
   URL:
   http://localhost:8080/aws/getJobResult/{jobId}
   
   Path Variable:
   jobId (String): The job ID for which to get the status.
   
   Response:
   {
   "status": "Success" | "In Progress" | "Failed"
   }

   ![image](https://github.com/Er-Ayush/AWS-SDK-Integration/assets/89703959/0df49e8e-260e-4b58-803f-0947a6e3a938)


******************************************************************************

3. GetDiscoveryResult API
   
   Description:
   Returns the discovery result for the specified service (EC2 or S3).

Method:
GET

URL:
http://localhost:8080/aws/getDiscoveryResult/{service}

Path Variable:
service (String): The service name (EC2 or S3).

Response:
For EC2:
[
{
"id": "some-id",
"jobId": "some-job-id",
"type": "EC2",
"resourceId": "i-1234567890abcdef0"
},
...
]

For S3:
[
{
"id": "some-id",
"jobId": "some-job-id",
"type": "S3",
"resourceId": "nimesaassignmentbucket1"
},
...
]

![image](https://github.com/Er-Ayush/AWS-SDK-Integration/assets/89703959/f4b01497-f58d-4df8-b9bc-9540eece7b0b)


******************************************************************************

4. GetS3BucketObjects API
   
   Description:
   Discovers all the file names in the specified S3 bucket and saves them in the database.

   Method:
   POST

   URL:
   http://localhost:8080/aws/getS3BucketObjects

   Request Body:
   json
   {
   "bucketName": "nimesaassignmentbucket1"
   }

   Response:
   json
   {
   "jobId": "some-job-id"
   }

******************************************************************************

5. GetS3BucketObjectCount API

   Description:
   Returns the count of objects in the specified S3 bucket.

   Method:
   GET

   URL:
   http://localhost:8080/aws/getS3BucketObjectCount/{bucketName}

   Path Variable:
   bucketName (String): The name of the S3 bucket.

   Response:
   json
   {
   "count": 123
   }

******************************************************************************

6. GetS3BucketObjectLike API

   Description:
   Returns a list of file names in the specified S3 bucket that match the given pattern.

   Method:
   GET

   URL:
   http://localhost:8080/aws/getS3BucketObjectLike/{bucketName}/{pattern}

   Path Variables:
   
   bucketName (String): The name of the S3 bucket.

   pattern (String): The pattern to match file names.

   Response:
   json
   [
   "file1.txt",
   "file2.txt",
   ...
   ]
