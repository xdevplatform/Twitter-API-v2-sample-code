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

/**
 * Demonstrates the use of the Full Archive Search endpoint for Twitter API v2.
 */
public class FullArchiveSearchDemo {

    // To set your environment variables in your terminal, run the following line:
    // export BEARER_TOKEN='<your_bearer_token>'

    public static void main(String[] args) throws IOException, URISyntaxException {
        String bearerToken = System.getenv("BEARER_TOKEN");
        
        if (bearerToken != null && !bearerToken.isEmpty()) {
            // Replace the search term with a term of your choice
            String searchTerm = "from:TwitterDev OR from:SnowBotDev OR from:DailyNASA";
            try {
                String response = search(searchTerm, bearerToken);
                System.out.println("Search Response:");
                System.out.println(response);
            } catch (Exception e) {
                System.err.println("An error occurred during the API call: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("There was a problem getting your bearer token. "
                    + "Please make sure you set the BEARER_TOKEN environment variable.");
        }
    }

    /**
     * Calls the Full Archive Search endpoint with the search term passed as a query parameter.
     *
     * @param searchString The query string for the search
     * @param bearerToken  The Bearer Token for authentication
     * @return The search response as a String
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String search(String searchString, String bearerToken) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/all");
        ArrayList<NameValuePair> queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("max_results", "10")); // Adjust max_results as needed
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            return EntityUtils.toString(entity, "UTF-8");
        } else {
            throw new IOException("No response received from the API.");
        }
    }
}
