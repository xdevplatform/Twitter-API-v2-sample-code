# This script uses your bearer token to authenticate and retrieve the specified Tweet objects (by ID)

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

tweet_lookup_url = "https://api.twitter.com/2/tweets"

# Specify the Tweet IDs that you want to lookup below (to 100 per request)
tweet_ids = "1261326399320715264,1278347468690915330"

# Add or remove optional parameters values from the params object below. Full list of parameters and their values can be found in the docs:
# https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/api-reference
params = {
	"ids": tweet_ids,
  # "expansions": "author_id,referenced_tweets.id",
  "tweet.fields": "attachments,author_id,conversation_id,created_at,entities,geo,id,in_reply_to_user_id,lang",
  # "user.fields": "name"
  # "media.fields": "url", 
  # "place.fields": "country_code",
  # "poll.fields": "options"
}

def tweet_lookup(url, bearer_token, params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2TweetLookupRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = tweet_lookup(tweet_lookup_url, bearer_token, params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
