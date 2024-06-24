import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

/*
 * Sample code to demonstrate the use of the Spaces lookup endpoint
 * */
public class SpacesLookupDemo {

  // To set your enviornment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException, URISyntaxException {
    String bearerToken = System.getenv("BEARER_TOKEN");
    if (null != bearerToken) {
      //Replace SPACE_ID with the ID of a Space
      String response = getSpaceById("SPACE_ID", bearerToken);
      System.out.println(response);
    } else {
      System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
    }
  }

  /*
   * This method calls the Spaces lookup endpoint with the ID passed to it as a query parameter
   * */
  private static String getSpaceById(String spaceId, String bearerToken) throws IOException, URISyntaxException {
    String searchResponse = null;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/spaces");
    ArrayList<NameValuePair> queryParameters;
    queryParameters = new ArrayList<>();
    queryParameters.add(new BasicNameValuePair("ids", spaceId));
    uriBuilder.addParameters(queryParameters);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpGet.setHeader("User-Agent", "v2SpacesLookupJava");
    httpGet.setHeader("Content-Type", "application/json");

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      searchResponse = EntityUtils.toString(entity, "UTF-8");
    }
    return searchResponse;
  }

}