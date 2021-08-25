import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * Sample code to upload a dataset for compliance
 * */
public class UploadDataset {

  public static void main(String args[]) throws IOException {
    // Replace with your path to the file that contains the Tweet or User Ids
    String path = "";
    // Replace the upload url with the appropriate url
    String uploadUrl = "";
    String response = uploadDataset(uploadUrl, path);
    System.out.println(response);
  }

  /*
   * This method uploads a list of Tweet or User Ids to the upload_url
   * */
  private static String uploadDataset(String uploadUrl, String path) throws IOException {
    File file = new File(path);

    String jobResponse = null;

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    HttpPut httpPut = new HttpPut(uploadUrl);
    httpPut.setHeader("Content-Type", "text/plain");

    HttpEntity body = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).build();
    httpPut.setEntity(body);

    HttpResponse response = httpClient.execute(httpPut);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      jobResponse = EntityUtils.toString(entity, "UTF-8");
    }
    return jobResponse;
  }

}