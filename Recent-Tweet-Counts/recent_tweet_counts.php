<?php

/**
 * To use these samples you will need to set your bearer token as an environment variable by running the following in your terminal:
 * export BEARER_TOKEN='<your_bearer_token>'
 */

const RECENT_TWEET_COUNTS_API_URL = 'https://api.twitter.com/2/tweets/counts/recent';

/**
 * @throws Exception
 */
function get_recent_tweet_counts(array $headers, array $params = []): array
{
    $url = RECENT_TWEET_COUNTS_API_URL.'?'.http_build_query($params);

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    $response = curl_exec($curl);
    $status_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    curl_close($curl);

    $decoded_response = json_decode($response, true);

    if ($status_code != 200) {
        $errors = print_r($decoded_response['errors'] ?? $decoded_response, true);

        throw new Exception("Unable to get tweet counts: ".$errors, $status_code);
    }

    return $decoded_response['data'] ?? [];
}

try {
    $headers = [
        'Authorization: Bearer '.getenv('BEARER_TOKEN')
    ];

    // update the array below with your parameters:
    // https://developer.twitter.com/en/docs/twitter-api/tweets/counts/api-reference/get-tweets-counts-recent
    $params = [
        'query' => 'from:twitterdev',
        'granularity' => 'day'
    ];

    $tweet_counts = get_recent_tweet_counts($headers, $params);
} catch (Exception $e) {
    print $e->getMessage();
}
