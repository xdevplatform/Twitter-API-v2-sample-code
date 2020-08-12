# This script uses your bearer token to authenticate and retrieve the specified User objects (by ID)

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

user_lookup_url = "https://api.twitter.com/2/users"

# Specify the User IDs that you want to lookup below (to 100 per request)
user_ids = "2244994945,783214"

# Add or remove optional parameters values from the params object below. Full list of parameters and their values can be found in the docs:
# https://developer.twitter.com/en/docs/twitter-api/tweets/sampled-stream/api-reference/get-tweets-sample-stream
params = {
	"ids": user_ids,
  "expansions": "pinned_tweet_id",
  "tweet.fields": "attachments,author_id,conversation_id,created_at,entities,geo,id,in_reply_to_user_id,lang",
  # "user.fields": "name"
  # "media.fields": "url", 
  # "place.fields": "country_code",
  # "poll.fields": "options"
}

def user_lookup(url, bearer_token, params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2UserLookupRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = user_lookup(user_lookup_url, bearer_token, params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
