# This script uses your bearer token to authenticate and retrieve the Usage

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

usage_tweets_url = "https://api.twitter.com/2/usage/tweets"

def usage_tweets(url, bearer_token)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2UsageTweetsRuby",
      "Authorization": "Bearer #{bearer_token}"
    }
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = usage_tweets(usage_tweets_url, bearer_token)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
