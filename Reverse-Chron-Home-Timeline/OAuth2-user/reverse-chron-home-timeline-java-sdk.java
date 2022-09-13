package com.twitter.clientlib.auth;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.ApiClient;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.Configuration;
import com.twitter.clientlib.auth.*;
import com.twitter.clientlib.model.*;
import com.twitter.clientlib.TwitterCredentialsOAuth2;


import com.twitter.clientlib.api.TwitterApi;

import com.twitter.clientlib.api.BookmarksApi;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.time.OffsetDateTime;
import org.json.*;

/**
* This is an example of getting an OAuth2 access token and using it to call an API.
* It's expected to set TWITTER_OAUTH2_CLIENT_ID & TWITTER_OAUTH2_CLIENT_SECRET in TwitterCredentialsOAuth2
*
* Example steps:
* 1. Getting the App Authorization URL.
* 2. User should click the URL and authorize it.
* 3. After receiving the access token, setting the values into TwitterCredentialsOAuth2.
* 4. Call the API.
*/

public class OAuth20GetAccessToken {

  public static void main(String[] args) {
    OAuth20GetAccessToken example = new OAuth20GetAccessToken();
    TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2("REPLACE-WITH-CLIENT-ID",
        "REPLACE-WITH-CLIENT-SECRET",
        null,
        null);

    OAuth2AccessToken accessToken = example.getAccessToken(credentials);
    if (accessToken == null) {
      return;
    }

    // Setting the access & refresh tokens into TwitterCredentialsOAuth2
    credentials.setTwitterOauth2AccessToken(accessToken.getAccessToken());
    credentials.setTwitterOauth2RefreshToken(accessToken.getRefreshToken());
    example.callApi(credentials);
  }

  public OAuth2AccessToken getAccessToken(TwitterCredentialsOAuth2 credentials) {
    TwitterOAuth20Service service = new TwitterOAuth20Service(
        credentials.getTwitterOauth2ClientId(),
        credentials.getTwitterOAuth2ClientSecret(),
        "https://www.example.com/oauth",
        "offline.access tweet.read users.read");

    OAuth2AccessToken accessToken = null;
    try {
      final Scanner in = new Scanner(System.in, "UTF-8");
      System.out.println("Fetching the Authorization URL...");

      final String secretState = "state";
      PKCE pkce = new PKCE();
      pkce.setCodeChallenge("challenge");
      pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
      pkce.setCodeVerifier("challenge");
      String authorizationUrl = service.getAuthorizationUrl(pkce, secretState);

      System.out.println("Go to the Authorization URL and authorize your App:\n" +
          authorizationUrl + "\nAfter that paste the authorization code here\n>>");
      final String code = in.nextLine();
      System.out.println("\nTrading the Authorization Code for an Access Token...");
      accessToken = service.getAccessToken(pkce, code);

      System.out.println("Access token: " + accessToken.getAccessToken());
      System.out.println("Refresh token: " + accessToken.getRefreshToken());
    } catch (Exception e) {
      System.err.println("Error while getting the access token:\n " + e);
      e.printStackTrace();
    }
    return accessToken;
  }

  public void callApi(TwitterCredentialsOAuth2 credentials) {
    TwitterApi apiInstance = new TwitterApi();
    apiInstance.setTwitterCredentials(credentials);

    // Set the params values
    String sinceId = "791775337160081409"; // String | The minimum Tweet ID to be included in the result set. This parameter takes precedence over start_time if both are specified.
    String untilId = "1346889436626259968"; // String | The maximum Tweet ID to be included in the result set. This parameter takes precedence over end_time if both are specified.
    Integer maxResults = 56; // Integer | The maximum number of results
    String paginationToken = "paginationToken_example"; // String | This parameter is used to get the next 'page' of results.
    OffsetDateTime startTime = OffsetDateTime.parse("2021-02-01T18:40:40.000Z"); // OffsetDateTime | YYYY-MM-DDTHH:mm:ssZ. The earliest UTC timestamp from which the Tweets will be provided. The since_id parameter takes precedence if it is also specified.
    OffsetDateTime endTime = OffsetDateTime.parse("2021-02-14T18:40:40.000Z"); // OffsetDateTime | YYYY-MM-DDTHH:mm:ssZ. The latest UTC timestamp to which the Tweets will be provided. The until_id parameter takes precedence if it is also specified.
    Set<String> expansions = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of fields to expand.
    Set<String> tweetFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of Tweet fields to display.
    Set<String> userFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of User fields to display.
    Set<String> mediaFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of Media fields to display.
    Set<String> placeFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of Place fields to display.
    Set<String> pollFields = new HashSet<>(Arrays.asList()); // Set<String> | A comma separated list of Poll fields to display.

    try {
      //Gets the authorized user ID and parses it into an JSON object
      SingleUserLookupResponse userData = apiInstance.users().findMyUser(null, null, null);
      String jsonString = userData.getData().toJson();
      JSONObject obj = new JSONObject(jsonString);
      String userId = obj.getString("id");
      
      //Passes the parsed ID into the userIdTimeline request
      GenericTweetsTimelineResponse result = apiInstance.tweets().usersIdTimeline(userId, sinceId, untilId, maxResults, null, paginationToken, startTime, endTime, expansions, tweetFields, userFields, mediaFields, placeFields, pollFields);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#usersIdTimeline");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
