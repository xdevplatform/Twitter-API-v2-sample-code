import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * Sample code to create a batch compliance job
 * */
public class CreateJob {

  // To set your enviornment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException {
    final String bearerToken = System.getenv("BEARER_TOKEN");
    if (null != bearerToken) {
      //Specify if you want to create a job for tweets or users
      String response = createJob("tweets", bearerToken);
      System.out.println(response);
    } else {
      System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
    }
  }

  /*
   * This method calls the batch compliance endpoint to create a new job
   * */
  private static String createJob(String type, String bearerToken) throws IOException {
    String jobResponse = null;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    HttpPost httpPost = new HttpPost("https://api.twitter.com/2/compliance/jobs");
    httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpPost.setHeader("Content-Type", "application/json");

    String body = String.format("{\n" +
        "    \"type\": \"%s\"\n" +
        "}", type);
    HttpEntity requestEntity = new ByteArrayEntity(body.getBytes("UTF-8"));
    httpPost.setEntity(requestEntity);

    HttpResponse response = httpClient.execute(httpPost);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      jobResponse = EntityUtils.toString(entity, "UTF-8");
    }
    return jobResponse;
  }

}