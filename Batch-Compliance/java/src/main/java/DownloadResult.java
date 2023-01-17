import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * Sample code to download batch compliance result
 * */
public class DownloadResult {

  public static void main(String args[]) throws IOException, URISyntaxException {
    // Replace with your job downloadUrl below
    String downloadUrl = "";
    String response = getResults(downloadUrl);
    System.out.println(response);
  }

  /*
   * This method gets the results for a batch compliance job
   * */
  private static String getResults(String downloadUrl) throws IOException, URISyntaxException {
    String jobResponse = null;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder(downloadUrl);

    HttpGet httpGet = new HttpGet(uriBuilder.build());

    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      jobResponse = EntityUtils.toString(entity, "UTF-8");
    }
    return jobResponse;
  }

}