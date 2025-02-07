<?php

/**
 * To use these samples you will need to set your bearer token as an environment variable by running the following in your terminal:
 * export BEARER_TOKEN='<your_bearer_token>'
 */

const SAMPLE_STREAM_API_URL = 'https://api.twitter.com/2/tweets/sample/stream';

/**
 * @throws Exception
 */
function stream(array $headers, array $params = []): void
{
    // handle what to do with the tweets in this function, in this example we'll be printing the response
    $callback = function($ch, $str) {
        print $str;

        // remove this line if you only want to use one tweet
        return strlen($str);
    };

    $url = SAMPLE_STREAM_API_URL.'?'.http_build_query($params);

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_WRITEFUNCTION, $callback);
    curl_exec($curl);
    curl_close($curl);
}

try {
    $headers = [
        'Authorization: Bearer '.getenv('BEARER_TOKEN')
    ];

    // update the array below with your parameters:
    // https://developer.twitter.com/en/docs/twitter-api/tweets/sampled-stream/api-reference/get-tweets-sample-stream
    $params = [
        'tweet.fields' => 'author_id'
    ];

    // start the stream
    stream($headers, $params);
} catch (Exception $e) {
    print $e->getMessage();
}
