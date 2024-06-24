import requests
import os
import json

# To set your environment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'
bearer_token = os.environ.get("BEARER_TOKEN")

compliance_job_url = "https://api.twitter.com/2/compliance/jobs"

# For User Compliance Job, replace the type value with users instead of tweets
# Also replace the name value with your desired job name
body = {"type": "tweets", "name": "my_batch_compliance_job"}


def bearer_oauth(r):
    """
    Method required by bearer token authentication.
    """

    r.headers["Authorization"] = f"Bearer {bearer_token}"
    r.headers["User-Agent"] = "v2BatchCompliancePython"
    return r


def connect_to_endpoint(url, params):
    response = requests.request("POST", url, auth=bearer_oauth, json=params)
    print(response.status_code)
    if response.status_code != 200:
        raise Exception(response.status_code, response.text)
    return response.json()


def main():
    json_response = connect_to_endpoint(compliance_job_url, body)
    print(json.dumps(json_response, indent=4, sort_keys=True))


if __name__ == "__main__":
    main()
