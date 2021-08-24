# This script uses your bearer token to authenticate and make a Spaces lookup request

require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on macOS, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL
spaces_search_url = "https://api.twitter.com/2/spaces/search"

# Replace this value with your search term
query = "NBA"

# Add or remove parameters below to adjust the query and response fields within the payload
# See docs for list of param options: https://developer.twitter.com/en/docs/twitter-api/spaces/search/api-reference
query_params = {
  "query": query, # Required
  "space.fields": 'title,created_at', 
  'expansions': 'creator_id'
}

def get_space(url, bearer_token, query_params)
  options = {
    method: 'get',
    headers: {
      "User-Agent": "v2SpacesSearchRuby",
      "Authorization": "Bearer #{bearer_token}"
    },
    params: query_params
  }

  request = Typhoeus::Request.new(url, options)
  response = request.run

  return response
end

response = get_space(spaces_search_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
