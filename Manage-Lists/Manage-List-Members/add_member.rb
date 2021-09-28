# This script implements the PIN-based OAuth flow to obtain access tokens for a user context request
require 'oauth'
require 'json'
require 'typhoeus'
require 'oauth/request_proxy/typhoeus_request'

# The code below sets the consumer key and secret from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export CONSUMER_KEY='YOUR-KEY', CONSUMER_SECRET='YOUR-SECRET'
consumer_key = ENV["CONSUMER_KEY"]
consumer_secret = ENV["CONSUMER_SECRET"]

# Be sure to replace your-list-id with your own list ID or one of an authenticating user
id = "your-list-id"

member_url = "https://api.twitter.com/2/lists/#{id}/members"

# Be sure to replace user-id-to-add with the user id you wish to add.
@user_id = { "user_id": "user-id-to-add" }

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


def add_member(url, oauth_params)
	options = {
	    :method => :post,
	    headers: {
	     	"User-Agent": "v2addMemberRuby",
        "content-type": "application/json"
	    },
	    body: JSON.dump(@user_id)
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


response = add_member(member_url, oauth_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
