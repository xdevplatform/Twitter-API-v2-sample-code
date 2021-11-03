require 'oauth'
require 'json'
require 'typhoeus'
require 'oauth/request_proxy/typhoeus_request'

# The code below sets the consumer key and secret from your environment variables
# To set environment variables on Mac OS X, run the export command below from the terminal:
# export CONSUMER_KEY='YOUR-KEY', CONSUMER_SECRET='YOUR-SECRET'
consumer_key = ENV["CONSUMER_KEY"]
consumer_secret = ENV["CONSUMER_SECRET"]


create_tweet_url = "https://api.twitter.com/2/tweets"

# Be sure to add replace the text of the with the text you wish to Tweet.
# You can also add parameters to post polls, quote Tweets, Tweet with reply settings, and Tweet to Super Followers in addition to other features.
@json_payload = {"text": "Hello world!"}

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


def create_tweet(url, oauth_params)
	options = {
	    :method => :post,
	    headers: {
	     	"User-Agent": "v2CreateTweetRuby",
        "content-type": "application/json"
	    },
	    body: JSON.dump(@json_payload)
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


response = create_tweet(create_tweet_url, oauth_params)
puts response.code, JSON.pretty_generate(JSON.parse(response.body))
