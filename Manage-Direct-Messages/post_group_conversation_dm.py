import base64
import hashlib
import os
import re
import json
import requests
from requests_oauthlib import OAuth2Session

#This example is set up to create a new group conversation and add a new DM to it.
POST_DM_URL = "https://api.twitter.com/2/dm_conversations"

#-----------------------------------------------------------------------------------------------------------------------
# These variables need to be updated to the setting that match how your Twitter App is set-up at
# https://developer.twitter.com/en/portal/dashboard. These will not change from run-by-run.
client_id = 'dE5vYmJaa3o5QWRlNEt1T01DZ206MTpjaQ'
#This must match *exactly* the redirect URL specified in the Developer Portal.
redirect_uri = "https://www.example.com"
#-----------------------------------------------------------------------------------------------------------------------
# These variables indicate the participants of the new group conversation and the message to add. A more ready-to-be
# used example would have these passed in from some calling code.
#Who is in this group conversation? Reference their User IDs.
participant_ids = ["944480690","906948460078698496"]
#Set the text of the message to be sent.
text_message = "Hi, I am creating a new *group* conversation, and starting it with the this message."
#-----------------------------------------------------------------------------------------------------------------------

def handle_oauth():

    # Set the scopes needed to be granted by the authenticating user.
    scopes = ["dm.read", "dm.write", "tweet.read", "users.read", "offline.access"]

    # Create a code verifier.
    code_verifier = base64.urlsafe_b64encode(os.urandom(30)).decode("utf-8")
    code_verifier = re.sub("[^a-zA-Z0-9]+", "", code_verifier)

    # Create a code challenge.
    code_challenge = hashlib.sha256(code_verifier.encode("utf-8")).digest()
    code_challenge = base64.urlsafe_b64encode(code_challenge).decode("utf-8")
    code_challenge = code_challenge.replace("=", "")

    # Start an OAuth 2.0 session.
    oauth = OAuth2Session(client_id, redirect_uri=redirect_uri, scope=scopes)

    # Create an authorize URL.
    auth_url = "https://twitter.com/i/oauth2/authorize"
    authorization_url, state = oauth.authorization_url(
        auth_url, code_challenge=code_challenge, code_challenge_method="S256"
    )

    # Visit the URL to authorize your App to make requests on behalf of a user.
    print(
        "Visit the following URL to authorize your App on behalf of your Twitter handle in a browser:"
    )
    print(authorization_url)

    # Paste in your authorize URL to complete the request.
    authorization_response = input(
        "Paste in the full URL after you've authorized your App:\n"
    )

    # Fetch your access token.
    token_url = "https://api.twitter.com/2/oauth2/token"

    # The following line of code will only work if you are using a type of App that is a public client.
    auth = False

    token = oauth.fetch_token(
        token_url=token_url,
        authorization_response=authorization_response,
        auth=auth,
        client_id=client_id,
        include_client_id=True,
        code_verifier=code_verifier,
    )

    # The access token
    access = token["access_token"]

    return access

def create_new_group_conversation_with_dm(dm_text, participant_ids):
    request_body = {}

    access = handle_oauth()

    headers = {
        "Authorization": "Bearer {}".format(access),
        "Content-Type": "application/json",
        "User-Agent": "TwitterDevSampleCode",
        "X-TFE-Experiment-environment": "staging1",
        "Dtab-Local": "/s/gizmoduck/test-users-temporary => /s/gizmoduck/gizmoduck"
    }

    request_url = POST_DM_URL
    request_body['message'] = {}
    request_body['message']['text'] = dm_text
    request_body['participant_ids'] = participant_ids
    request_body['conversation_type'] = "Group"
    json_body = json.dumps(request_body)

    #Send DM
    response = requests.request("POST", request_url, headers=headers, json=json.loads(json_body))

    if response.status_code != 201:
        print("Request returned an error: {} {}".format(response.status_code, response.text))
    else:
        print(f"Response code: {response.status_code}")

    return response

def main():
    response = create_new_group_conversation_with_dm(text_message, participant_ids)
    print(json.dumps(json.loads(response.text), indent=4, sort_keys=True))

if __name__ == "__main__":
    main()