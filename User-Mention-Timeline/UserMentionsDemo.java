import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/*
 * Sample code to demonstrate the use of the v2 User Mentions timeline endpoint
 * */
public class UserMentionsDemo {

  // To set your environment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException, URISyntaxException {
    final String bearerToken = System.getenv("BEARER_TOKEN");
    if (null != bearerToken) {
      //Replace with user ID below
      String response = getTweets("2244994945", bearerToken);
      System.out.println(response);
    } else {
      System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
    }
  }

  /*
   * This method calls the v2 User Mentions timeline endpoint by user ID
   * */
  private static String getTweets(String userId, String bearerToken) throws IOException, URISyntaxException {
    String tweetResponse = null;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/users/%s/mentions", userId));
    ArrayList<NameValuePair> queryParameters;
    queryParameters = new ArrayList<>();
    queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
    uriBuilder.addParameters(queryParameters);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpGet.setHeader("Content-Type", "application/json");

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      tweetResponse = EntityUtils.toString(entity, "UTF-8");
    }
    return tweetResponse;
  }
}
