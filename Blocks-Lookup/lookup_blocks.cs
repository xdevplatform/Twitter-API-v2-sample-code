using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using System.Security.Cryptography;
using System.Web;
using Newtonsoft.Json.Linq;

namespace TwitterAPIBlocksLookup
{
		class Program
		{
				static async Task Main(string[] args)
				{
						const string endpointURL = "https://api.twitter.com/2/users/your-user-id/blocking";
						const string requestTokenURL = "https://api.twitter.com/oauth/request_token?oauth_callback=oob";
						const string accessTokenURL = "https://api.twitter.com/oauth/access_token";
						const string consumer_key = Environment.GetEnvironmentVariable("CONSUMER_KEY");
						const string consumer_secret = Environment.GetEnvironmentVariable("CONSUMER_SECRET");
						string oauth_token, oauth_token_secret;
						var oauth = new OAuth1(consumer_key, consumer_secret);

						Console.Write("Enter the PIN (if you don't have it, please authorize the app via the link below): ");

						// Get request token
						var requestTokenResponse = await oauth.PostAsync(requestTokenURL, null);
						var requestTokens = HttpUtility.ParseQueryString(requestTokenResponse);
						oauth_token = requestTokens["oauth_token"];
						oauth_token_secret = requestTokens["oauth_token_secret"];

						// Get authorization
						var authorizeURL = new Uri("https://api.twitter.com/oauth/authorize");
						authorizeURL = authorizeURL.AddParameter("oauth_token", oauth_token);
						Console.WriteLine($"Please authorize the app via the following URL: {authorizeURL}");
						var pin = Console.ReadLine().Trim();

						// Get the access token
						var accessTokenParams = new Dictionary<string, string>
						{
								{"oauth_verifier", pin},
								{"oauth_token", oauth_token}
						};
						oauth_token_secret = requestTokens["oauth_token_secret"];
						var accessTokenResponse = await oauth.PostAsync(accessTokenURL, accessTokenParams, oauth_token_secret);
						var accessTokens = HttpUtility.ParseQueryString(accessTokenResponse);
						oauth_token = accessTokens["oauth_token"];
						oauth_token_secret = accessTokens["oauth_token_secret"];

						// Make the request
						var getRequestParams = new Dictionary<string, string>
						{
								{"user.fields", "id,name,created_at,description,location,url,public_metrics"},
								{"expansions", "pinned_tweet_id"}
						};
						var getRequestResponse = await oauth.GetAsync(endpointURL, getRequestParams, oauth_token_secret);
						var json = JObject.Parse(getRequestResponse);
						Console.WriteLine(json.ToString());
				}
		}

		static class Extensions
		{
				public static Uri AddParameter(this Uri uri, string paramName, string paramValue)
				{
						var queryString = HttpUtility.ParseQueryString(uri.Query);
						queryString[paramName] = paramValue;
						var builder = new UriBuilder(uri);
						builder.Query = queryString.ToString();
						return builder.Uri;
				}
		}

		class OAuth1
		{
				readonly string _consumerKey;
				readonly string _consumerSecret;

				public OAuth1(string consumerKey, string consumerSecret)
				{
						_consumerKey = consumerKey;
						_consumerSecret = consumerSecret;
				}

				private string GenerateNonce()
				{
						var random = new Random();
						var nonce = new StringBuilder();
						for (int i = 0; i < 32; i++)
						{
								nonce.Append(random.Next(0, 10));
						}
						return nonce.ToString();
				}

				private string GenerateTimestamp()
				{
					return ((int)(DateTime.UtcNow - new DateTime(1970, 1, 1)).TotalSeconds).ToString();
				}

					public async Task<string> PostAsync(string url, Dictionary<string, string> parameters, string accessToken, string accessTokenSecret)
				{
					var oauth = new OAuth()
				{
					ConsumerKey = consumerKey,
					ConsumerSecret = consumerSecret,
					AccessToken = accessToken,
					AccessTokenSecret = accessTokenSecret,
					SignatureMethod = OAuthSignatureMethod.HmacSha1,
					Version = "1.0",
				};

				var timestamp = GenerateTimestamp();
				var nonce = GenerateNonce();

				parameters.Add("oauth_consumer_key", oauth.ConsumerKey);
				parameters.Add("oauth_nonce", nonce);
				parameters.Add("oauth_signature_method", oauth.SignatureMethod.ToString());
				parameters.Add("oauth_timestamp", timestamp);
				parameters.Add("oauth_token", oauth.AccessToken);
				parameters.Add("oauth_version", oauth.Version);

				var signature = oauth.GenerateSignature(HttpMethod.Post.ToString(), url, parameters);
				parameters.Add("oauth_signature", signature);

				var client = new HttpClient();
				client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("OAuth", BuildOAuthHeaderString(parameters));

				var response = await client.PostAsync(url, new FormUrlEncodedContent(parameters));
				var responseString = await response.Content.ReadAsStringAsync();

				return responseString;
		}

		private string BuildOAuthHeaderString(Dictionary<string, string> parameters) {
			var headerParams = parameters
				.Where(p => p.Key.StartsWith("oauth_"))
				.OrderBy(p => p.Key)
				.Select(p => $"{Uri.EscapeDataString(p.Key)}=\"{Uri.EscapeDataString(p.Value)}\"");

			return string.Join(",", headerParams);
		}
	}
}

// Example usage:
// var twitterApi = new TwitterApi("YOUR-CONSUMER-KEY", "YOUR-CONSUMER-SECRET");
// var accessToken = await twitterApi.GetAccessTokenAsync("YOUR-REQUEST-TOKEN", "YOUR-REQUEST-TOKEN-SECRET", "PIN-CODE");
// var response = await twitterApi.PostAsync("https://api.twitter.com/1.1/statuses/update.json", new Dictionary<string, string> {{"status", "Hello Twitter!"}}, accessToken.Token, accessToken.TokenSecret);
