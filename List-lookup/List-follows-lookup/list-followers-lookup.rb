# This script uses your bearer token to authenticate and retrieve followers of a List specified by List ID

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Be sure to replace list-id with any List ID
id = "list-id"
url = "https://api.twitter.com/2/lists/#{id}/followers"


params = {
    # User fields are adjustable, options include:
    # created_at, description, entities, id, location, name,
    # pinned_tweet_id, profile_image_url, protected,
    # public_metrics, url, username, verified, and withheld
  "user.fields": "created_at,verified",
}

def lists(url, bearer_token, params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2ListFollowersLookupRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = lists(url, bearer_token, params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
