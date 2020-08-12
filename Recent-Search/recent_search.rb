# This script uses your bearer token to authenticate and make a Search request

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL for the Recent Search API
search_url = "https://api.twitter.com/2/tweets/search/recent"

# Set the query value here. Value can be up to 512 characters
query = "from:Twitter OR from:TwitterDev OR from:DailyNasa"

# Add or remove parameters below to adjust the query and response fields within the payload
# See docs for list of param options: https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-recent
query_params = {
  "query": query, # Required
  "max_results": 10,
  # "start_time": "2020-07-01T00:00:00Z",
  # "end_time": "2020-07-02T18:00:00Z",
  # "expansions": "attachments.poll_ids,attachments.media_keys,author_id",
  "tweet.fields": "attachments,author_id,conversation_id,created_at,entities,id,lang",
  "user.fields": "description"
  # "media.fields": "url", 
  # "place.fields": "country_code",
  # "poll.fields": "options"
}

def search_tweets(url, bearer_token, query_params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2RecentSearchRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: query_params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = search_tweets(search_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
