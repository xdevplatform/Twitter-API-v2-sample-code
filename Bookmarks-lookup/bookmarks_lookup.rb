require 'json'
require 'typhoeus'
require 'twitter_oauth2'

# First, you will need to enable OAuth 2.0 in your App’s auth settings in the Developer Portal to get your client ID.
# Inside your terminal you will need to set an enviornment variable
# export CLIENT_ID='your-client-id'
client_id = ENV["CLIENT_ID"]

# If you have selected a type of App that is a confidential client you will need to set a client secret.
# Confidential Clients securely authenticate with the authorization server.

# Inside your terminal you will need to set an enviornment variable
# export CLIENT_SECRET='your-client-secret'

# Remove the comment on the following line if you are using a confidential client
# client_secret = ENV["CLIENT_SECRET"]


# Replace the following URL with your callback URL, which can be obtained from your App's auth settings.
redirect_uri = "https://www.example.com"

# Start an OAuth 2.0 session with a public client
client = TwitterOAuth2::Client.new(
  identifier: "#{client_id}",
  redirect_uri: "#{redirect_uri}"
)

# Start an OAuth 2.0 session with a confidential client

# Remove the comment on the following lines if you are using a confidential client
# client = TwitterOAuth2::Client.new(
#   identifier: "#{client_id}",
#   secret: "#{client_secret}",
#   redirect_uri: "#{redirect_uri}"
# )

# Create your authorize url
authorization_url = client.authorization_uri(
  # Update scopes if needed
  scope: [
    :'users.read',
    :'tweet.read',
    :'bookmark.read',
    :'offline.access'
  ]
)

# Set code verifier and state
code_verifier = client.code_verifier
state = client.state

# Visit the URL to authorize your App to make requests on behalf of a user
print 'Visit the following URL to authorize your App on behalf of your Twitter handle in a browser'
puts authorization_url
`open "#{authorization_url}"`

print 'Paste in the full URL after you authorized your App: ' and STDOUT.flush

# Fetch your access token
full_text = gets.chop
new_code = full_text.split("code=")
code = new_code[1]
client.authorization_code = code

# Your access token
token_response = client.access_token! code_verifier

# Make a request to the users/me endpoint to get your user ID
def users_me(url, token_response)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "BookmarksSampleCode",
      "Authorization": "Bearer #{token_response}"
    },
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

url = "https://api.twitter.com/2/users/me"
me_response = users_me(url, token_response)

json_s = JSON.parse(me_response.body)
user_id = json_s["data"]["id"]

# Make a request to the bookmarks url
bookmarks_url = "https://api.twitter.com/2/users/#{user_id}/bookmarks"

def bookmarked_tweets(bookmarks_url, token_response)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "BookmarksSampleCode",
      "Authorization": "Bearer #{token_response}"
    }
  }

  request = Typhoeus::Request.new(bookmarks_url, options)
  response = request.run

  return response
end

response = bookmarked_tweets(bookmarks_url, token_response)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
