from requests_oauthlib import OAuth2Session
from datetime import datetime
from random import randint


def create_url_start():
    # You will need to replace where it says client_id to your own. This credential can be generated in the Dev Portal.
    client_id = r"client_id"
    # Replace with your own redirect_uri which is same as your callback url.
    redirect_uri = "https://oauth-playground.glitch.me/oauth"
    # You can update this to include of any of the available scopes
    scope = ["tweet.read", "users.read"]
    oauth = OAuth2Session(client_id, scope=scope, redirect_uri=redirect_uri)
    auth_base_url = "https://twitter.com/i/oauth2/authorize"
    oath_base = oauth.authorization_url(auth_base_url)
    return oath_base[0]


def spliting():
    url_start = create_url_start()
    string_split = url_start.split("&state=", 1)[0]
    return string_split


def code_challenge():
    now = datetime.now()
    timestamp = now.strftime("%m%d%Y%H%M%S")
    random_append = randint(1000, 100000)
    rand_dec = randint(300, 800)
    ser = "{}{}.{}".format(timestamp, random_append, rand_dec)
    return ser


def main():
    chal = code_challenge()
    string_split = spliting()
    formated = "{}&state={}".format(string_split, chal)
    final = formated + "&code_challenge=challenge&code_challenge_method=plain"
    print(final)


if __name__ == "__main__":
    main()
