# This script implements the PIN-based OAuth flow to obtain access tokens for a user context request
# It then makes a User lookup request (by usernames) with OAuth 1.0a authentication (user context)

require 'oauth'
require 'json'
require 'typhoeus'
require 'oauth/request_proxy/typhoeus_request'

# The code below sets the consumer key and secret from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export CONSUMER_KEY='YOUR-KEY', CONSUMER_SECRET='YOUR-SECRET'
consumer_key = ENV["CONSUMER_KEY"]
consumer_secret = ENV["CONSUMER_SECRET"]

# Be sure to replace your-user-id with your own user ID or one of an authenticating user
# You can find a user ID by using the user lookup endpoint
source_user_id = "your-user-id"

# Be sure to add replace id-to-unblock with the user id of the user you wish to unblock.
# You can find a user ID by using the user lookup endpoint
target_user_id = "id-to-unblock"

# Returns a user object for one or more users specified by the requested usernames
user_unblock_url = "https://api.twitter.com/2/users/#{source_user_id}/blocking/#{target_user_id}"

consumer = OAuth::Consumer.new(consumer_key, consumer_secret,
	                                :site => 'https://api.twitter.com',
	                                :authorize_path => '/oauth/authenticate',
	                                :debug_output => false)

def get_request_token(consumer)

	request_token = consumer.get_request_token()

  return request_token
end

def get_user_authorization(request_token)
	puts "Follow this URL to have a user authorize your app: #{request_token.authorize_url()}"
	puts "Enter PIN: "
	pin = gets.strip

  return pin
end

def obtain_access_token(consumer, request_token, pin)
	token = request_token.token
	token_secret = request_token.secret
	hash = { :oauth_token => token, :oauth_token_secret => token_secret }
	request_token  = OAuth::RequestToken.from_hash(consumer, hash)

	# Get access token
	access_token = request_token.get_access_token({:oauth_verifier => pin})

	return access_token
end


def user_unblock(url, oauth_params)
	options = {
	    :method => :delete,
	    headers: {
	     	"User-Agent": "v2UnblockUserRuby"
	    }
	}
	request = Typhoeus::Request.new(url, options)
	oauth_helper = OAuth::Client::Helper.new(request, oauth_params.merge(:request_uri => url))
	request.options[:headers].merge!({"Authorization" => oauth_helper.header}) # Signs the request
	response = request.run

	return response
end

# PIN-based OAuth flow - Step 1
request_token = get_request_token(consumer)
# PIN-based OAuth flow - Step 2
pin = get_user_authorization(request_token)
# PIN-based OAuth flow - Step 3
access_token = obtain_access_token(consumer, request_token, pin)

oauth_params = {:consumer => consumer, :token => access_token}

response = user_unblock(user_unblock_url, oauth_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))

# Note: If you were following the user you blocked, you have removed your follow and will have to refollow the user, even if you did unblock the user.
