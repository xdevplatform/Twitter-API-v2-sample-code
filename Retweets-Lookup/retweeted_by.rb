# This script uses your bearer token to authenticate and retrieve the specified User objects (by ID)

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]


# You can replace the ID given with the Tweet ID you wish to lookup Retweeting users for
# You can find an ID by using the Tweet lookup endpoint
id = "1412865600439738368"

url = "https://api.twitter.com/2/tweets/#{id}/retweeted_by"

params = {
  # User fields are adjustable, options include:
  # created_at, description, entities, id, location, name,
  # pinned_tweet_id, profile_image_url, protected,
  # public_metrics, url, username, verified, and withheld
  "user.fields": "created_at,description",
}

def retweeted_by_users(url, bearer_token, params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2RetweetedbyUsersRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = retweeted_by_users(url, bearer_token, params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
