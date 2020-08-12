# This script connects to the Sample stream endpoint and outputs data

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

stream_url = "https://api.twitter.com/2/tweets/sample/stream"

# Add or remove optional parameters values from the params object below. Full list of parameters and their values can be found in the docs:
# https://developer.twitter.com/en/docs/twitter-api/tweets/sampled-stream/api-reference/get-tweets-sample-stream
params = {
  # "expansions": "author_id,referenced_tweets.id",
  "tweet.fields": "attachments,author_id,conversation_id,created_at,entities,geo,id,in_reply_to_user_id,lang",
  # "user.fields": "name"
  # "media.fields": "url", 
  # "place.fields": "country_code",
  # "poll.fields": "options"
}

def stream_connect(url, bearer_token, params)
  options = {
    timeout: 20,
    method: 'get',
    headers: {
      "User-Agent": "v2SampledStreamRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: params
  }

  request = Typhoeus::Request.new(url, options)
  request.on_body do |chunk|
    puts chunk
  end
  request.run
end

# Listen to the stream.
# This reconnection logic will attempt to reconnect when a disconnection is detected.
# To avoid rate limites, this logic implements exponential backoff, so the wait time
# will increase if the client cannot reconnect to the stream.
timeout = 0
while true
  stream_connect(stream_url, bearer_token, params)
  sleep 2 ** timeout
  timeout += 1
end