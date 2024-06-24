# This script uses your bearer token to authenticate and make a Search request

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL for the Recent Search API
search_url = "https://api.twitter.com/2/tweets/counts/recent"

# Set the query value here. Value can be up to 512 characters
query = "from:TwitterDev"

# Add or remove parameters below to adjust the query and response fields within the payload
# See docs for list of param options: https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-counts-recent
query_params = {
  "query": query, # Required
  "granularity": "day"
}

def get_tweet_counts(url, bearer_token, query_params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2RecentTweetCountsRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: query_params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = get_tweet_counts(search_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
