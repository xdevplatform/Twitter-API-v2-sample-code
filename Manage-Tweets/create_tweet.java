package org.example;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

import com.google.gson.JsonObject;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class create_tweet {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, GeneralSecurityException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        HttpClient client = HttpClient.newHttpClient();

        // Create the OAuth parameters
        String consumerKey = "CONSUMER_KEY"; // Your consumer key
        String consumerSecret = "CONSUMER_SECRET"; // Your consumer secret
        String token = "ACCESS_TOKEN"; // Your access token
        String tokenSecret = "ACCESS_TOKEN_SECRET"; // Your access token secret
        String nonce = randomNonce(); // A random string
        String timestamp = String.valueOf(Instant.now().getEpochSecond()); // The current time in seconds since epoch
        String signatureMethod = "HMAC-SHA1"; // The signature method
        String version = "1.0"; // The OAuth version
        String uri = "https://api.twitter.com/2/tweets";

        JsonObject data = new JsonObject();
        data.addProperty("text", "Your tweet data");

        String authorizationHeader = generateHeader(uri, consumerKey, consumerSecret, token, tokenSecret, nonce, signatureMethod, timestamp, version);

        // Create an HttpRequest object with the POST method, the URL, the headers and the body
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data.toString())) // The body of the request as a query string
                .uri(URI.create("https://api.twitter.com/2/tweets")) // The URL of the API endpoint
                .header("Content-Type", "application/json") // The content type header
                .header("Authorization", authorizationHeader) // The authorization header with the OAuth parameters
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the status code and the body of the response
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    private static String generateHeader(String uri, String consumerKey, String consumerSecret, String token, String tokenSecret, String nonce, String signatureMethod, String timestamp, String version) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // Create the signature base string
        String signatureBaseString = "POST&" +
                URLEncoder.encode(uri, "UTF-8") + "&" +
                URLEncoder.encode("oauth_consumer_key=" + consumerKey + "&", "UTF-8") +
                URLEncoder.encode("oauth_nonce=" + nonce + "&", "UTF-8") +
                URLEncoder.encode("oauth_signature_method=" + signatureMethod + "&", "UTF-8") +
                URLEncoder.encode("oauth_timestamp=" + timestamp + "&", "UTF-8") +
                URLEncoder.encode("oauth_token=" + token + "&", "UTF-8") +
                URLEncoder.encode("oauth_version=" + version, "UTF-8");

        // Generate the signing key
        String signingKey = URLEncoder.encode(consumerSecret, "UTF-8") + "&" +
                URLEncoder.encode(tokenSecret, "UTF-8");

        // Generate the signature using HMAC-SHA1
        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(signatureBaseString.getBytes());
        String signature = Base64.getEncoder().encodeToString(result);

        // Create the authorization header
        String authorizationHeader = "OAuth " + "oauth_consumer_key=\"" + consumerKey + "\"," +
                "oauth_token=\"" + token + "\"," +
                "oauth_signature_method=\"" + signatureMethod + "\"," +
                "oauth_timestamp=\"" + timestamp + "\"," +
                "oauth_nonce=\"" + nonce + "\"," +
                "oauth_version=\"" + version + "\"," +
                "oauth_signature=\"" + URLEncoder.encode(signature, "UTF-8") + "\"";

        return authorizationHeader;
    }

    private static String randomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        String randomNonce = String.format("%06d", secureRandom.nextInt(1000000000));
        return randomNonce;
    }
}
