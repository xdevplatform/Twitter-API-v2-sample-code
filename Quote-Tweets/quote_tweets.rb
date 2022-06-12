# This script uses your bearer token to authenticate and make a request to the Quote Tweets endpoint.
require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL for the Quote Tweets endpoint.
endpoint_url = "https://api.twitter.com/2/tweets/:id/quote_tweets"

# Specify the Tweet ID for this request.
id = 20

# Add or remove parameters below to adjust the query and response fields within the payload
# TODO: See docs for list of param options: https://developer.twitter.com/en/docs/twitter-api/tweets/
query_params = {
  "max_results" => 100,
  "expansions" => "attachments.poll_ids,attachments.media_keys,author_id",
  "tweet.fields" => "attachments,author_id,conversation_id,created_at,entities,id,lang",
  "user.fields" => "description"
}

def get_quote_tweets(url, bearer_token, query_params)
  options = {
    method: 'get',
    headers: {
      "User-Agent" => "v2RubyExampleCode",
      "Authorization" => "Bearer #{bearer_token}"
    },
    params: query_params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

endpoint_url = endpoint_url.gsub(':id',id.to_s())

response = get_quote_tweets(endpoint_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
