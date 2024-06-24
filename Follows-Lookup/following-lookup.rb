require 'json'
require 'typhoeus'

# The code below sets the bearer token from your environment variables
# To set environment variables on macOS or Linux, run the export command below from the terminal:
# export BEARER_TOKEN='YOUR-TOKEN'
bearer_token = ENV["BEARER_TOKEN"]

# Endpoint URL for the user following API
endpoint_url = "https://api.twitter.com/2/users/:id/following"

# Specify the User ID for this request.
id = 2244994945

# Add or remove parameters below to adjust the query and response fields within the payload
query_params = {
  "max_results" => 1000,
  "user.fields" => "created_at"
}

def get_following(url, bearer_token, query_params)
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

endpoint_url = endpoint_url.gsub(':id',id.to_s())

response = get_following(endpoint_url, bearer_token, query_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
