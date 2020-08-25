import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/*
 * Sample code to demonstrate the use of the Filtered Stream endpoint
 * */
public class FilteredStreamDemo {
  // To set your enviornment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException, URISyntaxException {
    String bearerToken = System.getenv("BEARER_TOKEN");
    if (null != bearerToken) {
      Map<String, String> rules = new HashMap<>();
      rules.put("cats has:images", "cat images");
      rules.put("dogs has:images", "dog images");
      setupRules(bearerToken, rules);
      connectStream(bearerToken);
    } else {
      System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
    }
  }

  /*
   * This method calls the filtered stream endpoint and streams Tweets from it
   * */
  private static void connectStream(String bearerToken) throws IOException, URISyntaxException {

    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream");

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

  /*
   * Helper method to setup rules before streaming data
   * */
  private static void setupRules(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException {
    List<String> existingRules = getRules(bearerToken);
    if (existingRules.size() > 0) {
      deleteRules(bearerToken, existingRules);
    }
    createRules(bearerToken, rules);
  }

  /*
   * Helper method to create rules for filtering
   * */
  private static void createRules(String bearerToken, Map<String, String> rules) throws URISyntaxException, IOException {
    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

    HttpPost httpPost = new HttpPost(uriBuilder.build());
    httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpPost.setHeader("content-type", "application/json");
    StringEntity body = new StringEntity(getFormattedString("{\"add\": [%s]}", rules));
    httpPost.setEntity(body);
    HttpResponse response = httpClient.execute(httpPost);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      System.out.println(EntityUtils.toString(entity, "UTF-8"));
    }
  }

  /*
   * Helper method to get existing rules
   * */
  private static List<String> getRules(String bearerToken) throws URISyntaxException, IOException {
    List<String> rules = new ArrayList<>();
    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpGet.setHeader("content-type", "application/json");
    HttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      JSONObject json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
      if (json.length() > 1) {
        JSONArray array = (JSONArray) json.get("data");
        for (int i = 0; i < array.length(); i++) {
          JSONObject jsonObject = (JSONObject) array.get(i);
          rules.add(jsonObject.getString("id"));
        }
      }
    }
    return rules;
  }

  /*
   * Helper method to delete rules
   * */
  private static void deleteRules(String bearerToken, List<String> existingRules) throws URISyntaxException, IOException {
    HttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

    HttpPost httpPost = new HttpPost(uriBuilder.build());
    httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
    httpPost.setHeader("content-type", "application/json");
    StringEntity body = new StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules));
    httpPost.setEntity(body);
    HttpResponse response = httpClient.execute(httpPost);
    HttpEntity entity = response.getEntity();
    if (null != entity) {
      System.out.println(EntityUtils.toString(entity, "UTF-8"));
    }
  }

  private static String getFormattedString(String string, List<String> ids) {
    StringBuilder sb = new StringBuilder();
    if (ids.size() == 1) {
      return String.format(string, "\"" + ids.get(0) + "\"");
    } else {
      for (String id : ids) {
        sb.append("\"" + id + "\"" + ",");
      }
      String result = sb.toString();
      return String.format(string, result.substring(0, result.length() - 1));
    }
  }

  private static String getFormattedString(String string, Map<String, String> rules) {
    StringBuilder sb = new StringBuilder();
    if (rules.size() == 1) {
      String key = rules.keySet().iterator().next();
      return String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
    } else {
      for (Map.Entry<String, String> entry : rules.entrySet()) {
        String value = entry.getKey();
        String tag = entry.getValue();
        sb.append("{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}" + ",");
      }
      String result = sb.toString();
      return String.format(string, result.substring(0, result.length() - 1));
    }
  }

}