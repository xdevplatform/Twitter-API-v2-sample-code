import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/*
 * Sample code to demonstrate the use of the Sampled Stream endpoint
 * */
public class SampledStreamDemo {

  // To set your enviornment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException, URISyntaxException {
    String bearerToken = System.getenv("BEARER_TOKEN");
    if (null != bearerToken) {
      connectStream(bearerToken);
    } else {
      System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
    }

  }

  /*
   * This method calls the sample stream endpoint and streams Tweets from it
   * */
  private static void connectStream(String bearerToken) throws IOException, URISyntaxException {

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/sample/stream");

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      BufferedReader reader = new BufferedReader(new InputStreamReader((entity.getContent())));
      String line = reader.readLine();
      while (line != null) {
        System.out.println(line);
        line = reader.readLine();
      }
    }

  }
}