<?php

/**
 * To use these samples you will need to set your bearer token as an environment variable by running the following in your terminal:
 * export BEARER_TOKEN='<your_bearer_token>'
 */

/**
 * API endpoints
 */
const FILTERED_STREAM_API_URL = 'https://api.twitter.com/2/tweets/search/stream';
const FILTERED_STREAM_RULES_API_URL = 'https://api.twitter.com/2/tweets/search/stream/rules';

/**
 * Sample rules for testing
 */
const SAMPLE_RULES = [
    [
        'value' => "cat has:media",
        'tag' => "cats with media"
    ],
    [
        'value' => "cat has:media -grumpy",
        'tag' => "happy cats with media"
    ],
    [
        'value' => "meme",
        'tag' => "funny things"
    ],
    [
        'value' => "meme has:images",
    ],
];

/**
 * Retrieve existing rules
 *
 * @throws Exception
 */
function get_rules(array $headers): array
{
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, FILTERED_STREAM_RULES_API_URL);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    $response = curl_exec($curl);
    $status_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    curl_close($curl);

    $decoded_response = json_decode($response, true);

    if ($status_code != 200) {
        $errors = print_r($decoded_response['errors'] ?? $decoded_response, true);
        
        throw new Exception("Unable to retrieve rules: ".$errors, $status_code);
    }

    return $decoded_response['data'] ?? [];
}

/**
 * Delete a set of rules
 *
 * @throws Exception
 */
function delete_rules(array $rules, array $headers): array
{
    $headers[] = 'Content-Type: application/json';

    // extracting the ids from the rules array
    $ids = array_map(function($rule) {
        return $rule['id'];
    }, $rules);

    $params = [
        'delete' => [
            'ids' => $ids
        ]
    ];

    $url = FILTERED_STREAM_RULES_API_URL.'?'.http_build_query($params);

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
        
        throw new Exception("Unable to delete rules: ".$errors, $status_code);
    }

    return $decoded_response;
}

/**
 * Set new rules
 *
 * @throws Exception
 */
function set_rules(array $rules, array $headers): array
{
    $headers[] = 'Content-Type: application/json';

    $params = [
        'add' => $rules
    ];

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, FILTERED_STREAM_RULES_API_URL);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($params));
    $response = curl_exec($curl);
    $status_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    curl_close($curl);

    $decoded_response = json_decode($response, true);

    if (!in_array($status_code, [200, 201])) {
        $errors = print_r($decoded_response['errors'] ?? $decoded_response, true);
        
        throw new Exception("Unable to set rules: ".$errors, $status_code);
    }

    return $decoded_response['data'] ?? [];
}

/**
 * @throws Exception
 */
function stream(array $headers): void
{
    // handle what to do with the tweets in this function, in this example we'll be printing the response
    $callback = function($ch, $str) {
        print $str;

        // remove this line if you only want to use one tweet
        return strlen($str);
    };

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, FILTERED_STREAM_API_URL);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_WRITEFUNCTION, $callback);
    curl_exec($curl);
    curl_close($curl);
}

try {
    // default headers to use with every request
    $headers = [
        'Authorization: Bearer '.getenv('BEARER_TOKEN')
    ];

    $rules = get_rules($headers);

    $deleted_rules = delete_rules($rules, $headers);

    $rules = set_rules(SAMPLE_RULES, $headers);

    // start the stream
    stream($headers);
} catch (Exception $e) {
    print $e->getMessage();
}
