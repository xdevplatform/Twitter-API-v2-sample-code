# This script uses your bearer token to authenticate and make a request to the User Tweets timeline endpoint.

#Based on recent search example and updated for User mention timeline.
#Next steps: 
# [] Refactor for User Tweets timeline..
# [] Refactor for Full-archive search.

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL for the Recent Search API
endpoint_url = "https://api.twitter.com/2/users/:id/followers"

# Specify the User ID for this request.
id = 17200003

# Add or remove parameters below to adjust the query and response fields within the payload
# TODO: See docs for list of param options: https://developer.twitter.com/en/docs/twitter-api/tweets/
query_params = {
  "max_results" => 1000,
  "tweet.fields" => "created_at"
}

def get_followers(url, bearer_token, query_params)
  options = {
    method: 'get',
    headers: {
      "User-Agent" => "v2RubyExampleCode",
      "Authorization" => "Bearer #{bearer_token}",
    },
    params: query_params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

endpoint_url.gsub(':id',id.to_s())

response = get_user_tweets(endpoint_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
