# Batch Compliance sample code

This folder contains scripts to connect to the [Batch compliance endpoints](https://developer.twitter.com/en/docs/twitter-api/compliance/batch-compliance/introduction) in Python. Make sure you have your BEARER_TOKEN set up as an environment variable. Also, make sure you have the appropriate dependencies installed by running `mvn install` in the folder that you have the pom.xml in. Then, run the scripts in the following order:

## 1. Create compliance job

First, run the `CreateJob` file. In this file, make sure to specify your `type` as `tweets` or `users`, based on whether you will uploading a dataset with Tweet IDs or User IDs. This will give you a response like:

```json
{
    "data": {
        "resumable": false,
        "upload_url": "https://storage.googleapis.com/...",
        "download_expires_at": "2021-08-12T15:24:31.000Z",
        "download_url": "https://storage.googleapis.com/...",
        "status": "created",
        "name": "my_job",
        "upload_expires_at": "2021-08-05T15:39:31.000Z",
        "id": "XXXXX",
        "created_at": "2021-08-05T15:24:31.000Z",
        "type": "tweets"
    }
}
```

Note the `id` from here as well as well as the upload_url, as this is the URL you will upload the file with Tweet IDs or User IDs to.

## 2. Upload the Tweet IDs or User IDs to check for compliance

Next, you will upload the file with Tweet IDs or User IDs to check for compliance. To do this, you will run the `UploadDataset` file. Make sure to replace the `uploadUrl` with your `upload_url` from the previous step and make sure to specify the path to your text file that contains the Tweet IDs or User IDs, one ID per line.

## 3. Check the status of your compliance job

Now that you have uploaded your dataset to check for compliance, your status will be in_progress. You can download the result of your compliance job, once the `status` is complete. To check for the status of your job, there are two options:

### Get all jobs

In order to get all your jobs, you can run `GetJobs`. Make sure to specify the appropriate job_type in this file i.e. tweets or users

### Get job by job ID

You can also get the job information for a job using the `id` from obtained in step 1. Run `GetJobById` and make sure to replace the jobId with your job id obtained in step 1. This will give you your job status and the `download_url`.

```json
{
    "data": {
        "resumable": false,
        "upload_url": "https://storage.googleapis.com/...",
        "download_expires_at": "2021-08-12T02:34:13.000Z",
        "download_url": "https://storage.googleapis.com/...",
        "status": "expired",
        "upload_expires_at": "2021-08-05T02:49:13.000Z",
        "id": "XXXXX",
        "created_at": "2021-08-05T02:34:13.000Z",
        "type": "tweets"
    }
}
```

Once your `status` changes from `in_progress` to `complete`, note the `download_url` and go to the next step to download your results

## 4. Download your results

In order to download your results, you will need the `download_url` from the previous step (once the job status is complete). Run the `DownloadResult` file and make sure to replace the `downloadUrl` value with your appropriate value obtained from the previous step.