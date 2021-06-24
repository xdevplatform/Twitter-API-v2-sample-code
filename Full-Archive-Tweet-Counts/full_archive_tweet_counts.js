// Search for public Tweets across the whole Twitter archive
// https://developer.twitter.com/en/docs/twitter-api/tweets/search/quick-start/full-archive-search

const needle = require('needle');

// The code below sets the bearer token from your environment variables
// To set environment variables on macOS or Linux, run the export command below from the terminal:
// export BEARER_TOKEN='YOUR-TOKEN'
const token = process.env.BEARER_TOKEN;

const endpointUrl = 'https://api.twitter.com/2/tweets/counts/all'

async function getRequest() {

    // Edit query parameters below and specify a search query
    // optional params: start_time,end_time,since_id,until_id,next_token,granularity
    const params = {
        'query': 'from:twitterdev',
        'granularity': 'day',
        'start_time': '2021-01-01T00:00:00Z'
    }

    const res = await needle('get', endpointUrl, params, {
        headers: {
            "User-Agent": "v2FullArchiveTweetCountsJS",
            "authorization": `Bearer ${token}`
        }
    })

    if (res.body) {
        return res.body;
    } else {
        throw new Error('Unsuccessful request');
    }
}

(async () => {

    try {
        // Make request
        const response = await getRequest();
        console.dir(response, {
            depth: null
        });

    } catch (e) {
        console.log(e);
        process.exit(-1);
    }
    process.exit();
})();
