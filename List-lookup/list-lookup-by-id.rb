# This script uses your bearer token to authenticate and retrieve the specified List objects (by ID)

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Be sure to replace list-id with any List ID
id = "list-id"
url = "https://api.twitter.com/2/lists/#{id}"


params = {
    # List fields are adjustable, options include:
    # created_at, description, owner_id,
    # private, follower_count, member_count,
  "list.fields": "created_at,follower_count",
}

def lists(url, bearer_token, params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2ListLookupRuby",
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
