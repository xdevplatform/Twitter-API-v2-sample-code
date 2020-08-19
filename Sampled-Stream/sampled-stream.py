import json
import os

import requests

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'


def create_headers(bearer_token):
    """
    Create Headers for Twitter Connection
    """
    headers = {"Authorization": "Bearer {}".format(bearer_token)}
    return headers


def get_rules(headers):
    """
    Get active rules 
    """
    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream/rules", headers=headers
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot get rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    rules = response.json()
    return rules


def delete_rules(headers, rules):
    """
    Delete rules 
    """
    if rules is None or "data" not in rules:
        return None

    ids = [rule["id"] for rule in rules["data"]]
    payload = {"delete": {"ids": ids}}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        headers=headers,
        json=payload,
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot delete rules (HTTP {}): {}".format(
                response.status_code, response.text
            )
        )
    deleted = response.json()
    return deleted


def set_rules(headers, rules):
    """
    Set rules for streaming
    """
    payload = {"add": rules}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        headers=headers,
        json=payload,
    )
    if response.status_code != 201:
        raise Exception(
            "Cannot add rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))


def get_stream(headers):
    """
    Start Streaming
    """
    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream", headers=headers, stream=True,
    )
    print(response.status_code)
    if response.status_code != 200:
        raise Exception(
            "Cannot Stream (HTTP {}): {}".format(response.status_code, response.text)
        )
    for response_line in response.iter_lines():
        if response_line:
            json_response = json.loads(response_line)
            print(json.dumps(json_response, indent=4, sort_keys=True))


def main():
    """
    This sample code does five things:
    - Creates headers for connection with Twitter's API,
    - Gets previous applied rules for streaming
    - Deletes this rules
    - Sets new rules
    - Starts streaming
    """

    bearer_token = os.environ.get("BEARER_TOKEN")
    headers = create_headers(bearer_token)

    # Gets previous applied rules and delete them
    previous_rules = get_rules(headers)
    delete_rules(headers, previous_rules)

    # Sets new rules
    new_rules = [
        {"value": "dog has:images", "tag": "dog pictures"},
        {"value": "cat has:images -grumpy", "tag": "cat pictures"},
    ]
    set_rules(headers, new_rules)

    # Starts Streaming
    get_stream(headers)


if __name__ == "__main__":
    main()
