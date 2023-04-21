using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;

using Newtonsoft.Json.Linq;

using OAuth1Net;

namespace TwitterOAuth
{
    class Program
    {
        static async Task Main(string[] args)
        {
            var consumerKey = Environment.GetEnvironmentVariable("CONSUMER_KEY");
            var consumerSecret = Environment.GetEnvironmentVariable("CONSUMER_SECRET");
            var userId = "your-user-id";

            var oauth = new OAuthWorkflow
            {
                ConsumerKey = consumerKey,
                ConsumerSecret = consumerSecret,
                RequestTokenUrl = "https://api.twitter.com/oauth/request_token",
                AuthorizationUrl = "https://api.twitter.com/oauth/authorize",
                AccessTokenUrl = "https://api.twitter.com/oauth/access_token"
            };

            // Get the request token
            var requestTokenResponse = await oauth.GetRequestToken();
            if (requestTokenResponse.HttpStatusCode != 200)
            {
                Console.WriteLine($"Failed to get the request token. Status code: {requestTokenResponse.HttpStatusCode}, content: {requestTokenResponse.Content}");
                return;
            }

            var authorizationUrl = oauth.BuildAuthorizationUrl(requestTokenResponse.Token);
            Console.WriteLine($"Please visit the following URL to authorize the app: {authorizationUrl}");
            Console.Write("Enter the PIN provided by Twitter: ");
            var pin = Console.ReadLine();

            // Get the access token
            var accessTokenResponse = await oauth.GetAccessToken(requestTokenResponse.Token, requestTokenResponse.TokenSecret, pin);
            if (accessTokenResponse.HttpStatusCode != 200)
            {
                Console.WriteLine($"Failed to get the access token. Status code: {accessTokenResponse.HttpStatusCode}, content: {accessTokenResponse.Content}");
                return;
            }

            var accessToken = accessTokenResponse.Token;
            var accessTokenSecret = accessTokenResponse.TokenSecret;

            var client = new HttpClient();
            var baseUrl = "https://api.twitter.com/2/users/" + userId + "/blocking";
            var queryString = HttpUtility.ParseQueryString(string.Empty);
            queryString["user.fields"] = "created_at,description";
            var url = baseUrl + "?" + queryString.ToString();

            // Make the request
            var auth = new HttpAuthorizationHeader
            {
                Realm = "Twitter API",
                OAuthConsumerKey = consumerKey,
                OAuthConsumerSecret = consumerSecret,
                OAuthToken = accessToken,
                OAuthTokenSecret = accessTokenSecret
            };
            var request = new HttpRequestMessage(HttpMethod.Get, url);
            request.Headers.Authorization = auth.Build("GET", url);

            var response = await client.SendAsync(request);
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"Request failed. Status code: {response.StatusCode}");
                return;
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine(JToken.Parse(jsonResponse).ToString(Newtonsoft.Json.Formatting.Indented));
        }
    }
}
